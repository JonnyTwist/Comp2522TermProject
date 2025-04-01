package ca.bcit.comp2522.termProject.myGame;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.stream.*;

public class TablutBoard extends GridPane {
    private static final int           SIZE = 9;
    private final        TablutGame    game;
    private final        Rectangle[][] squares = new Rectangle[SIZE][SIZE];
    private final ImageView[][] pieces = new ImageView[SIZE][SIZE];

    public TablutBoard(TablutGame game) {
        this.game = game;
        initializeBoard();
        setupEventHandlers();
    }

    private void initializeBoard() {
        // Create board squares using streams
        IntStream.range(0, SIZE).forEach(x ->
                                                 IntStream.range(0, SIZE).forEach(y -> {
                                                     Rectangle rect = new Rectangle(60, 60);
                                                     rect.setFill((x + y) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY);
                                                     rect.setStroke(Color.BLACK);
                                                     squares[x][y] = rect;
                                                     add(rect, x, y);

                                                     ImageView pieceView = new ImageView();
                                                     pieceView.setFitWidth(50);
                                                     pieceView.setFitHeight(50);
                                                     pieces[x][y] = pieceView;
                                                     add(pieceView, x, y);
                                                 }));

        updateBoard();
    }

    public void updateBoard() {
        // Use streams to update piece images
        IntStream.range(0, SIZE).forEach(x ->
                                                 IntStream.range(0, SIZE).forEach(y -> {
                                                     TablutGame.Piece piece = game.getPieceAt(x, y);
                                                     if (piece != null) {
                                                         try {
                                                             String imageName = getImageNameForPiece(piece);
                                                             Path   imagePath = Paths.get("src/main/resources/images/" + imageName + ".png");
                                                             Image  image     = new Image(Files.newInputStream(imagePath));
                                                             pieces[x][y].setImage(image);
                                                             pieces[x][y].setVisible(true);
                                                         } catch (IOException e) {
                                                             pieces[x][y].setVisible(false);
                                                         }
                                                     } else {
                                                         pieces[x][y].setVisible(false);
                                                     }
                                                 }));
    }

    private String getImageNameForPiece(TablutGame.Piece piece) {
        if (piece instanceof TablutGame.King) return "king";
        if (piece.getOwner() == Player.DEFENDER) return "defender";
        return "attacker";
    }

    private void setupEventHandlers() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                final int fx = x;
                final int fy = y;
                squares[x][y].setOnMouseClicked(e -> handleSquareClick(fx, fy));
            }
        }
    }

    private void handleSquareClick(int x, int y) {
        // Implement piece selection and movement logic here
        // This would interact with the TablutGame instance
    }
}
