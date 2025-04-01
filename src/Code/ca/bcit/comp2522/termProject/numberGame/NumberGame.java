package ca.bcit.comp2522.termProject.numberGame;


import ca.bcit.comp2522.termProject.Playable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

//todo remove after fully testing winning


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
    private static final int NO_GAMES_PLAYED         = 0;
    private static final int SINGULAR                = 1;
    private static final int DEFAULT_INT             = 0;
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
    private static final int FIRST_ITEM              = 0;
    private static final int SINGLE_THREAD           = 1;
    private static final int DEFAULT_PADDING_PX      = 10;
    private static final String DEFAULT_BTN_TEXT     = "[ ]";
    private static final String PLAY_AGAIN_TEXT      = "Play Again";
    private static final String ACTIVE_BTN           = "active";
    private static final String INACTIVE_BTN         = "inactive";

    private final int[] chosenNums;
    private final Button[][] buttons;
    private int              numChosenSquares;
    private final Label      displayNextNum;
    private final CountDownLatch latch;
    private final int[] placedNums;
    private int gamesPlayed;
    private int gamesLost;
    private int successfulPlacements;
    private Stage primaryStage;

    /**
     * Constructor for a number game object.
     */
    public NumberGame()
    {
        this.chosenNums       = new int[TOTAL_CHOSEN_NUMS];
        this.buttons          = new Button[GRID_HEIGHT][GRID_WIDTH];
        this.numChosenSquares = STARTING_CHOSEN_SQUARES;

        this.displayNextNum   = new Label();
        this.placedNums       = new int[TOTAL_CHOSEN_NUMS];
        this.latch            = new CountDownLatch(SINGLE_THREAD);

        this.gamesPlayed          = 0;
        this.gamesLost            = 0;
        this.successfulPlacements = 0;

        displayNextNum.setId("mainLabel");
    }

    /**
     * Calls all required methods to set up the game
     * and start it.
     */
    public void start(final Stage stage)
    {
        primaryStage = stage;

        final Scene scene;

        Platform.setImplicitExit(false);

        prepareButtons();

        chooseNumbers();

        scene = prepareScene();

        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
            latch.countDown();
        });

        try
        {
            scene.getStylesheets()
                    .add(getClass().getResource("/styles/numberGameStyles.css")
                                 .toExternalForm());
        }
        catch(final NullPointerException e)
        {
            System.out.println("Error: Could not find stylesheet");
        }

        primaryStage.show();

        primaryStage.toFront();
        primaryStage.requestFocus();

        showOpeningAlert(primaryStage);

        gamesPlayed++;
    }

    /*
     * Shows an opening alert to welcome the user to the game.
     * @param primaryStage the primary stage that the user sees.
     */
    private void showOpeningAlert(final Stage primaryStage)
    {
        final Alert alert;
        final ButtonType playBtn;

        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Number Game!");
        alert.setContentText("Welcome to the number game challenge! Press 'Start' to begin.");

        playBtn = new ButtonType("Start");

        alert.getButtonTypes().setAll(playBtn);

        // brings the alert and primary stage to the front when
        // the game is launched.
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);

        alert.showAndWait();
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
        //Arrays.sort(chosenNums);
    }

    /**
     * Rolls a random number between a specified min and max.
     * Inherited from RNGGame.
     * @param min the minimum number to be rolled
     * @param max the maximum number to be rolled (not inclusive)
     * @return a random number in between.
     */
    @Override
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

        displayNextNum.setText("Next number is: " + chosenNums[FIRST_ITEM]);

        vbox.getChildren().add(displayNextNum);

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
                final int index;
                final Button btn;

                index = i * GRID_WIDTH + j;

                btn = new Button();
                btn.setMinSize(BTN_SIZE_PX,BTN_SIZE_PX);
                btn.setMaxSize(BTN_SIZE_PX, BTN_SIZE_PX);

                btn.getStyleClass().add(ACTIVE_BTN);

                btn.setOnAction(e->updateButton(btn, index));
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
    private void updateButton(final Button btn,
                              final int index)
    {
        placedNums[index] = chosenNums[numChosenSquares];
        if (checkValid())
        {
            btn.setText(Integer.toString(chosenNums[numChosenSquares]));
            btn.setDisable(true);
            btn.getStyleClass().remove(ACTIVE_BTN);
            btn.getStyleClass().add(INACTIVE_BTN);

            successfulPlacements++;
            numChosenSquares++;

            if(numChosenSquares == TOTAL_CHOSEN_NUMS)
            {
                createPopUp("You Win!",
                            "Press '" + PLAY_AGAIN_TEXT + "' to play again!");
            }
            else
            {
                checkLose();
                displayNextNum.setText("Next number is: " + chosenNums[numChosenSquares]);
            }
        }
        else
        {
            placedNums[index] = DEFAULT_INT;
        }
    }

    /*
     * Checks if the array of placed ints is still valid
     * when the user clicks on a button (prevents incorrect placement).
     * @return true if it's a valid placement. Else False.
     */
    private boolean checkValid()
    {
        //todo look at changing this so it only compares what was just placed
        // take in an index then
        int previous;
        int current;

        previous = STARTING_PREVIOUS;

        for (int i = 0; i < TOTAL_CHOSEN_NUMS; i++)
        {
            if (placedNums[i] != DEFAULT_INT)
            {
                current = placedNums[i];
                if (current < previous)
                {
                    return false;
                }
                previous = current;
            }
        }
        return true;
    }

    /*
     * Checks if the user has lost.
     */
    private void checkLose()
    {

        final int nextNum;
        boolean placeAble;

        nextNum = chosenNums[numChosenSquares];
        placeAble = false;

        // simulates if the next number were to be placed in each remaining
        // 0 and checks if it would be valid like that (checks one at a time).
        // returns the value to a 0 after simulating it.
        for (int i = 0; i < TOTAL_CHOSEN_NUMS && !placeAble; i++)
        {
            if (placedNums[i] == DEFAULT_INT)
            {
                placedNums[i] = nextNum;
                if (checkValid())
                {
                    placeAble = true;
                }
                placedNums[i] = DEFAULT_INT;
            }
        }

        if (!placeAble)
        {
            gamesLost++;
            final String loseMessage;
            loseMessage = generateLoseMessage(nextNum);
            loseGame(loseMessage);
        }
    }

    /*
     * Generates the message when the user cannot place the next number.
     * @param unplaceableNumber the number that cannot be placed.
     * @return the message as a String.
     */
    private static String generateLoseMessage(final int unplaceableNumber)
    {
        final StringBuilder sb;

        sb = new StringBuilder();

        sb.append("The next number (")
                .append(unplaceableNumber)
                .append(") cannot be placed. ")
                .append(PLAY_AGAIN_TEXT)
                .append("?");

        return sb.toString();

    }

    /*
     * Disables all the buttons and requests an alert pop up.
     * @param loseMessage the message thats passed to the pop up.
     */
    private void loseGame(final String loseMessage)
    {
        for (final Button[] btnArr : buttons)
        {
            for (final Button btn : btnArr)
            {
                btn.setDisable(true);
            }
        }
        createPopUp("You lose!", loseMessage);
    }

    /*
     * Creates a pop-up for when the user wins or loses.
     * @param title the title of the pop-up.
     * @param message the message of the pop-up.
     */
    private void createPopUp(final String title,
                             final String message)
    {
        final Alert alert;
        final ButtonType playAgainBtn;
        final ButtonType quitBtn;


        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setContentText(message);

        playAgainBtn = new ButtonType(PLAY_AGAIN_TEXT);
        quitBtn = new ButtonType("Quit");

        alert.getButtonTypes().setAll(playAgainBtn, quitBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == playAgainBtn) {
                restartGame();
            }
            else
            {
                final String finalScoreMessage;
                finalScoreMessage = createScoreMessage();
                showFinalScore(finalScoreMessage);
            }
        });
    }

    /*
     * todo check about making the pop up appear when the user continues as well.
     * Creates a pop-up alert to show the final Score before the user exits.
     * @param message the pop up message.
     */
    private void showFinalScore(final String message)
    {
        final Alert alert;
        final ButtonType closeBtn;

        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Thank you for playing");
        alert.setContentText(message);

        closeBtn = new ButtonType("Close");

        alert.getButtonTypes().setAll(closeBtn);

        alert.showAndWait().ifPresent(response -> {
            primaryStage.hide();
            latch.countDown();
        });
    }

    /*
     * Prepares the game to be played again.
     */
    private void restartGame()
    {
        this.numChosenSquares = STARTING_CHOSEN_SQUARES;

        for (int i = 0; i < TOTAL_CHOSEN_NUMS; i++)
        {
            placedNums[i] = DEFAULT_INT;
        }

        gamesPlayed++;

        resetButtons();
        chooseNumbers();

        displayNextNum.setText("Next number is: " + chosenNums[FIRST_ITEM]);
    }

    /*
     * Returns the buttons to their starting state without creating new
     * buttons.
     */
    private void resetButtons()
    {
        for (final Button[] row : buttons)
        {
            for (final Button btn : row)
            {
                btn.setText(DEFAULT_BTN_TEXT);
                btn.getStyleClass().remove(INACTIVE_BTN);
                btn.getStyleClass().add(ACTIVE_BTN);
                btn.setDisable(false);
            }
        }
    }

    /*
     * todo modularize / make shorter
     * creates a message to show the user their statistics from
     * playing the number game.
     * @return the message we generated.
     */
    private String createScoreMessage()
    {
        final StringBuilder msg;
        msg = new StringBuilder();

        if (gamesPlayed != NO_GAMES_PLAYED)
        {
            if (gamesLost == gamesPlayed)
            {
                msg.append("You lost ")
                        .append(gamesLost)
                        .append(" out of ")
                        .append(gamesPlayed);

                if (gamesPlayed > SINGULAR)
                {
                    msg.append(" games");
                }
                else
                {
                    msg.append(" game");
                }
            }
            else
            {
                msg.append("You won ")
                        .append(gamesPlayed - gamesLost)
                        .append(" out of ")
                        .append(gamesPlayed);

                if (gamesPlayed > SINGULAR)
                {
                    msg.append(" games");
                }
                else
                {
                    msg.append(" game");
                }

                msg.append(" and you lost ")
                        .append(gamesLost)
                        .append(" out of ")
                        .append(gamesPlayed);

                if (gamesPlayed > SINGULAR)
                {
                    msg.append(" games");
                }
                else
                {
                    msg.append(" game");
                }
            }

            msg.append(", with ")
                    .append(successfulPlacements)
                    .append(" successful placements, an average of ")
                    .append((double) successfulPlacements / gamesPlayed)
                    .append(" per game.");
        }
        else
        {
            msg.append("Something went terribly wrong... no games were played");
        }

        return msg.toString();

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
