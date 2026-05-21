package com.ewsv3.ews.hcmintegrations.service;

import com.ewsv3.ews.hcmintegrations.dto.PersonScheduleDto;
import com.ewsv3.ews.hcmintegrations.dto.ScheduleReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class HcmIntegrationService {

    @Value("${oracle.hcm.soap-endpoint}")
    private String soapEndpoint;

    @Value("${oracle.hcm.username}")
    private String username;

    @Value("${oracle.hcm.password}")
    private String password;

    @Value("${oracle.hcm.schedule-report-path}")
    private String reportPath;

    @Value("${oracle.hcm.schedules-report-path}")
    private String schedulesReportPath;

    @Autowired
    @Qualifier("hcmRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String DELETE_SQL = "DELETE FROM ISC_SCHEDULE_ASSIGNMENTS";

    private static final String INSERT_SQL =
            "INSERT INTO ISC_SCHEDULE_ASSIGNMENTS " +
            "(SCHEDULE_ASSIGNMENT_ID, PERSON_ID, PERSON_NUMBER, ASSIGNMENT_NUMBER, SCHEDULE_NAME, " +
            "EFFECTIVE_FROM_DATE, EFFECTIVE_TO_DATE, START_DATE, END_DATE, \"PRIMARY\") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String DELETE_SCHEDULES_SQL = "DELETE FROM ISC_SCHEDULES";

    private static final String INSERT_SCHEDULES_SQL =
            "INSERT INTO ISC_SCHEDULES " +
            "(SCHEDULE_ID, SCHEDULE_NAME, SCHEDULE_DESC, SCHEDULE_TYPE_CODE, SCHEDULE_CATEGORY, " +
            "EFFECTIVE_FROM_DATE, EFFECTIVE_TO_DATE, FIRST_WEEK, PATTERN_ID, PATTERN_SEQ_NUM, " +
            "PATTERN_NAME, PATTERN_TYPE_CODE, LENGTH_DAYS_NUM, START_DAY, END_DAY, " +
            "SHIFT_ID, SHIFT_NAME, SHIFT_TYPE_CODE, CODE, SHIFT_CATEGORY_CODE, " +
            "START_TIME, DURATION, DUR) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Transactional
    public int syncPersonSchedules() throws Exception {
        List<PersonScheduleDto> records = fetchFromHcm();
        jdbcTemplate.execute(DELETE_SQL);
        batchInsert(records);
        return records.size();
    }

    public List<PersonScheduleDto> fetchPersonSchedules() throws Exception {
        return fetchFromHcm();
    }

    public List<PersonScheduleDto> getStoredPersonSchedules() {
        return jdbcTemplate.query(
                "SELECT SCHEDULE_ASSIGNMENT_ID, PERSON_ID, PERSON_NUMBER, ASSIGNMENT_NUMBER, SCHEDULE_NAME, " +
                "EFFECTIVE_FROM_DATE, EFFECTIVE_TO_DATE, START_DATE, END_DATE, \"PRIMARY\" FROM ISC_SCHEDULE_ASSIGNMENTS",
                (rs, rowNum) -> {
                    PersonScheduleDto dto = new PersonScheduleDto();
                    dto.setScheduleAssignmentId(toLong(rs.getObject("SCHEDULE_ASSIGNMENT_ID")));
                    dto.setPersonId(toLong(rs.getObject("PERSON_ID")));
                    dto.setPersonNumber(rs.getString("PERSON_NUMBER"));
                    dto.setAssignmentNumber(rs.getString("ASSIGNMENT_NUMBER"));
                    dto.setScheduleName(rs.getString("SCHEDULE_NAME"));
                    dto.setEffectiveFromDate(toLocalDate(rs.getDate("EFFECTIVE_FROM_DATE")));
                    dto.setEffectiveToDate(toLocalDate(rs.getDate("EFFECTIVE_TO_DATE")));
                    dto.setStartDate(toLocalDate(rs.getDate("START_DATE")));
                    dto.setEndDate(toLocalDate(rs.getDate("END_DATE")));
                    dto.setPrimary(rs.getString("PRIMARY"));
                    return dto;
                });
    }

    @Transactional
    public int syncSchedules() throws Exception {
        List<ScheduleReportDto> records = fetchSchedulesFromHcm();
        jdbcTemplate.execute(DELETE_SCHEDULES_SQL);
        batchInsertSchedules(records);
        return records.size();
    }

    public List<ScheduleReportDto> fetchSchedulesFromHcm() throws Exception {
        String soapResponse = callBipSoapWithPath(schedulesReportPath);
        byte[] reportBytes = extractReportBytes(soapResponse);
        return parseSchedulesXml(reportBytes);
    }

    public List<ScheduleReportDto> getStoredSchedules() {
        return jdbcTemplate.query(
                "SELECT SCHEDULE_ID, SCHEDULE_NAME, SCHEDULE_DESC, SCHEDULE_TYPE_CODE, SCHEDULE_CATEGORY, " +
                "EFFECTIVE_FROM_DATE, EFFECTIVE_TO_DATE, FIRST_WEEK, PATTERN_ID, PATTERN_SEQ_NUM, " +
                "PATTERN_NAME, PATTERN_TYPE_CODE, LENGTH_DAYS_NUM, START_DAY, END_DAY, " +
                "SHIFT_ID, SHIFT_NAME, SHIFT_TYPE_CODE, CODE, SHIFT_CATEGORY_CODE, " +
                "START_TIME, DURATION, DUR FROM ISC_SCHEDULES",
                (rs, rowNum) -> {
                    ScheduleReportDto dto = new ScheduleReportDto();
                    dto.setScheduleId(toLong(rs.getObject("SCHEDULE_ID")));
                    dto.setScheduleName(rs.getString("SCHEDULE_NAME"));
                    dto.setScheduleDesc(rs.getString("SCHEDULE_DESC"));
                    dto.setScheduleTypeCode(rs.getString("SCHEDULE_TYPE_CODE"));
                    dto.setScheduleCategory(rs.getString("SCHEDULE_CATEGORY"));
                    dto.setEffectiveFromDate(rs.getString("EFFECTIVE_FROM_DATE"));
                    dto.setEffectiveToDate(rs.getString("EFFECTIVE_TO_DATE"));
                    dto.setFirstWeek(rs.getString("FIRST_WEEK"));
                    dto.setPatternId(toLong(rs.getObject("PATTERN_ID")));
                    dto.setPatternSeqNum(toLong(rs.getObject("PATTERN_SEQ_NUM")));
                    dto.setPatternName(rs.getString("PATTERN_NAME"));
                    dto.setPatternTypeCode(rs.getString("PATTERN_TYPE_CODE"));
                    dto.setLengthDaysNum(rs.getString("LENGTH_DAYS_NUM"));
                    dto.setStartDay(rs.getString("START_DAY"));
                    dto.setEndDay(rs.getString("END_DAY"));
                    dto.setShiftId(toLong(rs.getObject("SHIFT_ID")));
                    dto.setShiftName(rs.getString("SHIFT_NAME"));
                    dto.setShiftTypeCode(rs.getString("SHIFT_TYPE_CODE"));
                    dto.setCode(rs.getString("CODE"));
                    dto.setShiftCategoryCode(rs.getString("SHIFT_CATEGORY_CODE"));
                    dto.setStartTime(rs.getString("START_TIME"));
                    dto.setDuration(rs.getString("DURATION"));
                    dto.setDur(toLong(rs.getObject("DUR")));
                    return dto;
                });
    }

    private void batchInsertSchedules(List<ScheduleReportDto> records) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (ScheduleReportDto dto : records) {
            batchArgs.add(new Object[]{
                    dto.getScheduleId(), dto.getScheduleName(), dto.getScheduleDesc(),
                    dto.getScheduleTypeCode(), dto.getScheduleCategory(),
                    dto.getEffectiveFromDate(), dto.getEffectiveToDate(), dto.getFirstWeek(),
                    dto.getPatternId(), dto.getPatternSeqNum(), dto.getPatternName(),
                    dto.getPatternTypeCode(), dto.getLengthDaysNum(),
                    dto.getStartDay(), dto.getEndDay(),
                    dto.getShiftId(), dto.getShiftName(), dto.getShiftTypeCode(),
                    dto.getCode(), dto.getShiftCategoryCode(),
                    dto.getStartTime(), dto.getDuration(), dto.getDur()
            });
        }
        jdbcTemplate.batchUpdate(INSERT_SCHEDULES_SQL, batchArgs);
    }

    private List<ScheduleReportDto> parseSchedulesXml(byte[] xmlBytes) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlBytes));

        // Find row elements by locating parents of SCHEDULE_ID nodes
        NodeList scheduleIdNodes = doc.getElementsByTagName("SCHEDULE_ID");
        Set<Element> rows = new LinkedHashSet<>();
        for (int i = 0; i < scheduleIdNodes.getLength(); i++) {
            rows.add((Element) scheduleIdNodes.item(i).getParentNode());
        }

        List<ScheduleReportDto> result = new ArrayList<>();
        for (Element row : rows) {
            ScheduleReportDto dto = new ScheduleReportDto();
            dto.setScheduleId(parseNum(getText(row, "SCHEDULE_ID")));
            dto.setScheduleName(getText(row, "SCHEDULE_NAME"));
            dto.setScheduleDesc(getText(row, "SCHEDULE_DESC"));
            dto.setScheduleTypeCode(getText(row, "SCHEDULE_TYPE_CODE"));
            dto.setScheduleCategory(getText(row, "SCHEDULE_CATEGORY"));
            dto.setEffectiveFromDate(getText(row, "EFFECTIVE_FROM_DATE"));
            dto.setEffectiveToDate(getText(row, "EFFECTIVE_TO_DATE"));
            dto.setFirstWeek(getText(row, "FIRST_WEEK"));
            dto.setPatternId(parseNum(getText(row, "PATTERN_ID")));
            dto.setPatternSeqNum(parseNum(getText(row, "PATTERN_SEQ_NUM")));
            dto.setPatternName(getText(row, "PATTERN_NAME"));
            dto.setPatternTypeCode(getText(row, "PATTERN_TYPE_CODE"));
            dto.setLengthDaysNum(getText(row, "LENGTH_DAYS_NUM"));
            dto.setStartDay(getText(row, "START_DAY"));
            dto.setEndDay(getText(row, "END_DAY"));
            dto.setShiftId(parseNum(getText(row, "SHIFT_ID")));
            dto.setShiftName(getText(row, "SHIFT_NAME"));
            dto.setShiftTypeCode(getText(row, "SHIFT_TYPE_CODE"));
            dto.setCode(getText(row, "CODE"));
            dto.setShiftCategoryCode(getText(row, "SHIFT_CATEGORY_CODE"));
            dto.setStartTime(getText(row, "START_TIME"));
            dto.setDuration(getText(row, "DURATION"));
            dto.setDur(parseNum(getText(row, "DUR")));
            result.add(dto);
        }
        return result;
    }

    private List<PersonScheduleDto> fetchFromHcm() throws Exception {
        String soapResponse = callBipSoapWithPath(reportPath);
        byte[] reportBytes = extractReportBytes(soapResponse);
        return parseReportXml(reportBytes);
    }

    private void batchInsert(List<PersonScheduleDto> records) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (PersonScheduleDto dto : records) {
            batchArgs.add(new Object[]{
                    dto.getScheduleAssignmentId(),
                    dto.getPersonId(),
                    dto.getPersonNumber(),
                    dto.getAssignmentNumber(),
                    dto.getScheduleName(),
                    toSqlDate(dto.getEffectiveFromDate()),
                    toSqlDate(dto.getEffectiveToDate()),
                    toSqlDate(dto.getStartDate()),
                    toSqlDate(dto.getEndDate()),
                    dto.getPrimary()
            });
        }
        jdbcTemplate.batchUpdate(INSERT_SQL, batchArgs);
    }

    private String callBipSoapWithPath(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/xml;charset=UTF-8"));
        headers.set("SOAPAction", "");
        HttpEntity<String> entity = new HttpEntity<>(buildSoapRequest(path), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(soapEndpoint, entity, String.class);
        return response.getBody();
    }

    private String buildSoapRequest(String path) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:pub=\"http://xmlns.oracle.com/oxp/service/PublicReportService\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<pub:runReport>" +
                "<pub:reportRequest>" +
                "<pub:attributeFormat>xml</pub:attributeFormat>" +
                "<pub:reportAbsolutePath>" + path + "</pub:reportAbsolutePath>" +
                "<pub:sizeOfDataChunkDownload>-1</pub:sizeOfDataChunkDownload>" +
                "</pub:reportRequest>" +
                "<pub:userID>" + username + "</pub:userID>" +
                "<pub:password>" + password + "</pub:password>" +
                "</pub:runReport>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private byte[] extractReportBytes(String soapResponse) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(soapResponse)));
        NodeList nodes = doc.getElementsByTagName("reportBytes");
        if (nodes.getLength() == 0) {
            throw new RuntimeException("reportBytes element not found in SOAP response");
        }
        String base64Data = nodes.item(0).getTextContent().trim();
        return Base64.getDecoder().decode(base64Data);
    }

    private List<PersonScheduleDto> parseReportXml(byte[] xmlBytes) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlBytes));

        NodeList rows = doc.getElementsByTagName("G_WORKSCHEDULE_ASSIGNMENT");
        List<PersonScheduleDto> result = new ArrayList<>();

        for (int i = 0; i < rows.getLength(); i++) {
            Element row = (Element) rows.item(i);
            PersonScheduleDto dto = new PersonScheduleDto();
            dto.setScheduleAssignmentId(parseNum(getText(row, "SCHEDULE_ASSIGNMENT_ID")));
            dto.setPersonId(parseNum(getText(row, "PERSON_ID")));
            dto.setPersonNumber(getText(row, "PERSON_NUMBER"));
            dto.setAssignmentNumber(getText(row, "ASSIGNMENT_NUMBER"));
            dto.setScheduleName(getText(row, "SCHEDULE_NAME"));
            dto.setEffectiveFromDate(parseDate(getText(row, "EFFECTIVE_FROM_DATE")));
            dto.setEffectiveToDate(parseDate(getText(row, "EFFECTIVE_TO_DATE")));
            dto.setStartDate(parseDate(getText(row, "START_DATE")));
            dto.setEndDate(parseDate(getText(row, "END_DATE")));
            dto.setPrimary(getText(row, "PRIMARY"));
            result.add(dto);
        }
        return result;
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        String text = nodes.item(0).getTextContent();
        return (text == null || text.isBlank()) ? null : text.trim();
    }

    private LocalDate parseDate(String value) {
        if (value == null) return null;
        try {
            String datePart = value.contains("T") ? value.substring(0, 10) : value.trim();
            return LocalDate.parse(datePart, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private Date toSqlDate(LocalDate localDate) {
        return localDate != null ? Date.valueOf(localDate) : null;
    }

    private LocalDate toLocalDate(Date sqlDate) {
        return sqlDate != null ? sqlDate.toLocalDate() : null;
    }

    private Long parseNum(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return (long) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return null;
    }
}
