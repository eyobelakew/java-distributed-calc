package com.calculator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Calculator Server is running on port " + PORT);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                    
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    
                    CalculatorRequest request = (CalculatorRequest) in.readObject();
                    CalculatorResponse response = calculate(request);
                    out.writeObject(response);
                    out.flush();
                    
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static CalculatorResponse calculate(CalculatorRequest request) {
        try {
            double result;
            switch (request.getOperator()) {
                case "+":
                    result = request.getNumber1() + request.getNumber2();
                    break;
                case "-":
                    result = request.getNumber1() - request.getNumber2();
                    break;
                case "*":
                    result = request.getNumber1() * request.getNumber2();
                    break;
                case "/":
                    if (request.getNumber2() == 0) {
                        return new CalculatorResponse("Division by zero is not allowed");
                    }
                    result = request.getNumber1() / request.getNumber2();
                    break;
                default:
                    return new CalculatorResponse("Invalid operator: " + request.getOperator());
            }
            return new CalculatorResponse(result);
        } catch (Exception e) {
            return new CalculatorResponse("Calculation error: " + e.getMessage());
        }
    }
} 