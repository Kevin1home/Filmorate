package com.filmorate.excepsion;

/**
 * Exception thrown when a request contains invalid parameter.
 */
public class ValidationException extends RuntimeException {
    private final String inputValue;

    public ValidationException(final String message, final String inputValue) {
        super(message);
        this.inputValue = inputValue;
    }

    public String getDetailMessage() {
        return getMessage() + " Вы ввели: " + inputValue;
    }

}
