package com.calculator;

import java.io.Serializable;

public class CalculatorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double result;
    private boolean success;
    private String errorMessage;

    public CalculatorResponse(double result) {
        this.result = result;
        this.success = true;
        this.errorMessage = null;
    }

    public CalculatorResponse(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }

    // Getters
    public double getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
} 