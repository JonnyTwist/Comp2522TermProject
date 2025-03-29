package ca.bcit.comp2522.termProject;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import java.util.Random;

//todo remove after fully testing winning
import java.util.Arrays;

/*
make at least one interface, one abstract class,
and one concrete class.
 */

/**
 * todo javadoc
 * A class to
 * @author Jonny Twist
 * @version 1.0
 */
public final class NumberGame
        extends RNGGame
        implements Playable

{

    private static final int GRID_SIZE             = 5;
    private static final int BTN_SIZE_PX           = 100;
    private static final int DEFAULT_WIDTH_PX      = 600;
    private static final int DEFAULT_HEIGHT_PX     = 600;
    private static final int MIN_NUM               = 1;
    private static final int MAX_NUM               = 1001;
    private static final int TOTAL_CHOSEN_NUMS     = 25;
    private static final int STARTING_CHOSEN_SQUARES = 0;
    private static final int STARTING_PREVIOUS = 0;

    private final int[] chosenNums;
    private final Button[][] buttons;
    private int chosenSquares;

    public NumberGame()
    {
        super();
        this.chosenNums = new int[TOTAL_CHOSEN_NUMS];
        this.buttons = new Button[GRID_SIZE][GRID_SIZE];
        this.chosenSquares = STARTING_CHOSEN_SQUARES;
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
            System.out.println("Closing window...");
            primaryStage.hide();
            event.consume();
        });

        primaryStage.show();
    }

    private void chooseNumbers()
    {
        for (int i = 0; i < TOTAL_CHOSEN_NUMS; i++)
        {
            chosenNums[i] = roll(MIN_NUM, MAX_NUM);
        }

        //todo remove after fully testing winning

//        // Sort the array
//        Arrays.sort(chosenNums);
//
//        // Print each number with an index (1 - 25)
//        for (int i = 0; i < chosenNums.length; i++) {
//            System.out.println((i + 1) + ": " + chosenNums[i]);
//        }
    }

    //todo finish this
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

    private Scene prepareScene()
    {
        final VBox vbox;
        final Scene scene;

        vbox = new VBox();
        scene = new Scene(vbox,
                          DEFAULT_WIDTH_PX,
                          DEFAULT_HEIGHT_PX);

        for (int i = 0; i < GRID_SIZE; i++)
        {
            final HBox hbox;
            hbox = new HBox();
            for (int j = 0; j < GRID_SIZE; j++)
            {
                hbox.getChildren().add(buttons[i][j]);
            }
            vbox.getChildren().add(hbox);
        }

        return scene;
    }

    private void prepareButtons()
    {
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                final Button btn;
                btn = new Button();
                btn.setMinSize(BTN_SIZE_PX,BTN_SIZE_PX);
                btn.setMaxSize(BTN_SIZE_PX, BTN_SIZE_PX);

                btn.setOnAction(e->updateButton(btn));

                buttons[i][j] = btn;
            }
        }
    }

    private void updateButton(final Button btn)
    {
        //todo make it change to number and check for fail
        btn.setText(Integer.toString(chosenNums[chosenSquares]));
        btn.setDisable(true);

        checkLose();

        chosenSquares++;

        if (chosenSquares == TOTAL_CHOSEN_NUMS)
        {
            userWins();
        }
    }

    private void checkLose()
    {
        int previous = STARTING_PREVIOUS;
        int current;

        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (!buttons[i][j].getText().isBlank())
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

    private void loseGame()
    {
        System.out.println("YOU LOST THE GAME!");
    }

    private static void userWins()
    {
        System.out.println("Congrats you won!");
    }

    /**
     * Entry point for the NumberGame game.
     */
    public void play()
    {
        Platform.runLater(() -> {
            try
            {
                Stage newStage = new Stage();
                start(newStage);
            }
            catch (final Exception e)
            {
                System.out.println("Trouble launching number game: " + e);
            }
        });
    }
}
