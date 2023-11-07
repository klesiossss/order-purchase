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
public class ExchangeRate {

    @JsonProperty("record_date")
    private LocalDate recordDate;
    @JsonProperty("country")
    private String country;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("country_currency_desc")
    private String countryCurrency;
    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;
    @JsonProperty( "effective_date")
    private LocalDate effectiveDate;

}
