package com.calculator;

import java.io.Serializable;

public class CalculatorRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double number1;
    private double number2;
    private String operator;

    public CalculatorRequest(double number1, double number2, String operator) {
        this.number1 = number1;
        this.number2 = number2;
        this.operator = operator;
    }

    // Getters and setters
    public double getNumber1() {
        return number1;
    }

    public void setNumber1(double number1) {
        this.number1 = number1;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(double number2) {
        this.number2 = number2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
} 