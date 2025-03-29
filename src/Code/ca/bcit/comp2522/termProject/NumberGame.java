package ca.bcit.comp2522.termProject;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.Random;

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
    }

    //todo finish this
    public int roll(final int min, final int max)
    {
        final Random rand;
        rand = new Random();
        return rand.nextInt(min, max);
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

        checkWin();
    }

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
