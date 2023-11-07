package br.com.transaction.exception;

public class TransactionException extends RuntimeException{
    public TransactionException() {
        super("the purchase cannot be converted to the target currency.");
    }

    public TransactionException(String message) {
        super(message);
    }

}
