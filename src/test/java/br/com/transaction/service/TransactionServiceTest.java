package br.com.transaction.service;

import br.com.transaction.api.RateExchangeApi;
import br.com.transaction.exception.TransactionException;
import br.com.transaction.mapper.TransactionMapper;
import br.com.transaction.model.*;
import br.com.transaction.repository.TransactionRepository;
import br.com.transaction.util.UtilValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    RateExchangeApi exchangeRateApi;
    @Mock
    TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
    @Mock
    TransactionRepository repository;
    @Mock
    UtilValidator validator;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before("givenMockingIsDoneByMockRestServiceServer_whenGetIsCalled_thenReturnsMockedObject")
    void setup() {
        ReflectionTestUtils.setField(service, "repository", repository);
        ReflectionTestUtils.setField(service, "mapper", mapper);
        ReflectionTestUtils.setField(service, "validator", validator);
    }

    @Test
    void create() {
        var transactionRequest =  TransactionRequest.builder()
                .date(LocalDate.of(2023,05,03))
                .amount(BigDecimal.TEN)
                .description("")
                .build();
        var transactionResponse = TransactionResponse.builder()
                .id(1L)
                .amount(BigDecimal.TEN)
                .description("")
                .date(LocalDate.of(2023,05,03))
                .build();
        Transaction transaction  = new Transaction(1L,LocalDate.of(2023,05,03),BigDecimal.TEN,"");

        when(mapper.transactionToModel(transactionRequest)).thenReturn(transaction);
        when(repository.save(any(Transaction.class))).thenReturn(transaction);
        when(mapper.transaction(transaction)).thenReturn(transactionResponse);

        var created = service.create(transactionRequest);
        assertAll(() -> {
            assertEquals(1L, created.getId());
            assertEquals("", created.getDescription());
            assertEquals(LocalDate.of(2023,05,03), created.getDate());
        });
    }

    @Test
    public void validateTest(){
        var transactionRequest =  TransactionRequest.builder()
                .date(LocalDate.of(2023,04,25))
                .amount(BigDecimal.TEN)
                .description("")
                .build();
        var valid = mock(UtilValidator.class);
        valid.validate(transactionRequest);
        verify(valid,times(1)).validate(transactionRequest);
    }

    @Test
    public void validateRetrieveTest(){
        var retrieveRequest =  RetrieveRequest.builder()
                .countryCurrency("REAL")
                .identifier(1L)
                .build();
        var valid = mock(UtilValidator.class);
        valid.validateRetrieve(retrieveRequest);
        verify(valid,times(1)).validateRetrieve(retrieveRequest);
    }

    @Test
    void retrieve() {
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

        ExchangeDTO exchangeDTO  = new ExchangeDTO(1L,LocalDate.now(),BigDecimal.TEN,"",BigDecimal.ONE,BigDecimal.TEN);
        Transaction transaction = new Transaction(1L,LocalDate.now(),BigDecimal.TEN,"");
        when(mapper.transactionToExchangeDTO(transaction)).thenReturn(exchangeDTO);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));
        when(exchangeRateApi.getExchange(exchangeDTO.getDate(), exchangeDTO.getDate().minusMonths(6))).thenReturn(List.of(root));
        var root1 = service.retrieve(1L,"REAL");
        assertNotNull(root1);
    }

    @Test
    void catchExceptionInCaseDateLessThanTransactionDate(){
        Root root = new Root();
        ExchangeRate exchangeRate = new ExchangeRate().builder()
                .exchangeRate(BigDecimal.ONE)
                .country("Brazil")
                .countryCurrency("Brazil-Real")
                .currency("REAL")
                .effectiveDate(LocalDate.now().minusYears(1))
                .recordDate(LocalDate.now())
                .exchangeRate(BigDecimal.ONE)
                .build();
        root.setData(List.of(exchangeRate));

        ExchangeDTO exchangeDTO  = new ExchangeDTO(1L,LocalDate.now(),BigDecimal.TEN,"",BigDecimal.ONE,BigDecimal.TEN);
        Transaction transaction = new Transaction(1L,LocalDate.now(),BigDecimal.TEN,"");
        when(mapper.transactionToExchangeDTO(transaction)).thenReturn(exchangeDTO);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));
        when(exchangeRateApi.getExchange(exchangeDTO.getDate(), exchangeDTO.getDate().minusMonths(6))).thenReturn(List.of());
        assertThrows(TransactionException.class, () -> service.retrieve(1L,"REAL"));
    }

}