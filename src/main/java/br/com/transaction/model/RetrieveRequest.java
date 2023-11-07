package br.com.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieveRequest {

    @Positive( message = "14# identifier must be positive")
    @Min(value = 1, message = "15# identifier should be mininum 1")
    @Max(value = 99999999 ,message = "16# identifier should be maximum 99999999")
    Long identifier;
    @Size(min=1, max = 25, message = "13# the currency size should be maximum 25 caracters ")
    String countryCurrency;
}
