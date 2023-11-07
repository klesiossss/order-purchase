package br.com.transaction.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {

    @Primary
    @Bean("rateExchangeRestTemplate")
    public RestTemplate rateExchangeRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder(restTemplateBuilder);
    }

    public RestTemplate restTemplateBuilder(RestTemplateBuilder restTemplateBuilder){
        RestTemplate template =
                restTemplateBuilder.setConnectTimeout(Duration.ofMillis(6000))
                        .setReadTimeout(Duration.ofMillis(6000))
                        .build();
        return template;
    }
}
