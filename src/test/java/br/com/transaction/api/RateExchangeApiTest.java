package br.com.transaction.api;

import br.com.transaction.mapper.TransactionMapper;
import br.com.transaction.model.*;
import br.com.transaction.repository.TransactionRepository;
import br.com.transaction.service.TransactionService;
import br.com.transaction.util.UtilValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import net.minidev.json.writer.JsonReader;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@SpringBootTest
class RateExchangeApiTest {


    @Spy
    private RestTemplate restTemplate;

    @InjectMocks
    private RateExchangeApi exchangeRateApi;

    private  String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";


    @BeforeEach
    void setup() {

       //ReflectionTestUtils.setField(exchangeRateApi, "restTemplate", restTemplate);

    }

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
/*        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Root>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Root>>>any())
        ).thenReturn(new ResponseEntity<>(root,HttpStatus.OK));*/

       // when(exchangeRateApi.getExchange(LocalDate.of(2023,05,23), LocalDate.of(2022,12,23))).thenReturn(List.of(root));
        //when(service.retrieve(1L,"REAL")).thenReturn(exchangeDTO);

        //ResponseEntity<Root> variable = new ResponseEntity<>(root, HttpStatus.OK);


        var root1 = exchangeRateApi.getExchange(LocalDate.now(), LocalDate.now().minusMonths(6));
       assertNotNull(root1);
    }


}
