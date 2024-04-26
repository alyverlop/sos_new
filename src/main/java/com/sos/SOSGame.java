package com.sos;

import javafx.scene.control.Label;


public class SOSGame {
    private SOSMove[][] board;
    private int size;
    private boolean isGameOver;
    private int redScore;
    private int blueScore;
    private char currentPlayerSymbol;

    public class SOSMove {
        private char currentPlayerSymbol;
        private char moveSymbol;
    
        public SOSMove(char currentPlayerSymbol, char moveSymbol) {
            this.currentPlayerSymbol = currentPlayerSymbol;
            this.moveSymbol = moveSymbol;
        }
    
        public char getCurrentPlayerSymbol() {
            return currentPlayerSymbol;
        }
    
        public char getMoveSymbol() {
            return moveSymbol;
        }

        public void setCurrentPlayerSymbol(char symbol) {
            currentPlayerSymbol = symbol;
        }
    
        public void setMoveSymbol(char symbol) {
            moveSymbol = symbol;
        }
    }
    

    Label errorMessageLabel = new Label("");

    public String getErrorMessage() {
        if (errorMessageLabel.getText() != null) {
            return errorMessageLabel.getText();
        } else {
            return "";
        }
    }

    public SOSGame(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board size must be at least 3x3");
        }
        this.size = size;
        this.board = new SOSMove[size][size];
        this.isGameOver = false;
        this.redScore = 0;
        this.blueScore = 0;
        this.currentPlayerSymbol = 'B'; // Blue player starts
        initializeBoard(size);
    }

    private void initializeBoard(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new SOSMove('*','*'); // Initialize with an empty SOSMove instance
            }
        }
    }
    
    public boolean isBoxEmpty(int row, int col){
        if (row < 0 || row >= size || col < 0 || col >= size || board[row][col].moveSymbol != '*') {
            return false; // Invalid move
        }
        return true;
    }
    

    public boolean placeSOrO(int row, int col, char symbol) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false; // Invalid move
        }
        if (board[row][col].moveSymbol == '*') {
            board[row][col].moveSymbol = symbol;
            board[row][col].currentPlayerSymbol = currentPlayerSymbol;
            currentPlayerSymbol = (currentPlayerSymbol == 'B') ? 'R' : 'B';
            return true; // Valid move
        } 
        else {
            return false; // Position already occupied
        }
    }

    public void checkSOS(int row, int col) {
        boolean foundSOS = false;
        if (board[row][col].moveSymbol == 'S') {
            // Check horizontally for OS
            if ((col >= 2 && board[row][col - 1].moveSymbol == 'O' && board[row][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row][col - 2].moveSymbol == 'S' && board[row][col - 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol) ||
                (col < size - 2 && board[row][col + 1].moveSymbol == 'O' && board[row][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row][col + 2].moveSymbol == 'S' && board[row][col + 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol)) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check vertically for OS
            if (!foundSOS && ((row >= 2 && board[row - 1][col].moveSymbol == 'O' && board[row - 1][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row - 2][col].moveSymbol == 'S' && board[row - 2][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol) ||
                    (row <= size - 3 && board[row + 1][col].moveSymbol == 'O' && board[row + 1][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 2][col].moveSymbol == 'S' && board[row + 2][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol))) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check diagonally (top-left to bottom-right) for OS
            if (!foundSOS && ((row >= 2 && col >= 2 && board[row - 1][col - 1].moveSymbol == 'O' && board[row - 1][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row - 2][col - 2].moveSymbol == 'S' && board[row - 2][col - 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol) ||
                    (row <= size - 3 && col <= size - 3 && board[row + 1][col + 1].moveSymbol == 'O' && board[row + 1][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 2][col + 2].moveSymbol == 'S' && board[row + 2][col + 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol))) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check diagonally (top-right to bottom-left) for OS
            if (!foundSOS && ((row >= 2 && col < size - 2 && board[row - 1][col + 1].moveSymbol == 'O' && board[row - 1][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row - 2][col + 2].moveSymbol == 'S' && board[row - 2][col + 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol) ||
                    (col >= 2 && row < size - 2 && board[row + 1][col - 1].moveSymbol == 'O' && board[row + 1][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 2][col - 2].moveSymbol == 'S' && board[row + 2][col - 2].currentPlayerSymbol == board[row][col].currentPlayerSymbol))) {
                updateScores(row, col);
                foundSOS = true;
            }
        } else if (board[row][col].moveSymbol == 'O') {
            // Check horizontally for OS
            if (col >= 1 && col < size - 1 && board[row][col - 1].moveSymbol == 'S' && board[row][col + 1].moveSymbol == 'S' &&
                    board[row][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check vertically for OS
            if (!foundSOS && row >= 1 && row < size - 1 && board[row - 1][col].moveSymbol == 'S' && board[row + 1][col].moveSymbol == 'S' &&
                    board[row - 1][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 1][col].currentPlayerSymbol == board[row][col].currentPlayerSymbol) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check diagonally (top-left to bottom-right) for OS
            if (!foundSOS && row >= 1 && row < size - 1 && col >= 1 && col < size - 1 && board[row - 1][col - 1].moveSymbol == 'S' && board[row + 1][col + 1].moveSymbol == 'S' &&
                    board[row - 1][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 1][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol) {
                updateScores(row, col);
                foundSOS = true;
            }
            // Check diagonally (top-right to bottom-left) for OS
            if (!foundSOS && row >= 1 && row < size - 1 && col >= 1 && col < size - 1 && board[row - 1][col + 1].moveSymbol == 'S' && board[row + 1][col - 1].moveSymbol == 'S' &&
                    board[row - 1][col + 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol && board[row + 1][col - 1].currentPlayerSymbol == board[row][col].currentPlayerSymbol) {
                updateScores(row, col);
                foundSOS = true;
            }
        }
    }
    

    public boolean isGameOver(int row, int col, boolean isSimpleGame) {
        checkSOS(row, col);
        if (isBoardFull()){
            return true; 
        }
        else {
            if (isSimpleGame) {
                return redScore > 0 || blueScore > 0;
            } 
            else {
                return false;
            }
        }        
    }
    
    private void updateScores(int row, int col) {
        if (board[row][col].currentPlayerSymbol == 'R') {
            redScore++;
            // System.out.println("red");
            // System.out.println(redScore);
        } else {
            blueScore++;
            // System.out.println("blue");
            // System.out.println(blueScore);
        }
    }    

    public boolean isBoardFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].getMoveSymbol() == '*') {
                    return false; // Board is not full, empty cell found
                }
            }
        }
        return true; // Board is full, no empty cells found
    }
    

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getSize() {
        return size;
    }
}
