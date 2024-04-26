import com.sos.SOSGame;
import javafx.application.Platform;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class AppTest {
    private SOSGame game;

    @Before
    public void setUp() {
        // Initialize JavaFX toolkit
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            // Initialize the game within the JavaFX thread
            game = new SOSGame(3); // Initialize a 3x3 game board
            latch.countDown();
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlaceSOrO() {
        assertTrue(game.placeSOrO(0, 0, 'S')); // Valid move
        assertFalse(game.placeSOrO(-1, 0, 'O')); // Invalid move (out of bounds)
        assertFalse(game.placeSOrO(0, 0, 'O')); // Invalid move (position already occupied)
    }

    @Test
    public void testIsBoxEmpty() {
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isBoxEmpty(0, 0)); // Space should not be empty after placing a symbol 
        game.placeSOrO(0, 1, 'S');   // (Red move)
        assertFalse(game.isBoxEmpty(0, 1)); // Space should not be empty after placing a symbol 
        game.placeSOrO(0, 2, 'O');   // (blue move)
        assertFalse(game.isBoxEmpty(0, 2)); // Space should not be empty after placing a symbol 
        game.placeSOrO(1, 0, 'O');   // (red move)
        assertFalse(game.isBoxEmpty(1, 0)); // Space should not be empty after placing a symbol 
        game.placeSOrO(1, 1, 'S');   // (blue move)
        assertFalse(game.isBoxEmpty(1, 1)); // Space should not be empty after placing a symbol 
        game.placeSOrO(1, 2, 'S');   // (red move)
        assertFalse(game.isBoxEmpty(1, 2)); // Space should not be empty after placing a symbol 
        game.placeSOrO(2, 0, 'S');   // (blue move)
        assertFalse(game.isBoxEmpty(2, 0)); // Space should not be empty after placing a symbol 
        game.placeSOrO(2, 1, 'O');   // (red move)
        assertFalse(game.isBoxEmpty(2, 1)); // Space should not be empty after placing a symbol 
        game.placeSOrO(2, 2, 'O');   // (blue move)
        assertFalse(game.isBoxEmpty(2,2)); // Space should not be empty after placing a symbol 
    }

    @Test
    public void testIsGameOverSimple() {
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over initially
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 0, 'S');   // (Red move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 1, 'O');   // (blue move)
        assertFalse(game.isGameOver(0, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 1, 'S');   // (red move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 2, 'S');   // (blue move)
        assertTrue(game.isGameOver(0, 2, true)); // Game should be over after completing a row
    } 
    
    @Test
    public void testIsGameOverSimple_Column() {
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over initially
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 1, 'S');   // (Red move)
        assertFalse(game.isGameOver(0, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 0, 'O');   // (blue move)
        assertFalse(game.isGameOver(1, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 1, 'S');   // (red move)
        assertFalse(game.isGameOver(1, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 0, 'S');   // (blue move)
        assertTrue(game.isGameOver(2, 0, true)); // Game should be over after completing a column
    }

    @Test
    public void testIsGameOverSimple_Diagonal() {
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over initially
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 1, 'S');   // (Red move)
        assertFalse(game.isGameOver(0, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 1, 'O');   // (blue move)
        assertFalse(game.isGameOver(1, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 0, 'S');   // (red move)
        assertFalse(game.isGameOver(1, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 2, 'S');   // (blue move)
        assertTrue(game.isGameOver(2, 2, true)); // Game should be over after completing a diagonal
    }

    @Test
    public void testIsGameOverSimple_Tie() {
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over initially
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 1, 'S');   // (Red move)
        assertFalse(game.isGameOver(0, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 2, 'O');   // (blue move)
        assertFalse(game.isGameOver(0, 2, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 0, 'O');   // (red move)
        assertFalse(game.isGameOver(1, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 1, 'S');   // (blue move)
        assertFalse(game.isGameOver(1, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 2, 'S');   // (red move)
        assertFalse(game.isGameOver(1, 2, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(2, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 1, 'O');   // (red move)
        assertFalse(game.isGameOver(2, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 2, 'O');   // (blue move)
        assertTrue(game.isGameOver(2, 2, true)); // Game should be over after filling the board
    }

    @Test
    public void testIsGameOverGeneral_FullBoard() {
        assertFalse(game.isGameOver(0, 0, false)); // Game should not be over initially
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                game.placeSOrO(i, j, 'S'); // Fill the board with 'S' symbols
            }
        }
        assertTrue(game.isGameOver(0, 0, false)); // Game should be over when the board is full
    }

    @Test
    public void testIsGameOverGeneral_PartialBoard() {
        assertFalse(game.isGameOver(0, 0, false)); // Game should not be over initially
        for (int i = 0; i < game.getSize() - 1; i++) {
            for (int j = 0; j < game.getSize(); j++) {
                game.placeSOrO(i, j, 'S'); // Fill all but the last row with 'S' symbols
            }
        }
        assertFalse(game.isGameOver(0, 0, false)); // Game should not be over when the board is partially filled
        for (int j = 0; j < game.getSize(); j++) {
            game.placeSOrO(game.getSize() - 1, j, 'O'); // Fill the last row with 'O' symbols
        }
        assertTrue(game.isGameOver(0, 0, false)); // Game should be over when the board is full
    }

    @Test
    public void testIsGameOverGeneral_OneSOS() {
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over initially
        game.placeSOrO(0, 0, 'S');   // (blue move)
        assertFalse(game.isGameOver(0, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 1, 'S');   // (Red move)
        assertFalse(game.isGameOver(0, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(0, 2, 'O');   // (blue move)
        assertFalse(game.isGameOver(0, 2, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 0, 'O');   // (red move)
        assertFalse(game.isGameOver(1, 0, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 1, 'O');   // (blue move)
        assertFalse(game.isGameOver(1, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(1, 2, 'S');   // (red move)
        assertFalse(game.isGameOver(1, 2, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 2, 'S');   // (blue move)
        assertFalse(game.isGameOver(2, 0, true)); // Game should not be over after blue makes SOS
        game.placeSOrO(2, 1, 'O');   // (red move)
        assertFalse(game.isGameOver(2, 1, true)); // Game should not be over after placing a symbol 
        game.placeSOrO(2, 0, 'O');   // (blue move)
        assertTrue(game.isGameOver(2, 2, true)); // Game should be over after filling the board
    }
    

    @Test
    public void testIsGameOverGeneral_Tie() {
        assertFalse(game.isGameOver(0, 0, false)); // Game should not be over initially
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                char symbol = (i + j) % 2 == 0 ? 'S' : 'O'; // Alternate 'S' and 'O' symbols
                game.placeSOrO(i, j, symbol);
            }
        }
        assertTrue(game.isGameOver(0, 0, false)); // Game should be over when the board is full
    }
}
