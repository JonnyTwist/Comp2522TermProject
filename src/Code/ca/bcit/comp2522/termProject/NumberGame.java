package ca.bcit.comp2522.termProject;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

//todo remove after fully testing winning
import java.util.Arrays;

/*
make at least one interface, one abstract class,
and one concrete class.
 */

/**
 * Allows users to play the number game.
 * Users must place 20 numbers in a non-decreasing order on a grid
 * to win this game.
 * @author Jonny Twist
 * @version 1.0
 */
public final class NumberGame
        extends RNGGame
        implements Playable
{

    private static final int GRID_WIDTH              = 5;
    private static final int GRID_HEIGHT             = 4;
    private static final int BTN_SIZE_PX             = 100;
    private static final int DEFAULT_WIDTH_PX        = 600;
    private static final int DEFAULT_HEIGHT_PX       = 600;
    private static final int MIN_NUM                 = 1;
    private static final int MAX_NUM                 = 1001;
    private static final int TOTAL_CHOSEN_NUMS       = 20;
    private static final int STARTING_CHOSEN_SQUARES = 0;
    private static final int STARTING_PREVIOUS       = 0;
    private static final int SINGLE_THREAD           = 1;
    private static final int DEFAULT_PADDING_PX      = 10;
    private static final String DEFAULT_BTN_TEXT     = "[ ]";

    private final int[] chosenNums;
    private final Button[][] buttons;
    private int chosenSquares;
    private final CountDownLatch latch;

    /**
     * Constructor for a number game object.
     */
    public NumberGame()
    {
        super();
        this.chosenNums = new int[TOTAL_CHOSEN_NUMS];
        this.buttons = new Button[GRID_HEIGHT][GRID_WIDTH];
        this.chosenSquares = STARTING_CHOSEN_SQUARES;
        this.latch = new CountDownLatch(SINGLE_THREAD);

    }

    /**
     * Calls all required methods to set up the game
     * and start it.
     * @param primaryStage the stage that we will place our javaFX items.
     */
    public void start(final Stage primaryStage)
    {
        final Scene scene;

        Platform.setImplicitExit(false);

        prepareButtons();

        scene = prepareScene();

        chooseNumbers();

        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
            latch.countDown();
        });

        primaryStage.show();
    }

    /*
     * Chooses the numbers that will be used for the game.
     * Notice: the loop and assignment is not inside the roll method to restrict
     * reassignment of numbers from outside this class.
     */
    private void chooseNumbers()
    {
        for (int i = 0; i < TOTAL_CHOSEN_NUMS; i++)
        {
            chosenNums[i] = roll(MIN_NUM, MAX_NUM);
        }

        //todo remove after fully testing winning

        // Sort the array
        Arrays.sort(chosenNums);
    }

    /**
     * Rolls a random number between a specified min and max.
     * Inherited from RNGGame.
     * @param min the minimum number to be rolled
     * @param max the maximum number to be rolled (not inclusive)
     * @return a random number in between.
     */
    public int roll(final int min, final int max)
    {
        final Random rand;
        int num;

        rand = new Random();

        do
        {
            num = rand.nextInt(min, max);
        } while(containsNum(chosenNums, num));

        return num;
    }

    /*
     * Checks if a number has been already placed in the array
     * to avoid duplicate numbers.
     * @param arr the array to check.
     * @param num the number to check for.
     * @return true if the number exists in the array. Else false.
     */
    private static boolean containsNum(final int[] arr, final int num)
    {
        for(final int item : arr)
        {
            if(item == num)
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Prepares the scene.
     * @return a scene containing all the needed VBox, buttons, and HBox.
     */
    private Scene prepareScene()
    {
        final VBox vbox;
        final Scene scene;

        vbox = new VBox();
        scene = new Scene(vbox,
                          DEFAULT_WIDTH_PX,
                          DEFAULT_HEIGHT_PX);

        for (int i = 0; i < GRID_HEIGHT; i++)
        {
            final HBox hbox;
            hbox = new HBox();
            for (int j = 0; j < GRID_WIDTH; j++)
            {
                HBox.setMargin(buttons[i][j], new Insets(DEFAULT_PADDING_PX,
                                                         DEFAULT_PADDING_PX,
                                                         DEFAULT_PADDING_PX,
                                                         DEFAULT_PADDING_PX));
                hbox.getChildren().add(buttons[i][j]);
            }
            vbox.getChildren().add(hbox);
        }

        return scene;
    }

    /*
     * prepares the buttons by defining their interaction as well as
     * initial text and size. Then it adds them to the buttons array.
     */
    private void prepareButtons()
    {
        for (int i = 0; i < GRID_HEIGHT; i++)
        {
            for (int j = 0; j < GRID_WIDTH; j++)
            {
                final Button btn;
                btn = new Button();
                btn.setMinSize(BTN_SIZE_PX,BTN_SIZE_PX);
                btn.setMaxSize(BTN_SIZE_PX, BTN_SIZE_PX);

                btn.setOnAction(e->updateButton(btn));
                btn.setText(DEFAULT_BTN_TEXT);

                buttons[i][j] = btn;
            }
        }
    }

    /*
     * Updates a button to contain the placed number
     * and deactivates it from further use.
     * @param btn the button to be activated.
     */
    private void updateButton(final Button btn)
    {
        btn.setText(Integer.toString(chosenNums[chosenSquares]));
        btn.setDisable(true);

        checkLose();

        chosenSquares++;

        if (chosenSquares == TOTAL_CHOSEN_NUMS)
        {
            userWins();
        }
    }

    /*
     * Checks if the user has lost.
     */
    private void checkLose()
    {
        int previous = STARTING_PREVIOUS;
        int current;

        for (int i = 0; i < GRID_HEIGHT; i++)
        {
            for (int j = 0; j < GRID_WIDTH; j++)
            {
                if (isInteger(buttons[i][j].getText()))
                {
                    current = Integer.parseInt(buttons[i][j].getText());
                    if (current < previous)
                    {
                        loseGame();
                    }
                    previous = current;
                }
            }
        }
    }

    /*
     * Checks if a String is an integer.
     * @param str the string to check.
     * @return true if it can be parsed as an int. Else false.
     */
    private static boolean isInteger(final String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch (final NumberFormatException ex)
        {
            return false;
        }
    }

    /*
     * todo javadoc and do this
     */
    private void loseGame()
    {
        System.out.println("YOU LOST THE GAME!");

        for (final Button[] btnArr : buttons)
        {
            for (final Button btn : btnArr)
            {
                btn.setDisable(true);
            }
        }
    }

    /*
     * When the user wins this method will create a pop-up to
     * congratulate them and ask if they want to play again or quit.
     */
    private void userWins() {
        final Alert alert;
        final ButtonType playAgainBtn;
        final ButtonType quitBtn;


        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("You Win!");
        alert.setContentText("Game Over! Press 'Play Again' to play again!");

        playAgainBtn = new ButtonType("Play Again");
        quitBtn = new ButtonType("Quit");

        alert.getButtonTypes().setAll(playAgainBtn, quitBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == playAgainBtn) {
                restartGame();
            }
            else
            {
                showFinalScore();
            }
        });
    }

    //todo use this to modularize win and lose
    private void createPopUp()
    {

    }

    //todo comment
    private void restartGame()
    {
        System.out.println("Restarting...");

        this.chosenSquares = STARTING_CHOSEN_SQUARES;

        resetButtons();
        chooseNumbers();
    }

    //todo comment
    private void resetButtons()
    {
        for (final Button[] row : buttons)
        {
            for (final Button btn : row)
            {
                btn.setText(DEFAULT_BTN_TEXT);
                btn.setDisable(false);
            }
        }
    }

    //todo do
    private void showFinalScore()
    {
        System.out.println("I need to show the score here");
    }

    /**
     * Entry point for the NumberGame game.
     */
    public void play()
    {
        try
        {
            Platform.runLater(() -> {
                final Stage newStage;
                newStage = new Stage();
                start(newStage);
            });

            latch.await();
        }
        catch (final Exception e)
        {
            System.out.println("Trouble launching number game: " + e);
        }
    }
}
