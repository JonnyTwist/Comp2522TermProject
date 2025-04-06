package ca.bcit.comp2522.termProject.myGame;

import ca.bcit.comp2522.termProject.Playable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javafx.scene.image.ImageView;

//todo try to implement clonable on a piece
public final class TablutSpinoff extends Application implements Playable
{
    private enum Movement {
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN
    }

    //todo dont trust params always validate data

    static Player currentMove = Player.DEFENDER;

    static final         int BTN_SIZE_PX             = 75;
    private static final int DEFAULT_SCENE_WIDTH_PX  = 675;
    private static final int DEFAULT_SCENE_HEIGHT_PX = 750;
    private static final int BOARD_SIZE              = 9;
    private static final int OFFSET_ONE              = 1;
    private static final int OFFSET_TWO              = 2;
    private static final int HALF                    = 2;

    private static final int KNIGHT_SELECT_BOUND = 3;
    private static final int ROOK_SELECT_BOUND   = 6;
    private static final int BISHOP_SELECT_BOUND = 9;
    private static final int MOVE_SELECT_BOUND   = 10;

    private static final int UP          = -1;
    private static final int DOWN        = 1;
    private static final int RIGHT       = 1;
    private static final int LEFT        = -1;
    private static final int NO_MOVEMENT = 0;

    private static final int LEFT_CENTER_COL;
    private static final int CENTER_COL;
    private static final int RIGHT_CENTER_COL;
    private static final int TOP_CENTER_ROW;
    private static final int CENTER_ROW;
    private static final int BOTTOM_CENTER_ROW;
    private static final int FIRST_COL  = 0;
    private static final int FIRST_ROW  = 0;
    private static final int SECOND_COL = 1;
    private static final int LAST_COL;
    private static final int LAST_ROW;
    private static final int SECOND_LAST_COL;


    private static final String VAL_MOVE    = "validMove";
    private static final String SELECTED    = "selectedPiece";
    private static final String LIGHT       = "lightTile";
    private static final String DARK        = "darkTile";
    private static final String RESTRICTED  = "noAccessTile";
    private static final String WIN_TILE    = "winTile";
    private static final String THIS_MOVE   = "This Move: ";
    private static final String NEXT_MOVE   = "Next Move: ";
    private static final String ATK_WIN_STR = "Attacker Wins: ";
    private static final String DFD_WIN_STR = "Defender Wins: ";

    private static final int            SINGLE_THREAD = 1;
    private static final Button[][]     BOARD;
    private static final Piece[][]      PIECES;
    private static final Label          THIS_MOVE_LABEL;
    private static final Label          NEXT_MOVE_LABEL;
    private static final List<Position> WIN_POSITIONS;

    private static final int FIRST_ELEMENT  = 0;
    private static final int SECOND_ELEMENT = 1;

    private static final int KNIGHT_MOVE_LONG  = 2;
    private static final int KNIGHT_MOVE_SHORT = 1;

    private static boolean  nextTileLight = true;
    private static Position lastClickedPos;
    private static Movement thisMove;
    private static Movement nextMove;
    private static int      attackerWins;
    private static int      defenderWins;

