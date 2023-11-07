package br.com.transaction.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class StandardError {
    private Integer code;
    private String title;
    private String detail;
}
