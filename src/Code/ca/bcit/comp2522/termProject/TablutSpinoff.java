package ca.bcit.comp2522.termProject;

// AI IS ALLOWED ON MY OWN GAME
// MUST BE UNIQUE

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

//todo make game 2 player (tablut) viking chess
public final class TablutSpinoff implements Playable
{
    private enum Player {
        DEFENDER,
        ATTACKER
    }

    static Player currentMove = Player.DEFENDER;

    private static final int BTN_SIZE_PX           = 75;
    private static final int DEFAULT_SCENE_SIZE_PX = 675;
    private static final int BOARD_SIZE = 9;
    private static final int SINGLE_THREAD = 1;
    private static final Button[][] board;

    static
    {
        board = new Button[BOARD_SIZE][BOARD_SIZE];
    }

    private final CountDownLatch latch;

    /**
     * Constructor for a TablutSpinoff object.
     */
    public TablutSpinoff()
    {
        this.latch = new CountDownLatch(SINGLE_THREAD);
    }

    /**
     * Calls all required methods to set up the game
     * and start it.
     */
    public void start(final Stage primaryStage)
    {
        final Scene scene;

        Platform.setImplicitExit(false);

//        prepareButtons();

        scene = prepareScene();

        primaryStage.setTitle("My Game");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
            latch.countDown();
        });

        primaryStage.show();

        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    private static Scene prepareScene()
    {
        final VBox vbox;
        final Scene scene;

        vbox = new VBox();
        scene = new Scene(vbox,
                          DEFAULT_SCENE_SIZE_PX,
                          DEFAULT_SCENE_SIZE_PX);

        for(int x = 0; x < BOARD_SIZE; x++)
        {
            final HBox hbox;
            hbox = new HBox();
            for (int y = 0; y < BOARD_SIZE; y++)
            {
                final Button btn;
                final Position position;

                position = new Position(x, y);

                btn = new Button();
                btn.setMinSize(BTN_SIZE_PX, BTN_SIZE_PX);
                btn.setOnAction(event -> btnClicked(btn, position));

                hbox.getChildren().add(btn);
            }
            vbox.getChildren().add(hbox);
        }

        return scene;
    }

    private static void btnClicked(final Button btn,
                                   final Position pos)
    {
        System.out.println(pos.x);
        System.out.println(pos.y);
    }

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

    private static class Position
    {
        private final int x;
        private final int y;
        private Position(final int x,
                         final int y)
        {
            //todo validate?
            this.x = x;
            this.y = y;
        }
    }

//    private static abstract class Piece
//    {
//        private
//    }


}
