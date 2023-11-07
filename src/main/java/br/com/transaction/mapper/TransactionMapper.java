package br.com.transaction.mapper;

import br.com.transaction.model.ExchangeDTO;
import br.com.transaction.model.Transaction;
import br.com.transaction.model.TransactionRequest;
import br.com.transaction.model.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction transactionToModel(TransactionRequest transactionRepresentation);
    TransactionResponse transaction(Transaction transaction);
    ExchangeDTO transactionToExchangeDTO(Transaction transaction);
}
