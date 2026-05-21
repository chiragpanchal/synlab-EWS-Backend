package com.ewsv3.ews.hcmintegrations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HcmIntegrationConfig {

    @Bean(name = "hcmRestTemplate")
    public RestTemplate hcmRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30_000);
        factory.setReadTimeout(120_000);
        return new RestTemplate(factory);
    }
}
