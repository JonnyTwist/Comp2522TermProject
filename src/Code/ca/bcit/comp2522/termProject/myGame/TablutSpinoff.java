package ca.bcit.comp2522.termProject.myGame;

import ca.bcit.comp2522.termProject.Playable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//todo make game 2 player (tablut) viking chess
public final class TablutSpinoff extends Application implements Playable
{
    private enum Player {
        DEFENDER,
        ATTACKER
    }

    static Player currentMove = Player.DEFENDER;

    private static final int BTN_SIZE_PX           = 75;
    private static final int DEFAULT_SCENE_SIZE_PX = 675;
    private static final int BOARD_SIZE = 9;
    private static final int OFFSET_ONE = 1;
    private static final int OFFSET_TWO = 2;
    private static final int HALF       = 2;

    private static final int CENTER_COL;
    private static final int CENTER_ROW;
    private static final int FIRST_COL = 0;
    private static final int FIRST_ROW = 0;
    private static final int LAST_COL;
    private static final int LAST_ROW;

    private static final String VAL_MOVE   = "validMove";
    private static final String SELECTED   = "selectedPiece";
    private static final String LIGHT      = "lightTile";
    private static final String DARK       = "darkTile";
    private static final String RESTRICTED = "noAccessTile";
    private static final String WIN_TILE   = "winTile";

    private static final int        SINGLE_THREAD = 1;
    private static final Button[][] board;
    private static final Piece[][]  pieces;

    private static boolean nextTileLight = true;

    static
    {
        board      = new Button[BOARD_SIZE][BOARD_SIZE];
        pieces     = new Piece[BOARD_SIZE][BOARD_SIZE];
        //todo change these?
        CENTER_COL = (BOARD_SIZE - OFFSET_ONE) / HALF;
        CENTER_ROW = (BOARD_SIZE - OFFSET_ONE) / HALF;
        LAST_COL = BOARD_SIZE - OFFSET_ONE;
        LAST_ROW = BOARD_SIZE - OFFSET_ONE;
    }

    private final CountDownLatch latch;

    private static Position lastClickedPos;

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

        scene = prepareScene();

        primaryStage.setTitle("My Game");
        primaryStage.setScene(scene);

