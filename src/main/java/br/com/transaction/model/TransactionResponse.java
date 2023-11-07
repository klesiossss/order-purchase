package br.com.transaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    @JsonProperty("transactionIdentifier")
    private Long id;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("description")
    private String description;
}
