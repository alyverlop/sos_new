package com.sos;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;



public class App extends Application {
    private SOSGame game;
    private Button[][] buttons;
    private boolean isSimpleGame;
    private boolean recordGame;
    private ComboBox<String> gameModeComboBox;
    private TextField boardSizeField;
    private int check;
    ToggleGroup redPlayerToggleGroup = new ToggleGroup();
    ToggleGroup bluePlayerToggleGroup = new ToggleGroup();
    char chosenCharacter;
    CheckBox recordGameCheckbox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SOS Game");

        Label errorLabel = new Label(""); // Create error label
        errorLabel.setAlignment(Pos.CENTER); // Center the error label horizontally

        if (check > 0){
            errorLabel.setText("Invalid board size. Please enter a valid size.");
        }

        // Create UI elements for initial dialog
        gameModeComboBox = new ComboBox<>();
        gameModeComboBox.getItems().addAll("Simple Game", "General Game");
        gameModeComboBox.setValue("Simple Game");

        boardSizeField = new TextField("3");

        recordGameCheckbox = new CheckBox("Record Game");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(event -> {
            boolean gameStarted = startGame(primaryStage);
            if (!gameStarted) {
                check++;
                start(primaryStage);
            }
        });
        
        // Create radio buttons for character selection
        RadioButton blueHumanRadioButton = new RadioButton("Human");
        RadioButton blueComputerRadioButton = new RadioButton("Computer");
        blueHumanRadioButton.setSelected(true);
        blueHumanRadioButton.setToggleGroup(bluePlayerToggleGroup);
        blueComputerRadioButton.setToggleGroup(bluePlayerToggleGroup);
    
        RadioButton redHumanRadioButton = new RadioButton("Human");
        RadioButton redComputerRadioButton = new RadioButton("Computer");
        redHumanRadioButton.setSelected(true);
        redHumanRadioButton.setToggleGroup(redPlayerToggleGroup);
        redComputerRadioButton.setToggleGroup(redPlayerToggleGroup);

        HBox bluePlayerOptions = new HBox(5); // VBox to stack the character options vertically
        bluePlayerOptions.setAlignment(Pos.CENTER); // Center align the options
        bluePlayerOptions.getChildren().addAll(
                blueHumanRadioButton,
                blueComputerRadioButton
        );

        HBox redPlayerOptions = new HBox(5); // VBox to stack the character options vertically
        redPlayerOptions.setAlignment(Pos.CENTER); // Center align the options
        redPlayerOptions.getChildren().addAll(
                redHumanRadioButton,
                redComputerRadioButton
        );



        // Layout UI elements for initial dialog
        GridPane initialDialogPane = new GridPane();
        initialDialogPane.setAlignment(Pos.CENTER);
        initialDialogPane.setHgap(20);
        initialDialogPane.setVgap(10);
        initialDialogPane.addRow(0, new Label("Game Mode:"), gameModeComboBox);
        initialDialogPane.addRow(1, new Label("Board Size:"), boardSizeField);
        initialDialogPane.addRow(2, new Label("Blue Player:"),bluePlayerOptions);
        initialDialogPane.addRow(3, new Label("Red Player:"),redPlayerOptions);
        initialDialogPane.add(recordGameCheckbox, 1, 4); // Add checkbox to the layout
        initialDialogPane.add(startButton, 1, 6);
        initialDialogPane.add(errorLabel, 1, 7); // Add error label to the layout
    

