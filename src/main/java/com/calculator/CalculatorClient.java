package com.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class CalculatorClient extends Application {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    private TextField number1Field;
    private TextField number2Field;
    private ComboBox<String> operatorComboBox;
    private Label resultLabel;
    private Button calculateButton;
    private static final String BACKGROUND_COLOR = "#2C3E50";
    private static final String ACCENT_COLOR = "#3498DB";
    private static final String SUCCESS_COLOR = "#2ECC71";
    private static final String ERROR_COLOR = "#E74C3C";

    @Override
    public void start(Stage primaryStage) {
        number1Field = createStyledTextField("Enter first number");
        number2Field = createStyledTextField("Enter second number");
        operatorComboBox = createStyledComboBox();
        resultLabel = createStyledLabel("Result will appear here");
        calculateButton = createStyledButton();

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        Label titleLabel = new Label("Distributed Calculator");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label userInfoLabel = new Label("Name: Eyob Lakew\nID: BDU1411233");
        userInfoLabel.setFont(Font.font("System", 14));
        userInfoLabel.setTextFill(Color.WHITE);
        userInfoLabel.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-padding: 10px; -fx-background-radius: 5px;");

        HBox inputBox = new HBox(15);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(number1Field, operatorComboBox, number2Field);

        root.getChildren().addAll(
            titleLabel,
            userInfoLabel,
            inputBox,
            calculateButton,
            resultLabel
        );

        calculateButton.setOnAction(e -> performCalculation());
        number1Field.textProperty().addListener((obs, oldVal, newVal) -> validateInput(number1Field));
        number2Field.textProperty().addListener((obs, oldVal, newVal) -> validateInput(number2Field));

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Distributed Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: white; -fx-text-fill: " + BACKGROUND_COLOR + "; " +
                      "-fx-font-size: 14px; -fx-padding: 8px; -fx-background-radius: 5px; " +
                      "-fx-pref-width: 120px;");
        return field;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("+", "-", "*", "/");
        comboBox.setValue("+");
        comboBox.setStyle("-fx-background-color: white; -fx-text-fill: " + BACKGROUND_COLOR + "; " +
                         "-fx-font-size: 14px; -fx-padding: 8px; -fx-background-radius: 5px; " +
                         "-fx-pref-width: 80px;");
        return comboBox;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-padding: 10px; -fx-background-radius: 5px;");
        return label;
    }

    private Button createStyledButton() {
        Button button = new Button("Calculate");
        button.setStyle("-fx-background-color: " + ACCENT_COLOR + "; " +
                       "-fx-text-fill: white; -fx-font-size: 16px; " +
                       "-fx-padding: 12px 30px; -fx-background-radius: 5px; " +
                       "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        button.setPrefWidth(200);
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #2980B9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: #2980B9;", 
                                                                             "-fx-background-color: " + ACCENT_COLOR + ";")));
        return button;
    }

    private void validateInput(TextField field) {
        String text = field.getText();
        if (!text.isEmpty() && !text.matches("-?\\d*\\.?\\d*")) {
            field.setStyle(field.getStyle() + "-fx-border-color: " + ERROR_COLOR + "; -fx-border-width: 2px;");
        } else {
            field.setStyle(field.getStyle().replace("-fx-border-color: " + ERROR_COLOR + "; -fx-border-width: 2px;", ""));
        }
    }

    private void performCalculation() {
        try {
            if (!validateInputs()) return;

            double number1 = Double.parseDouble(number1Field.getText());
            double number2 = Double.parseDouble(number2Field.getText());
            String operator = operatorComboBox.getValue();

            CalculatorRequest request = new CalculatorRequest(number1, number2, operator);

            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject(request);
                out.flush();

                CalculatorResponse response = (CalculatorResponse) in.readObject();
                displayResult(response);

            } catch (IOException | ClassNotFoundException e) {
                showError("Connection error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            showError("Please enter valid numbers");
        }
    }

    private boolean validateInputs() {
        if (number1Field.getText().isEmpty() || number2Field.getText().isEmpty()) {
            showError("Please fill in all fields");
            return false;
        }
        if (operatorComboBox.getValue() == null) {
            showError("Please select an operator");
            return false;
        }
        return true;
    }

    private void displayResult(CalculatorResponse response) {
        if (response.isSuccess()) {
            resultLabel.setText(String.format("Result: %.2f", response.getResult()));
            resultLabel.setTextFill(Color.web(SUCCESS_COLOR));
            resultLabel.setStyle("-fx-background-color: rgba(46, 204, 113, 0.1); -fx-padding: 10px; -fx-background-radius: 5px;");
        } else {
            showError(response.getErrorMessage());
        }
    }

    private void showError(String message) {
        resultLabel.setText("Error: " + message);
        resultLabel.setTextFill(Color.web(ERROR_COLOR));
        resultLabel.setStyle("-fx-background-color: rgba(231, 76, 60, 0.1); -fx-padding: 10px; -fx-background-radius: 5px;");
    }

    public static void main(String[] args) {
        launch(args);
    }
} 