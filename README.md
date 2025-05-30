# Distributed Calculator - Class Assignment

## Student Information
- **Name:** Eyob Lakew
- **ID:** BDU1411233

## Project Overview
A distributed calculator application implementing client-server architecture using Java sockets and JavaFX. The application demonstrates network programming concepts with a modern user interface.

## Components
1. **Server**: Java-based calculation server
2. **Client**: JavaFX-based user interface
3. **DTOs**: Request/Response objects for communication

## Features
- Arithmetic operations (+, -, *, /)
- Modern UI with real-time validation
- Error handling
- Network communication
- Clean, object-oriented design

## Requirements
- JDK 17+
- JavaFX SDK 19.0.2.1

## Project Structure
```
distributed-calculator/
├── src/main/java/com/calculator/
│   ├── CalculatorClient.java    # JavaFX client
│   ├── CalculatorServer.java    # Server application
│   ├── CalculatorRequest.java   # Request DTO
│   └── CalculatorResponse.java  # Response DTO
├── pom.xml                      # Maven config
└── run.bat                      # Launcher script
```

## Running the Application
1. Compile:
   ```bash
   javac -d target/classes --module-path "C:\Program Files\Java\javafx-sdk-19.0.2.1\lib" --add-modules javafx.controls,javafx.fxml src/main/java/com/calculator/*.java
   ```
2. Run:
   ```bash
   run.bat
   ```

## Technical Details
- Server runs on port 5000
- TCP socket communication
- Serializable DTOs
- Input validation
- Error handling for:
  - Division by zero
  - Invalid inputs
  - Network issues
