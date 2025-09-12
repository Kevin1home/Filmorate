package com.filmorate.excepsion;

/**
 * Exception thrown when a request contains incorrect parameter.
 */
public class IncorrectParameterException extends RuntimeException {
    String parameter;

    public IncorrectParameterException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
