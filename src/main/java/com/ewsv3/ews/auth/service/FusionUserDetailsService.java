package com.ewsv3.ews.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Resolves the calling user against Oracle Fusion HCM by invoking
 * UserDetailsServiceV2.findSelfUserDetails with the Fusion-issued JWT as a
 * bearer token. Replaces the JAX-WS Dispatch based call used by the older
 * Oracle ADF application (JAX-WS is no longer part of the JDK).
 */
@Service
public class FusionUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(FusionUserDetailsService.class);

    private static final String USER_DETAILS_NS = "http://xmlns.oracle.com/apps/hcm/people/roles/userDetailsServiceV2/";

    private static final String USER_DETAILS_TYPES_NS = "http://xmlns.oracle.com/apps/hcm/people/roles/userDetailsServiceV2/types/";

    private static final String SOAP_ACTION = USER_DETAILS_NS + "findSelfUserDetails";

    @Value("${oracle.fusion.user-details-endpoint}")
    private String userDetailsEndpoint;

    @Autowired
    @Qualifier("hcmRestTemplate")
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Calls findSelfUserDetails with the given JWT and returns the Fusion UserId,
     * or null when the service does not return one.
     */
    public String findSelfUserId(String jwt) {
        String soapResponse = callFindSelfUserDetails(jwt);
        return extractUserId(soapResponse);
    }

    /**
     * Decodes the JWT payload (without signature verification - the token is
     * validated by Fusion when findSelfUserDetails is invoked) and returns the
     * subject claim. Used for logging/traceability only.
     */
    public String extractSubject(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JsonNode node = objectMapper.readTree(payload);
            if (node.hasNonNull("sub")) {
                return node.get("sub").asText();
            }
            if (node.hasNonNull("prn")) {
                return node.get("prn").asText();
            }
            return null;
        } catch (Exception e) {
            logger.warn("FUSION_JWT - Unable to decode JWT payload: {}", e.getMessage());
            return null;
        }
    }

    private String callFindSelfUserDetails(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/xml;charset=UTF-8"));
        headers.set("SOAPAction", SOAP_ACTION);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<String> entity = new HttpEntity<>(buildSoapRequest(), headers);

        logger.info("FUSION_USER_DETAILS - Calling findSelfUserDetails at {} with JWT subject: {}",
                userDetailsEndpoint, extractSubject(jwt));

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(userDetailsEndpoint, entity, String.class);
            logger.info("FUSION_USER_DETAILS - SOAP call successful - Status: {}, Body: {}", response.getStatusCode(),
                    response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("FUSION_USER_DETAILS - SOAP call failed - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Oracle Fusion UserDetailsService call failed with status "
                    + e.getStatusCode(), e);
        }
    }

    private String buildSoapRequest() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:typ=\"" + USER_DETAILS_TYPES_NS + "\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<typ:findSelfUserDetails/>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private String extractUserId(String soapResponse) {

        if (soapResponse == null || soapResponse.isBlank()) {
            return null;
        }

        try {
            logger.info(
                    "FUSION_USER_DETAILS - SOAP response length: {}",
                    soapResponse.length());

            // The Oracle Fusion response is multipart MIME.
            // Extract only the SOAP XML envelope.
            int xmlStart = soapResponse.indexOf("<env:Envelope");
            int xmlEnd = soapResponse.indexOf("</env:Envelope>");

            if (xmlStart < 0 || xmlEnd < 0) {
                logger.warn(
                        "FUSION_USER_DETAILS - SOAP Envelope not found in response");
                return null;
            }

            xmlEnd += "</env:Envelope>".length();

            String xml = soapResponse.substring(xmlStart, xmlEnd);

            logger.info(
                    "FUSION_USER_DETAILS - XML content starts at character {}",
                    xmlStart);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);

            // XXE protection
            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true);

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false);

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false);

            factory.setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document = factory
                    .newDocumentBuilder()
                    .parse(
                            new InputSource(
                                    new StringReader(xml)));

            XPath xpath = XPathFactory
                    .newInstance()
                    .newXPath();

            String userId = xpath.evaluate(
                    "//*[local-name()='UserId']/text()",
                    document);

            logger.info(
                    "FUSION_USER_DETAILS - Extracted UserId: {}",
                    userId);

            return userId != null && !userId.isBlank()
                    ? userId.trim()
                    : null;

        } catch (Exception e) {

            logger.error(
                    "FUSION_USER_DETAILS - Unable to parse SOAP response: {}",
                    e.getMessage(),
                    e);

            throw new RuntimeException(
                    "Unable to parse Oracle Fusion UserDetailsService response",
                    e);
        }
    }

    private String extractUserId_old(String soapResponse) {
        if (soapResponse == null || soapResponse.isBlank()) {
            return null;
        }
        try {
            logger.info("FUSION_USER_DETAILS - Parsing SOAP response {}", soapResponse);

            String xml = soapResponse;

            logger.info("FUSION_USER_DETAILS - First characters: {}",
                    soapResponse.chars()
                            .limit(10)
                            .mapToObj(c -> String.format("U+%04X", c))
                            .toList());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Security: prevent XXE attacks
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document = factory
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            logger.info("FUSION_USER_DETAILS - Parsed SOAP response successfully, document: {}", document);

            XPath xpath = XPathFactory.newInstance().newXPath();

            logger.info("FUSION_USER_DETAILS - Extracting UserId from SOAP response using XPath {}", xpath);

            String userId = xpath.evaluate(
                    "//*[local-name()='UserId']",
                    document);

            logger.info("FUSION_USER_DETAILS - Extracted UserId: {}", userId);

            return userId != null && !userId.isBlank() ? userId.trim() : null;

            // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // factory.setNamespaceAware(true);
            // factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",
            // true);
            // factory.setXIncludeAware(false);
            // factory.setExpandEntityReferences(false);
            // DocumentBuilder builder = factory.newDocumentBuilder();
            // Document doc = builder.parse(new InputSource(new
            // StringReader(soapResponse)));
            // logger.info("FUSION_USER_DETAILS - Parsed SOAP response successfully");
            // logger.debug("FUSION_USER_DETAILS - doc: {}", doc);

            // NodeList nodes = doc.getElementsByTagNameNS(USER_DETAILS_NS, "ns1:UserId");
            // logger.info("FUSION_USER_DETAILS - Extracted UserId nodes count: {}",
            // nodes.getLength());
            // if (nodes.getLength() == 0) {
            // // Fall back to a namespace-agnostic lookup
            // nodes = doc.getElementsByTagName("ns1:UserId");
            // logger.info("FUSION_USER_DETAILS - Fallback UserId nodes count: {}",
            // nodes.getLength());
            // }
            // if (nodes.getLength() == 0 || nodes.item(0).getChildNodes().getLength() == 0)
            // {
            // logger.warn("FUSION_USER_DETAILS - UserId element not found in SOAP
            // response");
            // return null;
            // }
            // Node first = nodes.item(0).getFirstChild();
            // String userId = first != null ? first.getNodeValue() : null;
            // return userId != null && !userId.isBlank() ? userId.trim() : null;
        } catch (Exception e) {
            logger.error("FUSION_USER_DETAILS - Unable to parse SOAP response: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to parse Oracle Fusion UserDetailsService response", e);
        }
    }
}