    static
    {
        BOARD  = new Button[BOARD_SIZE][BOARD_SIZE];
        PIECES = new Piece[BOARD_SIZE][BOARD_SIZE];

        //decided to make the center col/row calculate dynamically just incase
        CENTER_COL = (BOARD_SIZE - OFFSET_ONE) / HALF;
        CENTER_ROW = (BOARD_SIZE - OFFSET_ONE) / HALF;

        LEFT_CENTER_COL = CENTER_COL - OFFSET_ONE;
        RIGHT_CENTER_COL = CENTER_COL + OFFSET_ONE;
        TOP_CENTER_ROW = CENTER_ROW - OFFSET_ONE;
        BOTTOM_CENTER_ROW = CENTER_ROW + OFFSET_ONE;

        LAST_COL = BOARD_SIZE - OFFSET_ONE;
        LAST_ROW = BOARD_SIZE - OFFSET_ONE;

        SECOND_LAST_COL = BOARD_SIZE - OFFSET_TWO;

        THIS_MOVE_LABEL = new Label();
        NEXT_MOVE_LABEL = new Label();
        WIN_POSITIONS   = chooseWinPos();
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

        currentMove = Player.DEFENDER;
        thisMove = selectMovement();
        nextMove = selectMovement();

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
     * Creates an Array of Positions which will be considered winning positions.
     * (The positions that the defender must move their king)
     * @return an Array of Positions.
     */
    private static List<Position> chooseWinPos()
    {
        final List<Position> winPos;
        winPos = new ArrayList<>();

        // Top row
        winPos.add(new Position(FIRST_ROW, FIRST_COL + OFFSET_ONE));
        winPos.add(new Position(FIRST_ROW, FIRST_COL + OFFSET_TWO));
        winPos.add(new Position(FIRST_ROW, LAST_COL - OFFSET_ONE));
        winPos.add(new Position(FIRST_ROW, LAST_COL - OFFSET_TWO));

        // Bottom row
        winPos.add(new Position(LAST_ROW, FIRST_COL + OFFSET_ONE));
        winPos.add(new Position(LAST_ROW, FIRST_COL + OFFSET_TWO));
        winPos.add(new Position(LAST_ROW, LAST_COL - OFFSET_ONE));
        winPos.add(new Position(LAST_ROW, LAST_COL - OFFSET_TWO));

        // Left column
        winPos.add(new Position(FIRST_ROW + OFFSET_ONE, FIRST_COL));
        winPos.add(new Position(FIRST_ROW + OFFSET_TWO, FIRST_COL));
        winPos.add(new Position(LAST_ROW - OFFSET_ONE, FIRST_COL));
        winPos.add(new Position(LAST_ROW - OFFSET_TWO, FIRST_COL));

        // Right column
        winPos.add(new Position(FIRST_ROW + OFFSET_ONE, LAST_COL));
        winPos.add(new Position(FIRST_ROW + OFFSET_TWO, LAST_COL));
        winPos.add(new Position(LAST_ROW - OFFSET_ONE, LAST_COL));
        winPos.add(new Position(LAST_ROW - OFFSET_TWO, LAST_COL));

        return winPos;
    }

    /*
     * Selects what kind of movement will be coming up.
     * @return the randomly selected movement.
     */
    private static Movement selectMovement()
    {
        final Random rand;
        final int moveSelect;


        rand = new Random();
        moveSelect = rand.nextInt(MOVE_SELECT_BOUND);

        if (moveSelect < KNIGHT_SELECT_BOUND)
        {
            return Movement.KNIGHT;
        }
        else if (moveSelect < ROOK_SELECT_BOUND)
        {
            return Movement.ROOK;
        }
        else if (moveSelect < BISHOP_SELECT_BOUND)
        {
            return Movement.BISHOP;
        }
        else
        {
            return Movement.QUEEN;
        }
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
                          DEFAULT_SCENE_WIDTH_PX,
                          DEFAULT_SCENE_HEIGHT_PX);

        THIS_MOVE_LABEL.setText(THIS_MOVE + thisMove.toString());
        NEXT_MOVE_LABEL.setText(NEXT_MOVE + nextMove.toString());

        vbox.getChildren().add(THIS_MOVE_LABEL);
        vbox.getChildren().add(NEXT_MOVE_LABEL);

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

                // Initialize the piece (null if there is no piece)
                PIECES[row][col] = initializePiece(row, col);

                // Update the button based on the piece at this position
                updateButtonImage(btn, PIECES[row][col]);

                btn.setMinSize(BTN_SIZE_PX, BTN_SIZE_PX);
                btn.setMaxSize(BTN_SIZE_PX, BTN_SIZE_PX);
                btn.setOnAction(event -> btnClicked(btn, position));
                giveInitialColor(btn, row, col);

                BOARD[row][col] = btn;

                hbox.getChildren().add(btn);
            }
            vbox.getChildren().add(hbox);
        }

        addWinPositions();

        return scene;
    }

    /*
     * Gives the tiles on the board classes which can be used
     * to tell if the tile is special as well as color it.
     * @param btn the btn to be colored.
     * @param row the row index.
     * @param col the column index.
     */
    private static void giveInitialColor(final Button btn,
                                         final int row,
                                         final int col)
    {
        validateNonNull(btn);
        Position.validateRow(row);
        Position.validateCol(col);

        nextTileLight = !nextTileLight;

        //places the middle tile which no piece can step on
        if (row == CENTER_ROW && col == CENTER_COL)
        {
            btn.getStyleClass().add(RESTRICTED);
            return;
        }

        //creates an alternating checked pattern
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
     * Adds the winning positions onto the board.
     */
    private static void addWinPositions()
    {
        WIN_POSITIONS.stream()
                .filter(p->p.row >= FIRST_ROW && p.row <= LAST_ROW)
                .filter(p->p.col >= FIRST_COL && p.col <= LAST_COL)
                .forEach(p-> BOARD[p.row][p.col].getStyleClass().add(WIN_TILE));
    }

    /*
     * Places pieces based on a specified position.
     * Only for starting / restarting the game.
     * @param row the y coordinate.
     * @param col the y coordinate.
     * @return the piece that belongs in the specified position.
     */
    private static Piece initializePiece(final int row,
                                         final int col)
    {
        Position.validateRow(row);
        Position.validateCol(col);

        //place the king in the middle
        if (row == CENTER_ROW && col == CENTER_COL)
        {
            return King.getInstance();
        }

        //place attackers in T-shapes on the right and left sides
        // and lines on the top and bottom
        //todo make this more readable
        if ((row == FIRST_ROW && (col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL)) ||   // Top line

            (row == LAST_ROW && (col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL)) ||   // Bottom line

            (col == FIRST_COL && (row >= TOP_CENTER_ROW && row <= BOTTOM_CENTER_ROW)) ||   // Left T
            (col == SECOND_COL && row == CENTER_ROW) ||

            (col == LAST_COL && (row >= TOP_CENTER_ROW && row <= BOTTOM_CENTER_ROW)) ||   // Right T
            (col == SECOND_LAST_COL && row == CENTER_ROW))
        {

            return new Pawn(Player.ATTACKER);
        }

        //place a cross of defenders and 4 extra diagonal of king
        if (((row >= CENTER_ROW - OFFSET_TWO && row <= CENTER_ROW + OFFSET_TWO && col == CENTER_COL) ||
            (col >= CENTER_COL - OFFSET_TWO && col <= CENTER_COL + OFFSET_TWO && row == CENTER_ROW)) ||
            (row == BOTTOM_CENTER_ROW && col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL) ||
            (row == CENTER_ROW - OFFSET_ONE  && col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL))
        {
            return new Pawn(Player.DEFENDER);
        }

        return null;  // Empty space
    }

    /*
     * Updates a button's displayed image. Used when a piece moves to this
     * tile (button) and when setting up the board.
     * @param btn the button to change the image of.
     * @param piece the piece to be placed in this position
     */
    private static void updateButtonImage(final Button btn,
                                          final Piece piece)
    {
        validateNonNull(btn);
        validateNonNull(piece);

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
        validateNonNull(btn);

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
        validateNonNull(btn);
        validatePosition(pos);

        if (btn.getStyleClass().contains(VAL_MOVE))
        {
            final Piece pieceToMove;
            pieceToMove = PIECES[lastClickedPos.row][lastClickedPos.col];
            makeTheMove(pieceToMove, pos, btn);
        }

        final Piece piece;
        piece = PIECES[pos.row][pos.col];

        clearSelectColors();

        if (piece != null)
        {
            lastClickedPos = pos;
            BOARD[lastClickedPos.row][lastClickedPos.col].getStyleClass().add(SELECTED);

            if (currentMove == piece.getOwner() && piece.isKing())
            {
                moveLikeKing(pos);
            }
            else if (currentMove == piece.getOwner())
            {
                switch (thisMove)
                {
                    case KNIGHT ->
                        moveLikeKnight(pos);
                    case ROOK ->
                        moveLikeRook(pos);
                    case BISHOP ->
                        moveLikeBishop(pos);
                    case QUEEN ->
                        moveLikeQueen(pos);
                    default ->
                        System.out.println("ERROR: reached unreachable code!!");
                }
            }
        }
    }

    /*
     * Highlights the surrounding valid moves for the king.
     * @param pos the position of the king.
     */
    private static void moveLikeKing(final Position pos)
    {
        validatePosition(pos);

        //all 8 possible movement directions for the king
        final int[][] directions;
        directions = new int[][] {
                {DOWN, NO_MOVEMENT},
                {UP, NO_MOVEMENT},
                {NO_MOVEMENT, RIGHT},
                {NO_MOVEMENT, LEFT},
                {DOWN, RIGHT},
                {DOWN, LEFT},
                {UP, RIGHT},
                {UP, LEFT}
        };

        for (final int[] direction : directions) {
            final int newRow;
            final int newCol;

            newRow = pos.row + direction[FIRST_ELEMENT];
            newCol = pos.col + direction[SECOND_ELEMENT];

            if (newRow >= FIRST_ROW && newRow <= LAST_ROW && newCol >= FIRST_COL && newCol <= LAST_COL)
            {
                if ((PIECES[newRow][newCol] == null ||
                     PIECES[newRow][newCol].getOwner() != currentMove) &&
                    !BOARD[newRow][newCol].getStyleClass().contains(RESTRICTED))
                {
                    BOARD[newRow][newCol].getStyleClass().add("validMove");
                }
            }
        }
    }

    /*
     * Shows the valid movements for a Knight.
     * @param pos the current position of the piece.
     */
    private static void moveLikeKnight(final Position pos)
    {
        validatePosition(pos);

        final int[][] knightMoves = getKnightMoves();

        for (final int[] move : knightMoves)
        {
            final int newRow;
            final int newCol;

            newRow = pos.row + move[FIRST_ELEMENT];
            newCol = pos.col + move[SECOND_ELEMENT];

            if (newRow >= FIRST_ROW && newRow <= LAST_ROW &&
                    newCol >= FIRST_COL && newCol <= LAST_COL &&
                    (newCol != CENTER_COL || newRow != CENTER_ROW) &&
                    (PIECES[newRow][newCol] == null || PIECES[newRow][newCol].getOwner() != currentMove))
            {
                BOARD[newRow][newCol].getStyleClass().add("validMove");
            }
        }
    }

    /*
     * Creates an array of the valid moves a knight can make.
     * @return the valid knight moves.
     */
    private static int[][] getKnightMoves()
    {
        final int[][] knightMoves;
        knightMoves = new int[][] {
                {KNIGHT_MOVE_LONG, KNIGHT_MOVE_SHORT},
                {KNIGHT_MOVE_LONG, -KNIGHT_MOVE_SHORT},
                {-KNIGHT_MOVE_LONG, KNIGHT_MOVE_SHORT},
                {-KNIGHT_MOVE_LONG, -KNIGHT_MOVE_SHORT},
                {KNIGHT_MOVE_SHORT, KNIGHT_MOVE_LONG},
                {KNIGHT_MOVE_SHORT, -KNIGHT_MOVE_LONG},
                {-KNIGHT_MOVE_SHORT, KNIGHT_MOVE_LONG},
                {-KNIGHT_MOVE_SHORT, -KNIGHT_MOVE_LONG}
        };
        return knightMoves;
    }

    /*
     * Highlights the valid moves for a pawn moving like a rook.
     * @param pos the position of the pawn.
     */
    private static void moveLikeRook(final Position pos)
    {
        validatePosition(pos);

        checkDirection(pos, DOWN, NO_MOVEMENT);
        checkDirection(pos, UP, NO_MOVEMENT);
        checkDirection(pos, NO_MOVEMENT, RIGHT);
        checkDirection(pos, NO_MOVEMENT, LEFT);
    }

    /*
     * Highlights the valid moves for a pawn moving like a bishop.
     * @param pos the position of the pawn.
     */
    private static void moveLikeBishop(final Position pos)
    {
        validatePosition(pos);

        checkDirection(pos, DOWN, RIGHT);
        checkDirection(pos, DOWN, LEFT);
        checkDirection(pos, UP, RIGHT);
        checkDirection(pos, UP, LEFT);
    }

    /*
     * Highlights the valid moves for a pawn moving like a queen.
     * @param pos the position of the pawn.
     */
    private static void moveLikeQueen(final Position pos)
    {
        validatePosition(pos);

        moveLikeRook(pos);
        moveLikeBishop(pos);
    }

    /*
     * Helper method to check valid moves in a specific direction.
     * @param pos The starting position.
     * @param rowDelta The change in row for each step (1 for down, -1 for up).
     * @param colDelta The change in column for each step (1 for right, -1 for left).
     */
    private static void checkDirection(final Position pos,
                                       final int rowDelta,
                                       final int colDelta)
    {
        validatePosition(pos);

        int row;
        int col;

        row = pos.row + rowDelta;
        col = pos.col + colDelta;

        while (row >= FIRST_ROW && row <= LAST_ROW && col >= FIRST_COL && col <= LAST_COL)
        {
            final boolean isRestricted;
            isRestricted = BOARD[row][col].getStyleClass().contains(RESTRICTED);
            if (PIECES[row][col] != null || isRestricted)
            {
                if (!isRestricted && PIECES[row][col].getOwner() != currentMove)
                {
                    BOARD[row][col].getStyleClass().add("validMove");
                }
                break;
            }

            BOARD[row][col].getStyleClass().add("validMove");

            row += rowDelta;
            col += colDelta;
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
        validateNonNull(piece);
        validateNonNull(btn);
        validatePosition(newPos);

        final Button btnToClear;
        boolean delayedWin;

        btnToClear = BOARD[lastClickedPos.row][lastClickedPos.col];
        delayedWin = false;

        PIECES[lastClickedPos.row][lastClickedPos.col] = null;

        updateButtonImage(btnToClear);

        if (PIECES[newPos.row][newPos.col] != null &&
                PIECES[newPos.row][newPos.col].isKing())
        {
            delayedWin = true;
        }

        PIECES[newPos.row][newPos.col] = piece;
        updateButtonImage(btn, piece);

        updatePlayerTurn();
        clearSelectColors();

        if (piece.isKing() && btn.getStyleClass().contains(WIN_TILE) ||
            !attackerExists())
        {
            defendersWin();
        }
        else if(delayedWin)
        {
            attackersWin();
        }
    }

    private static boolean attackerExists()
    {
        for (final Piece[] row : PIECES)
        {
            for (final Piece piece : row)
            {
                if (piece != null && piece.getOwner() == Player.ATTACKER)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * todo this
     */
    private static void defendersWin()
    {
        readStats();
        //todo read / write to file to update stats.
        System.out.println("Defenders win");
    }

    /*
     * todo this
     */
    private static void attackersWin()
    {
        //todo read / write to file to update stats.
        System.out.println("Attackers win");
    }

    private static void readStats()
    {
        final List<String> stats;
        //todo use method reference for Objects::nonNull
        // stream it

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
            thisMove = nextMove;
            nextMove = selectMovement();
            THIS_MOVE_LABEL.setText(THIS_MOVE + thisMove.toString());
            NEXT_MOVE_LABEL.setText(NEXT_MOVE + nextMove.toString());
        }
    }

    /*
     * Clears the valid move colors as well as the selected piece color.
     */
    private static void clearSelectColors()
    {
        for (final Button[] row : BOARD)
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
     * Validates that objects are not null.
     * @param obj the object to check.
     * @param <T> any object.
     */
    private static <T> void validateNonNull(final T obj)
    {
        if (obj == null)
        {
            throw new IllegalArgumentException("Object cannot be null!");
        }
    }

    /*
     * Validates that a position is not null and is on the board.
     * @param pos the position to validate.
     */
    private static void validatePosition(final Position pos)
    {
        validateNonNull(pos);
        Position.validateCol(pos.col);
        Position.validateRow(pos.row);
    }

    /*
     * Allows the creation of position object to aid in mapping
     * the game board.
     *
     * Note to self or anyone who reads:
     * All positions are stored in y, x form (opposed to x, y).
     * row is y, col is x.
     * Greater y moves down the board and lesser y moves up the board.
     * Greater x moves to the right and lesser x moves to the left.
     */
    private static class Position
    {
        private final int row;
        private final int col;

        /*
         * Constructor for a position object.
         * @param row the row (inverted y)
         * @param col the col (x)
         */
        private Position(final int row,
                         final int col)
        {
            validateRow(row);
            validateCol(col);

            this.row = row;
            this.col = col;
        }

        /*
         * Validates that a row is on the board.
         * @param row the row to validate.
         */
        private static void validateRow(final int row)
        {
            if (row < FIRST_ROW || row > LAST_ROW)
            {
                throw new IllegalArgumentException("Row " + row + " is out of board bounds");
            }
        }

        /*
         * Validates that a Column is on the board.
         * @param col the column to validate.
         */
        private static void validateCol(final int col)
        {
            if (col < FIRST_COL || col > LAST_COL)
            {
                throw new IllegalArgumentException("Column " + col + " is out of board bounds");
            }
        }
    }
}
