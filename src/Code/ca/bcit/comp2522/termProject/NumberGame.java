package ca.bcit.comp2522.termProject;

import java.util.Random;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

//file:///C:/Users/jsatw/Downloads/PlatformJava.pdf

/*
make at least one interface, one abstract class,
and one concrete class.
 */

// REQUIRES GUI KNOWLEDGE
public final class NumberGame
        extends RNGGame
        implements Playable

{

    private static final int GRID_SIZE = 5;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;

    private static final Button[][] buttons;

    static
    {
        buttons = new Button[GRID_SIZE][GRID_SIZE];
    }

    public void start(final Stage primaryStage)
    {
        final Scene scene;

        prepareButtons();

        scene = prepareScene();

        primaryStage.setTitle("Quiz Time");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private static Scene prepareScene()
    {
        final VBox vbox;
        final Scene scene;

        vbox = new VBox();
        scene = new Scene(vbox, DEFAULT_WIDTH, DEFAULT_HEIGHT);

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

    private static void prepareButtons()
    {
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                final Button btn;
                btn = new Button();
                btn.setMinSize(100,100);
                buttons[i][j] = btn;
            }
        }
    }

    //todo finish this
    public int roll(final int min, final int max)
    {
        return 0;
    }

    public void play()
    {
        launch();
    }
}
