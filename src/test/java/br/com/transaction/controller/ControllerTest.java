package br.com.transaction.controller;

import br.com.transaction.model.ExchangeDTO;
import br.com.transaction.model.TransactionRequest;
import br.com.transaction.model.TransactionResponse;
import br.com.transaction.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private static ObjectMapper mapper;

    private static TransactionRequest request;
    private static TransactionResponse response;
    private static ExchangeDTO exchangeDTO;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        request = TransactionRequest.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.ONE)
                .description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
                .build();


        response = TransactionResponse.builder()
                .id(1L)
                .date(LocalDate.now())
                .amount(BigDecimal.ONE)
                .description("Sed consectetur dui quis est commodo, ut tincidunt sapien fringilla")
                .build();

        exchangeDTO = ExchangeDTO.builder()
                .id(1L)
                .amountConverted(BigDecimal.TEN)
                .exchangeRate(BigDecimal.ONE)
                .description("")
                .amount(BigDecimal.TEN)
                .date(LocalDate.now())
                .build();

    }


    @Test
    void createTransaction() throws Exception{

        when(transactionService.create(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)))
                .andExpect(header().string("Location", "http://localhost/v1/createTransaction/1"))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    void retrieveExchaged() throws Exception{

            when(transactionService.retrieve(1L,"REAL")).thenReturn(exchangeDTO);

            mockMvc.perform(get("/v1/{transactionIdentifier}/{countryCurrency}", 1L, "REAL")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.transactionIdentifier").value(1))
                    .andExpect(jsonPath("$.amountConverted").value(10))
                    .andExpect(jsonPath("$.exchangeRate").value(1))
                    .andExpect(jsonPath("$.amount").value(10))
                    .andExpect(jsonPath("$.description").value(""));



    }
}