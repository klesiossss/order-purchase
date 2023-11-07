package br.com.transaction.util;

import br.com.transaction.model.RetrieveRequest;
import br.com.transaction.model.TransactionRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
public class UtilValidator {
    @Autowired
    private final Validator validator;

    public void validate(TransactionRequest transaction){
        Set<ConstraintViolation<TransactionRequest>> violation = validator.validate(new TransactionRequest( transaction.getDate(), transaction.getAmount(),transaction.getDescription()));
        if(!violation.isEmpty()){
            log.error("..: _Error in validation of transaction");
            throw new ConstraintViolationException(violation);
        }
        validMoney(transaction);
    }


    public void validateRetrieve(RetrieveRequest retrieveRequest){
        Set<ConstraintViolation<RetrieveRequest>> violation = validator.validate(new RetrieveRequest( retrieveRequest.getIdentifier(), retrieveRequest.getCountryCurrency()));
        if(!violation.isEmpty()){
            log.error("..: _Error in validation of retrieve");
            throw new ConstraintViolationException(violation);
        }
    }

    public void validMoney(TransactionRequest transaction) {
        transaction.setAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_EVEN));
    }




}
