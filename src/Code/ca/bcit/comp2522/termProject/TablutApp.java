package ca.bcit.comp2522.termProject;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;

public class TablutApp extends Application {
    private TablutGame game;
    private TablutBoard boardUI;

    @Override
    public void start(Stage primaryStage) {
        game = new TablutGame();
        boardUI = new TablutBoard(game);
        game.addObserver(game -> boardUI.updateBoard());

        MenuBar menuBar = createMenuBar();
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(boardUI);

        Scene scene = new Scene(root, 600, 650);
        primaryStage.setTitle("Tablut Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        game.play();
    }

    private MenuBar createMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Game");
        saveItem.setOnAction(e -> saveGame());
        MenuItem loadItem = new MenuItem("Load Game");
        loadItem.setOnAction(e -> loadGame());
        fileMenu.getItems().addAll(saveItem, loadItem);

        return new MenuBar(fileMenu);
    }

    private void saveGame() {
        try {
            Path path = Paths.get("tablut_save.dat");
            game.saveGame(path);
        } catch (IOException ex) {
            AlertUtility.showAlert("Error saving game: " + ex.getMessage());
        }
    }

    private void loadGame() {
        try {
            Path path = Paths.get("tablut_save.dat");
            game.loadGame(path);
            boardUI.updateBoard();
        } catch (IOException | ClassNotFoundException ex) {
            AlertUtility.showAlert("Error loading game: " + ex.getMessage());
        }
    }

//    public static void main(String[] args) {
//        // For console testing
//        if (args.length > 0 && args[0].equals("--console")) {
//            ConsoleTablut consoleGame = new ConsoleTablut();
//            consoleGame.startGame();
//        }
//        // Normal JavaFX launch
//        else {
//            launch(args);
//        }
//    }
}