        Scene initialDialogScene = new Scene(initialDialogPane, 350, 250);
        primaryStage.setScene(initialDialogScene);
        primaryStage.show();
    }

    

    private boolean startGame(Stage primaryStage) {
        primaryStage.close(); // Close initial dialog
    
        if (gameModeComboBox.getValue().equals("Simple Game")) {
            isSimpleGame = true;
        } else {
            isSimpleGame = false;
        }

        if (recordGameCheckbox.isSelected()) {
            recordGame = true;
        } else {
            recordGame = false;
        }

        RadioButton redPlayerRadioButton = (RadioButton) redPlayerToggleGroup.getSelectedToggle();
        char redPlayerMode = redPlayerRadioButton.getText().charAt(0);

        RadioButton bluePlayerRadioButton = (RadioButton) bluePlayerToggleGroup.getSelectedToggle();
        char bluePlayerMode = bluePlayerRadioButton.getText().charAt(0);

        System.out.println(bluePlayerMode + " " + redPlayerMode);
    
        int boardSize;
        boardSize = Integer.parseInt(boardSizeField.getText());
        if (boardSize < 3) {
            return false; // Invalid board size
        }
    
        game = new SOSGame(boardSize);
        buttons = new Button[game.getSize()][game.getSize()];
    
        // Create and layout game board
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
    
        // Create and initialize error label
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
    
        ToggleGroup redToggleGroup = new ToggleGroup();
        ToggleGroup blueToggleGroup = new ToggleGroup();
    
        int[] player = {1}; // Define as an array of size 1
        player[0] = 1;
    
        // Loop to create buttons
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Button button = new Button("*");
                button.setMinSize(50, 50);
                buttons[i][j] = button; // Store the reference to the button
                gridPane.add(button, j, i);
            }
        }

        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Button button = new Button("*");
                button.setMinSize(50, 50);
                int row = i;
                int col = j;

                
                if (redPlayerMode != 'C' && bluePlayerMode == 'C') { // Blue is computer, red is human
                    button.setOnAction(event -> {
                        // Red player's move
                        RadioButton selectedRadioButton = (RadioButton) redToggleGroup.getSelectedToggle();
                        chosenCharacter = selectedRadioButton.getText().charAt(0);
                        
                        if (game.placeSOrO(row, col, chosenCharacter, recordGame)) {
                            button.setText(String.valueOf(chosenCharacter));
                            button.setTextFill(Color.RED);
                            errorLabel.setText(""); // Clear error message
                            if (recordGame) {
                                try {
                                    FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                                    writer.write("Red" + "\n");
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    // Handle IOException if necessary
                                }
                            }
                            
                            // Check if the game is over after red player's move
                            if (game.isGameOver(row, col, isSimpleGame)) {
                                // Determine the winner or if it's a tie
                                if (game.getRedScore() > game.getBlueScore()) {
                                    errorLabel.setTextFill(Color.BLUE);
                                    errorLabel.setText("Blue Player is the Winner!"); // Red winner
                                } else if (game.getBlueScore() > game.getRedScore()) {
                                    errorLabel.setText("Red Player is the Winner!"); // Blue winner
                                } else {
                                    errorLabel.setTextFill(Color.PURPLE);
                                    errorLabel.setText("It's a tie!"); // Tie
                                }
                                return;
                            }
                            
                            // Blue player's turn (computer move)
                            makeComputerMove("Blue");
                            //player[0]++; // Increment the player count
                            
                        } else {
                            errorLabel.setText("Position already occupied. Please try again.");
                        }
                    });
                }

                if(redPlayerMode == 'C' && bluePlayerMode != 'C' ){ // red is computer, blue is human
                    button.setOnAction(event -> {
                        RadioButton selectedRadioButton = (RadioButton) blueToggleGroup.getSelectedToggle();
                        chosenCharacter = selectedRadioButton.getText().charAt(0);

                        if (game.placeSOrO(row, col, chosenCharacter, recordGame)) {
                            button.setText(String.valueOf(chosenCharacter));
                            if (player[0] % 2 == 0) { //changes color for each player
                                button.setTextFill(Color.RED);
                                if (recordGame) {
                                    try {
                                        FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                                        writer.write("Red" + "\n");
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        // Handle IOException if necessary
                                    }
                                }
                            } 
                            else {
                                button.setTextFill(Color.BLUE);
                                if (recordGame) {
                                    try {
                                        FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                                        writer.write("Blue" + "\n");
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        // Handle IOException if necessary
                                    }
                                }
                            }
                            errorLabel.setText(""); // Clear error message
                            if (game.isGameOver(row, col, isSimpleGame)){
                                if (game.getRedScore() > game.getBlueScore()){
                                    errorLabel.setText("Red Player is the Winner!"); // red winner
                                }
                                else if (game.getBlueScore() > game.getRedScore()){
                                    errorLabel.setTextFill(Color.BLUE);
                                    errorLabel.setText("Blue Player is the Winner!"); // blue winner
                                }
                                else {
                                    errorLabel.setTextFill(Color.PURPLE);
                                    errorLabel.setText("It's a tie!"); // tie
                                }
                                return;
                            }
                            player[0]++; // Increment the value inside the array
                            // Automatically make the computer (red player) move after blue player's move
                            if (player[0] % 2 == 0) {
                                makeComputerMove("Red");
                                player[0]++;
                            }
                            
                        } else {
                            errorLabel.setText("Position already occupied. Please try again.");
                        }
                    });
                }

                if(redPlayerMode != 'C' && bluePlayerMode != 'C'){ // both human
                    button.setOnAction(event -> {
                        if (player[0] % 2 == 0) {
                            RadioButton selectedRadioButton = (RadioButton) redToggleGroup.getSelectedToggle();
                            chosenCharacter = selectedRadioButton.getText().charAt(0);
                        } else {
                            RadioButton selectedRadioButton = (RadioButton) blueToggleGroup.getSelectedToggle();
                            chosenCharacter = selectedRadioButton.getText().charAt(0);
                        }
                        if (game.placeSOrO(row, col, chosenCharacter, recordGame)) {
                            button.setText(String.valueOf(chosenCharacter));
                            if (player[0] % 2 == 0) { //changes color for each player
                                button.setTextFill(Color.RED);
                                if (recordGame) {
                                    try {
                                        FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                                        writer.write("Red" + "\n");
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        // Handle IOException if necessary
                                    }
                                }
                            } 
                            else {
                                button.setTextFill(Color.BLUE);
                                if (recordGame) {
                                    try {
                                        FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                                        writer.write("Blue" + "\n");
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        // Handle IOException if necessary
                                    }
                                }
                            }
                            errorLabel.setText(""); // Clear error message
                            if (game.isGameOver(row, col, isSimpleGame)){
                                if (game.getRedScore() > game.getBlueScore()){
                                    errorLabel.setText("Red Player is the Winner!"); // red winner
                                }
                                else if (game.getBlueScore() > game.getRedScore()){
                                    errorLabel.setTextFill(Color.BLUE);
                                    errorLabel.setText("Blue Player is the Winner!"); // blue winner
                                }
                                else {
                                    errorLabel.setTextFill(Color.PURPLE);
                                    errorLabel.setText("It's a tie!"); // tie
                                }
                                return;
                            }
                            player[0]++; // Increment the value inside the array
                            
                        } else {
                            errorLabel.setText("Position already occupied. Please try again.");
                        }
                    });
                }
                
                if (bluePlayerMode == 'C' && redPlayerMode == 'C' ) { // both computer
                    if (game.isGameOver(row, col, isSimpleGame)) {
                        // Determine the winner or if it's a tie
                        if (game.getRedScore() > game.getBlueScore()) {
                            errorLabel.setText("Red Player is the Winner!"); // Red winner
                        } else if (game.getBlueScore() > game.getRedScore()) {
                            errorLabel.setTextFill(Color.BLUE);
                            errorLabel.setText("Blue Player is the Winner!"); // Blue winner
                        } else {
                            errorLabel.setTextFill(Color.PURPLE);
                            errorLabel.setText("It's a tie!"); // Tie
                        }
                        break;
                    }
                    makeComputerMove("Blue");
                    // Check if the game is over after red player's move
                    if (game.isGameOver(row, col, isSimpleGame)) {
                        // Determine the winner or if it's a tie
                        if (game.getRedScore() > game.getBlueScore()) {
                            errorLabel.setText("Red Player is the Winner!"); // Red winner
                        } else if (game.getBlueScore() > game.getRedScore()) {
                            errorLabel.setTextFill(Color.BLUE);
                            errorLabel.setText("Blue Player is the Winner!"); // Blue winner
                        } else {
                            errorLabel.setTextFill(Color.PURPLE);
                            errorLabel.setText("It's a tie!"); // Tie
                        }
                        break;
                    }
                    makeComputerMove("Red");
                    if (game.isGameOver(row, col, isSimpleGame)) {
                        // Determine the winner or if it's a tie
                        if (game.getRedScore() > game.getBlueScore()) {
                            errorLabel.setText("Red Player is the Winner!"); // Red winner
                        } else if (game.getBlueScore() > game.getRedScore()) {
                            errorLabel.setTextFill(Color.BLUE);
                            errorLabel.setText("Blue Player is the Winner!"); // Blue winner
                        } else {
                            errorLabel.setTextFill(Color.PURPLE);
                            errorLabel.setText("It's a tie!"); // Tie
                        }
                        break;
                    }
                } 


                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }

        
    
        // Create radio buttons for character selection
        RadioButton blueSRadioButton = new RadioButton("S");
        RadioButton blueORadioButton = new RadioButton("O");
        blueSRadioButton.setSelected(true); // Default selection
        blueSRadioButton.setToggleGroup(blueToggleGroup);
        blueORadioButton.setToggleGroup(blueToggleGroup);
    
        RadioButton redSRadioButton = new RadioButton("S");
        RadioButton redORadioButton = new RadioButton("O");
        redSRadioButton.setSelected(true); // Default selection
        redSRadioButton.setToggleGroup(redToggleGroup);
        redORadioButton.setToggleGroup(redToggleGroup);

        VBox blueCharacterOptions = new VBox(5); // VBox to stack the character options vertically
        blueCharacterOptions.getChildren().addAll(
                new Label("Blue Player:"),
                blueSRadioButton,
                blueORadioButton
        );

        VBox redCharacterOptions = new VBox(5); // VBox to stack the character options vertically
        redCharacterOptions.getChildren().addAll(
                new Label("Red Player:"),
                redSRadioButton,
                redORadioButton
        );
        
        HBox gameBoardAndSelectionBox = new HBox(50);
        gameBoardAndSelectionBox.setAlignment(Pos.CENTER); // Center horizontally
        gameBoardAndSelectionBox.getChildren().addAll(
                blueCharacterOptions,
                gridPane, // The game board
                redCharacterOptions
        );
    
        // Layout game board, character selection, and error label
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(gameBoardAndSelectionBox, errorLabel, createReplayButton(), createRestartButton(primaryStage));

        int height = 75 * boardSize;
        int width = 125 * boardSize;

        if (100 * boardSize < 400){
            height = 400;
            width = 500;
        }

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();

        return true;
    }
    
     // Method to create the restart button
     private Button createRestartButton(Stage primaryStage) {
        Button restartButton = new Button("Restart Game");
        restartButton.setOnAction(event -> start(primaryStage));
        return restartButton;
    }

    // Method to clear the board
    private void clearBoard() {
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                buttons[i][j].setText("*");
                buttons[i][j].setTextFill(Color.BLACK);
            }
        }
    }


    // Method to replay moves from the text file
    private void replayMovesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("successful_moves.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                
                // Parse the line to get row, column, symbol, and color
                String[] parts = line.split(", ");
                int row = Integer.parseInt(parts[0].substring(5).trim()); // Extract row number
                int col = Integer.parseInt(parts[1].substring(5).trim()); // Extract column number
                char symbol = parts[2].substring(8).trim().charAt(0); // Extract symbol
                String color = parts[3].substring(7).trim(); // Extract color
                
                // Update the corresponding button on the game board
                buttons[row][col].setText(Character.toString(symbol));
                if("Blue".equals(color)){
                    buttons[row][col].setTextFill(Color.BLUE);
                }
                else {
                    buttons[row][col].setTextFill(Color.RED);
                }
                
                
                Thread.sleep(1000); // Adjust the delay time as needed
            }
            reader.close();
        } catch (IOException | InterruptedException | NumberFormatException e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }




    // Method to create the replay button
    private Button createReplayButton() {
        Button replayButton = new Button("Replay");
        replayButton.setOnAction(event -> {
            clearBoard(); // Clear the board
            replayMovesFromFile(); // Replay moves from the text file
        });
        return replayButton;
    }


    private void makeComputerMove(String color) {
        // Create an instance of the Random class
        Random random = new Random();

        // Generate random row and column indices
        int row = random.nextInt(game.getSize());
        int col = random.nextInt(game.getSize());

        // Check if the random position is already occupied
        while (!game.isBoxEmpty(row, col)) {
            row = random.nextInt(game.getSize());
            col = random.nextInt(game.getSize());
        }

        // Generate a random integer between 0 and 1
        int randomNumber = random.nextInt(2);

        // If the random number is 0, output "S", otherwise output "O"
        chosenCharacter = (randomNumber == 0) ? 'S' : 'O';

        // Place the chosen character 
        game.placeSOrO(row, col, chosenCharacter, recordGame);
        
        if ("Red".equals(color)){
            buttons[row][col].setTextFill(Color.RED);
            buttons[row][col].setText(String.valueOf(chosenCharacter));
            if (recordGame) {
                try {
                    FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                    writer.write(color + "\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle IOException if necessary
                }
            }
        }
        else {
            buttons[row][col].setTextFill(Color.BLUE);
            buttons[row][col].setText(String.valueOf(chosenCharacter));
            if (recordGame) {
                try {
                    FileWriter writer = new FileWriter("successful_moves.txt", true); // Append mode
                    writer.write(color + "\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle IOException if necessary
                }
            }
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
