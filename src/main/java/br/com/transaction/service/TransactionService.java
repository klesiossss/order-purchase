package br.com.transaction.service;

import br.com.transaction.api.RateExchangeApi;
import br.com.transaction.exception.ResourceNotFoundException;
import br.com.transaction.exception.TransactionException;
import br.com.transaction.mapper.TransactionMapper;
import br.com.transaction.model.*;
import br.com.transaction.repository.TransactionRepository;
import br.com.transaction.util.UtilValidator;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    public TransactionRepository repository;
    @Autowired
    public UtilValidator validator;
    @Autowired
    public RateExchangeApi exchangeRateApi;


    TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    public TransactionResponse create(TransactionRequest transaction) {
        validator.validate(transaction);
        var transactionDomain = mapper.transactionToModel(transaction);
        var transactionRepresentation = repository.save(transactionDomain);
        return mapper.transaction(transactionRepresentation);
    }

    public ExchangeDTO retrieve(Long identifier, String countryCurrency) {
        validator.validateRetrieve(new RetrieveRequest(identifier,countryCurrency));
        var transaction = repository.findById(identifier).orElseThrow(() -> new ResourceNotFoundException());
        var exchange = mapper.transactionToExchangeDTO(transaction);
        LocalDate start = exchange.getDate(), end = exchange.getDate().minusMonths(6);
        var data = exchangeRateApi.getExchange(start, end);

        return applyingRules(data, exchange, countryCurrency);
    }


    public ExchangeDTO applyingRules(List<Root> data, ExchangeDTO exchange, String countryCurrency) {
        var exchangeRate = data.stream().findFirst().stream().map(Root::getData).findFirst().orElseThrow(()->new TransactionException())
                .stream().filter(c -> c.getCountryCurrency().toLowerCase().contains(countryCurrency)
                || c.getCountry().toLowerCase().equalsIgnoreCase(countryCurrency)
                || c.getCurrency().toLowerCase().equalsIgnoreCase(countryCurrency))
                .findFirst().orElseThrow(() -> new TransactionException());

                var rate = exchangeRate.getExchangeRate();
                var converted = exchange.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
                exchange.setExchangeRate(exchangeRate.getExchangeRate());
                exchange.setAmountConverted(converted);
                return exchange;
    }

}