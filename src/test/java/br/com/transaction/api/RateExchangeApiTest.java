package br.com.transaction.api;

import br.com.transaction.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@SpringBootTest
class RateExchangeApiTest {
    @Spy
    private RestTemplate restTemplate;
    @InjectMocks
    private RateExchangeApi exchangeRateApi;
    private  String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";

    @Test
    public void GetIsCalled_thenReturnsMockedObject() throws Exception {
        Root root = new Root();
        ExchangeRate exchangeRate = new ExchangeRate().builder()
                .exchangeRate(BigDecimal.ONE)
                .country("Brazil")
                .countryCurrency("Brazil-Real")
                .currency("REAL")
                .effectiveDate(LocalDate.now())
                .recordDate(LocalDate.now())
                .exchangeRate(BigDecimal.ONE)
                .build();
        root.setData(List.of(exchangeRate));
        HttpHeaders header =  new HttpHeaders();
        header.set("ContentType","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<Root> variable = new ResponseEntity<>(root, HttpStatus.OK);
        when(restTemplate.exchange(url
                , HttpMethod.GET
                , entity
                , Root.class)
        ).thenReturn(variable);
        var root1 = exchangeRateApi.getExchange(LocalDate.now(), LocalDate.now().minusMonths(6));
       assertNotNull(root1);
    }


}
