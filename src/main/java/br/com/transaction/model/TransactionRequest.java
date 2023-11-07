package br.com.transaction.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class TransactionRequest {
 public TransactionRequest(LocalDate date,BigDecimal amount, String description ){
  this.date = date;
  this.amount = amount;
  this.description = description;
 }
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    @Positive( message = "3# amount must be positive")
    private BigDecimal amount;
    @Size(min=1, max = 50, message = "4# the description should be maximum 50 caracters")
    private String description;


}
