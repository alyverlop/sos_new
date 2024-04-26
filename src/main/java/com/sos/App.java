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
import javafx.scene.control.Label;
import java.util.Random;
import javafx.geometry.Insets;



public class App extends Application {
    private SOSGame game;
    private Button[][] buttons;
    private Button startButton;
    private boolean isSimpleGame;
    private ComboBox<String> gameModeComboBox;
    private TextField boardSizeField;
    private int check;
    RadioButton selectedRedRadioButton;
    RadioButton selectedBlueRadioButton;
    RadioButton selectedRedPlayerRadioButton;
    RadioButton selectedBluePlayerRadioButton;
    char chosenCharacter;

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

        Button startButton = new Button("Start Game");
        startButton.setOnAction(event -> {
            boolean gameStarted = startGame(primaryStage);
            if (!gameStarted) {
                check++;
                start(primaryStage);
            }
        });

        ToggleGroup redPlayerToggleGroup = new ToggleGroup();
        ToggleGroup bluePlayerToggleGroup = new ToggleGroup();

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
        initialDialogPane.add(startButton, 1, 4);
        initialDialogPane.add(errorLabel, 1, 5); // Add error label to the layout

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
    
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Button button = new Button("*");
                button.setMinSize(50, 50);
                int[] row ={i};
                int[] col = {j};


                selectedRedRadioButton = (RadioButton) redToggleGroup.getSelectedToggle();
                selectedBlueRadioButton = (RadioButton) blueToggleGroup.getSelectedToggle();
                if (player[0] % 2 == 0) {   
                    chosenCharacter = selectedRedRadioButton.getText().charAt(0);
                }
                else {
                    if (selectedBlueRadioButton == null) {
                        // If no button is selected, set the default button here
                        chosenCharacter = '*';
                    }
                    else{
                        chosenCharacter = selectedBlueRadioButton.getText().charAt(0);
                    }
                }

                if (chosenCharacter == 'C'){
                    makeComputerMove();
                }
                else{
                    button.setOnAction(event -> {
                        if (player[0] % 2 == 0) {
                            
                            chosenCharacter = selectedRedRadioButton.getText().charAt(0);
                        }
                        else {
                            chosenCharacter = selectedBlueRadioButton.getText().charAt(0);
                        }
                        if (game.placeSOrO(row[0], col[0], chosenCharacter)) { 
                            button.setText(String.valueOf(chosenCharacter));
                            if (player[0] % 2 == 0) { //changes color for each player
                                button.setTextFill(Color.RED);
                            } 
                            else {
                                button.setTextFill(Color.BLUE);
                            }
                            errorLabel.setText(""); // Clear error message
    
                            if (game.isGameOver(row[0], col[0], isSimpleGame)){
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
        root.getChildren().addAll(gameBoardAndSelectionBox, errorLabel, createRestartButton(primaryStage));

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
    
    // Method to create the start and restart button
    private Button createRestartButton(Stage primaryStage) {
        // If game hasn't started yet, create a start button
        if (startButton == null) {
            startButton = new Button("Start Game");
            startButton.setOnAction(event -> {
                boolean gameStarted = startGame(primaryStage);
                if (!gameStarted) {
                    check++;
                    start(primaryStage);
                } else {
                    // Change button text to "Restart Game" after game has started
                    startButton.setText("Restart Game");
                }
            });
            return startButton;
        } else {
            // If game has started, create a restart button
            Button restartButton = new Button("Restart Game");
            restartButton.setOnAction(event -> start(primaryStage));
            return restartButton;
        }
    }


    private void makeComputerMove() {
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
        game.placeSOrO(row, col, chosenCharacter);
        buttons[row][col].setText(String.valueOf(chosenCharacter));
        buttons[row][col].setTextFill(Color.RED);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
