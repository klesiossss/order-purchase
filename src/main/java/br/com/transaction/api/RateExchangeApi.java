package br.com.transaction.api;

import br.com.transaction.model.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Component
public class RateExchangeApi {

/*    @Autowired
    private RestTemplate restTemplate = new RestTemplate();*/
@Autowired
@Qualifier("rateExchangeRestTemplate")
private RestTemplate restTemplate;

    public List<Root> getExchange(LocalDate start, LocalDate end){

        String urlBase = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?filter=record_date:gte:"+end+",record_date:lte:"+start+"&sort=-effective_date,country&page[number]=1&page[size]=10000";

        HttpHeaders header =  new HttpHeaders();
        header.set("ContentType","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<Root> exchange = restTemplate.exchange(urlBase, HttpMethod.GET, entity,Root.class);
        return Arrays.asList(exchange.getBody());

    }
}