        try
        {
            scene.getStylesheets()
                    .add(getClass().getResource("/styles/tablutStyles.css")
                                 .toExternalForm());
        }
        catch(final NullPointerException e)
        {
            System.out.println("Error: Could not find stylesheet");
        }

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
            latch.countDown();
        });

        primaryStage.show();

        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    /*
     * Creates the scene.
     * @return a grid of buttons to be used as the game board.
     */
    private static Scene prepareScene()
    {
        final VBox vbox;
        final Scene scene;

        vbox = new VBox();
        scene = new Scene(vbox,
                          DEFAULT_SCENE_SIZE_PX,
                          DEFAULT_SCENE_SIZE_PX);

        for(int row = 0; row < BOARD_SIZE; row++)
        {
            final HBox hbox;
            hbox = new HBox();
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                final Button btn;
                final Position position;

                position = new Position(row, col);
                btn = new Button();

                // Initialize the piece (e.g., set to null for an empty space, or create a new piece for defenders/attackers)
                pieces[row][col] = initializePiece(row, col);  // You should write this method to initialize the pieces

                // Update the button based on the piece at this position
                updateButtonImage(btn, pieces[row][col]);

                btn.setMinSize(BTN_SIZE_PX, BTN_SIZE_PX);
                btn.setMaxSize(BTN_SIZE_PX, BTN_SIZE_PX);
                btn.setOnAction(event -> btnClicked(btn, position));
                giveInitialColor(btn, row, col);

                board[row][col] = btn;

                hbox.getChildren().add(btn);
            }
            vbox.getChildren().add(hbox);
        }

        return scene;
    }

    private static void giveInitialColor(final Button btn,
                                         final int row,
                                         final int col)
    {
        nextTileLight = !nextTileLight;

        if ((row == FIRST_ROW && (col == FIRST_COL + OFFSET_ONE || col == FIRST_COL + OFFSET_TWO)) ||
            (row == FIRST_ROW && (col == LAST_COL - OFFSET_ONE || col == LAST_COL - OFFSET_TWO)) ||
            (row == LAST_ROW && (col == FIRST_COL + OFFSET_ONE || col == FIRST_COL + OFFSET_TWO)) ||
            (row == LAST_ROW && (col == LAST_COL - OFFSET_ONE || col == LAST_COL - OFFSET_TWO)) ||
            (col == FIRST_COL && (row == FIRST_ROW + OFFSET_ONE || row == FIRST_ROW + OFFSET_TWO)) ||
            (col == FIRST_COL && (row == LAST_ROW - OFFSET_ONE || row == LAST_ROW - OFFSET_TWO)) ||
            (col == LAST_COL && (row == FIRST_ROW + OFFSET_ONE || row == FIRST_ROW + OFFSET_TWO)) ||
            (col == LAST_COL && (row == LAST_ROW - OFFSET_ONE || row == LAST_ROW - OFFSET_TWO)))
        {
            btn.getStyleClass().add(WIN_TILE);
            return;
        }

        if (row == CENTER_ROW && col == CENTER_COL)
        {
            btn.getStyleClass().add(RESTRICTED);
            return;
        }

        if (nextTileLight)
        {
            btn.getStyleClass().add(LIGHT);
        }
        else
        {
            btn.getStyleClass().add(DARK);
        }
    }

    /*
     * Places pieces based on a specified position.
     * Only for starting / restarting the game.
     * @param row the y coordinate.
     * @param col the y coordinate.
     * @return the piece that belongs in the specified position.
     */
    private static Piece initializePiece(final int row,
                                         final int col) {

        //todo remove magic nums

        //place the king in the middle
        if (row == CENTER_ROW && col == CENTER_COL) {
            return new Piece(Player.DEFENDER, true);
        }

        //todo make this not this
        // Place attackers in T-shapes at the ends of each cross
        if ((row == FIRST_ROW && (col == 3 || col == CENTER_COL || col == 5)) ||   // Top T
                (row == 1 && col == CENTER_COL) ||

                (row == LAST_ROW && (col == 3 || col == CENTER_COL || col == 5)) ||   // Bottom T
                (row == 7 && col == CENTER_COL) ||

                (col == FIRST_COL && (row == 3 || row == CENTER_ROW || row == 5)) ||   // Left T
                (col == 1 && row == CENTER_ROW) ||

                (col == LAST_COL && (row == 3 || row == CENTER_ROW || row == 5)) ||   // Right T
                (col == 7 && row == CENTER_ROW)) {

            return new Piece(Player.ATTACKER, false);
        }

        if (((row >= CENTER_ROW - OFFSET_TWO && row <= CENTER_ROW + OFFSET_TWO && col == CENTER_COL) ||
            (col >= CENTER_COL - OFFSET_TWO && col <= CENTER_COL + OFFSET_TWO && row == CENTER_ROW)) ||
            (row == CENTER_ROW + OFFSET_ONE && col <= CENTER_COL + OFFSET_ONE && col >= CENTER_COL - OFFSET_ONE) ||
            (row == CENTER_ROW - OFFSET_ONE && col <= CENTER_COL + OFFSET_ONE && col >= CENTER_COL - OFFSET_ONE)) {
            return new Piece(Player.DEFENDER, false);
        }

        return null;  // Empty space
    }

    // Method to update the button based on the piece

    /*
     * Updates a button's displayed image. Used when a piece moves to this
     * tile (button) and when setting up the board.
     * @param btn the button to change the image of.
     * @param piece the piece to be placed in this position
     */
    private static void updateButtonImage(final Button btn,
                                              final Piece piece)
    {
        final ImageView imageView;
        imageView = Piece.getPieceImageView(piece);
        btn.setGraphic(imageView);
    }

    /*
     * Clears a buttons image. Used when a piece moves off a tile.
     * @param btn the button to change the image of.
     */
    private static void updateButtonImage(final Button btn)
    {
        btn.setGraphic(null);
    }

    /*
     * todo comment
     * A button has been clicked.
     * Time to figure out what it's supposed to do.
     * @param btn
     * @param pos
     */
    private static void btnClicked(final Button btn,
                                   final Position pos)
    {
        if (btn.getStyleClass().contains(VAL_MOVE))
        {
            final Piece pieceToMove;
            pieceToMove = pieces[lastClickedPos.row][lastClickedPos.col];
            makeTheMove(pieceToMove, pos, btn);
        }

        final Piece piece;
        piece = pieces[pos.row][pos.col];

        if (piece != null)
        {
            clearSelectColors();
            System.out.println("Piece at " + pos.row + "," + pos.col + ": " + piece);

            //todo set clicked cell to color
            lastClickedPos = pos;
            board[lastClickedPos.row][lastClickedPos.col].getStyleClass().add(SELECTED);

            if (currentMove == piece.getOwner() && piece.isKing)
            {
                moveLikeKing(pos);
            }
            else if (currentMove == piece.getOwner() && !piece.isKing)
            {
                moveLikeKnight(pos);
            }
        }
        else
        {
            clearSelectColors();
            System.out.println("Empty space at " + pos.row + "," + pos.col);
        }
    }

    /*
     * Highlights the surrounding valid moves for the king.
     * @param pos the position of the king.
     */
    private static void moveLikeKing(final Position pos)
    {
        //todo update so that kings can move like chess king and
        // capture pieces
        if (pos.row + OFFSET_ONE <= LAST_ROW &&
                pieces[pos.row + OFFSET_ONE][pos.col] == null)
        {
            board[pos.row + OFFSET_ONE][pos.col].getStyleClass().add("validMove");
        }

        if (pos.row - OFFSET_ONE >= FIRST_ROW &&
                pieces[pos.row - OFFSET_ONE][pos.col] == null)
        {
            board[pos.row - OFFSET_ONE][pos.col].getStyleClass().add("validMove");
        }

        if (pos.col + OFFSET_ONE <= LAST_COL &&
                pieces[pos.row][pos.col + OFFSET_ONE] == null)
        {
            board[pos.row][pos.col + OFFSET_ONE].getStyleClass().add("validMove");
        }

        if (pos.col - OFFSET_ONE >= FIRST_COL &&
                pieces[pos.row][pos.col - OFFSET_ONE] == null)
        {
            board[pos.row][pos.col - OFFSET_ONE].getStyleClass().add("validMove");
        }
    }

    /*
     * Calculates the valid movements if a piece were to move
     * like a knight from chess.
     * @param pos the current position of the piece.
     */
    private static void moveLikeKnight(final Position pos)
    {
        //todo fix this jippity
        final int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : knightMoves)
        {
            int newRow = pos.row + move[0];
            int newCol = pos.col + move[1];

            if (newRow >= FIRST_ROW && newRow <= LAST_ROW &&
                    newCol >= FIRST_COL && newCol <= LAST_COL &&
                    (newCol != CENTER_COL || newRow != CENTER_ROW) &&
                    (pieces[newRow][newCol] == null || pieces[newRow][newCol].getOwner() != currentMove))
            {
                board[newRow][newCol].getStyleClass().add("validMove");
            }
        }
    }

    /*
     * Moves a piece from one tile to another.
     * @param piece the piece to be moved.
     * @param newPos the new position of the piece.
     * @param btn the button that will be updated to show the new
     * position of the piece.
     */
    private static void makeTheMove(final Piece piece,
                                    final Position newPos,
                                    final Button btn)
    {
        final Button btnToClear;
        boolean delayedWin = false;

        btnToClear = board[lastClickedPos.row][lastClickedPos.col];

        pieces[lastClickedPos.row][lastClickedPos.col] = null;

        updateButtonImage(btnToClear);

        if (pieces[newPos.row][newPos.col] != null &&
                pieces[newPos.row][newPos.col].isKing)
        {
            delayedWin = true;
        }

        pieces[newPos.row][newPos.col] = piece;
        updateButtonImage(btn, piece);

        updatePlayerTurn();
        clearSelectColors();

        if (piece.isKing && btn.getStyleClass().contains(WIN_TILE))
        {
            defendersWin();
        }
        else if(delayedWin)
        {
            attackersWin();
        }
    }

    /*
     * todo this
     */
    private static void defendersWin()
    {
        System.out.println("Defenders win");
    }

    /*
     * todo this
     */
    private static void attackersWin()
    {
        System.out.println("Attackers win");
    }

    /*
     * updates which player will make the next move.
     */
    private static void updatePlayerTurn()
    {
        if (currentMove == Player.DEFENDER)
        {
            currentMove = Player.ATTACKER;
        }
        else
        {
            currentMove = Player.DEFENDER;
        }
    }

    /*
     * Clears the valid move colors as well as the selected piece color.
     */
    private static void clearSelectColors()
    {
        for (final Button[] row : board)
        {
            for (final Button btn : row)
            {
                btn.getStyleClass().removeAll(VAL_MOVE, SELECTED);
            }
        }
    }

    /**
     * Entry point of my game. Launches the game and tells Main to wait.
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

    /*
     * Allows the creation of position object to aid in mapping
     * the game board.
     */
    private static class Position
    {
        private final int row;
        private final int col;
        private Position(final int row,
                         final int col)
        {
            //todo validate?
            this.row = row;
            this.col = col;
        }
    }

    /*
     * A static inner class to make pieces for my game.
     */
    private static class Piece {

        private static final int IMAGE_PADDING_PX = 10;
        private static final Image ATTACKER_IMAGE;
        private static final Image DEFENDER_IMAGE;
        private static final Image KING_IMAGE;

        static
        {
            ATTACKER_IMAGE = new Image("images/attacker.png");
            DEFENDER_IMAGE = new Image("images/defender.png");
            KING_IMAGE = new Image("images/king.png");
        }

        private final Player owner;
        private final boolean isKing;

        /**
         * Constructor for a Piece object.
         * @param owner the owner of the piece.
         * @param isKing if the piece is the king or not.
         */
        private Piece(Player owner, boolean isKing) {
            this.owner = owner;
            this.isKing = isKing;
        }

        /**
         * Getter for the owner.
         * @return the owner.
         */
        public final Player getOwner() {
            return owner;
        }

        /**
         * Getter for if the piece is the king.
         * @return if the piece is the king return true. Else false.
         */
        public final boolean isKing() {
            return isKing;
        }

        /**
         * A toString method that I use for debugging.
         * @return the type of the piece.
         */
        @Override
        public String toString() {
            if (isKing) {
                return owner == Player.DEFENDER ? "D_K" : "A_K"; // Defender King, Attacker King (A_K is not real)
            }
            return owner == Player.DEFENDER ? "D" : "A"; // Defender, Attacker
        }

        /*
         * todo comment
         * @param piece
         * @return
         */
        private static ImageView getPieceImageView(final Piece piece) {
            ImageView imageView;

            imageView = null;

            if (piece != null) {
                if (piece.isKing()) {
                    imageView = new ImageView(KING_IMAGE);
                } else if (piece.getOwner() == Player.DEFENDER) {
                    imageView = new ImageView(DEFENDER_IMAGE);
                } else if (piece.getOwner() == Player.ATTACKER) {
                    imageView = new ImageView(ATTACKER_IMAGE);
                }

                if (imageView != null) {
                    imageView.setFitWidth(BTN_SIZE_PX - IMAGE_PADDING_PX);  // Slightly smaller than button
                    imageView.setFitHeight(BTN_SIZE_PX - IMAGE_PADDING_PX);
                }
            }

            return imageView;
        }
    }
}
