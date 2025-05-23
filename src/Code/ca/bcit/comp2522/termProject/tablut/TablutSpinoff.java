package ca.bcit.comp2522.termproject.tablut;

import ca.bcit.comp2522.termproject.Playable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javafx.scene.image.ImageView;

/**
 * Defines the majority of how a tablut spinoff game is played.
 * Defines how the pawns and kings will move as well as the board.
 * @author Jonny Twist
 * @version 1.0
 */
public final class TablutSpinoff
        extends Application
        implements Playable
{
    static Player currentMove = Player.DEFENDER;

    static final         int BTN_SIZE_PX             = 75;
    private static final int DEFAULT_SCENE_WIDTH_PX  = 675;
    private static final int DEFAULT_SCENE_HEIGHT_PX = 750;
    static final int BOARD_SIZE              = 9;
    private static final int OFFSET_ONE              = 1;
    private static final int OFFSET_TWO              = 2;
    private static final int HALF                    = 2;

    private static final int KNIGHT_SELECT_BOUND = 3;
    private static final int ROOK_SELECT_BOUND   = 6;
    private static final int BISHOP_SELECT_BOUND = 9;
    private static final int MOVE_SELECT_BOUND   = 10;

    static final int UP          = -1;
    static final int DOWN        = 1;
    static final int RIGHT       = 1;
    static final int LEFT        = -1;
    static final int NO_MOVEMENT = 0;

    private static final int LEFT_CENTER_COL;
    static final int CENTER_COL;
    private static final int RIGHT_CENTER_COL;
    private static final int TOP_CENTER_ROW;
    static final int CENTER_ROW;
    private static final int BOTTOM_CENTER_ROW;
    static final int FIRST_COL  = 0;
    static final int FIRST_ROW  = 0;
    private static final int SECOND_COL = 1;
    static final int LAST_COL;
    static final int LAST_ROW;
    private static final int SECOND_LAST_COL;


    private static final String VAL_MOVE    = "validMove";
    private static final String SELECTED    = "selectedPiece";
    private static final String LIGHT       = "lightTile";
    private static final String DARK        = "darkTile";
    static final String RESTRICTED  = "noAccessTile";
    private static final String WIN_TILE    = "winTile";
    private static final String THIS_MOVE   = "This Move: ";
    private static final String NEXT_MOVE   = "Next Move: ";

    private static final String STAT_FILE_NAME = "TablutStats.txt";

    private static final String MOVE_LABEL_STYLE = "moveLabel";
    private static final String MENU_BTN_STYLE = "menuButton";

    private static final int            SINGLE_THREAD = 1;
    private static final CountDownLatch LATCH;

    private static final Button[][]     BOARD;
    private static final Piece[][]      PIECES;
    private static final Label          THIS_MOVE_LABEL;
    private static final Label          NEXT_MOVE_LABEL;
    private static final List<Position> WIN_POSITIONS;

    private static final Scene MENU_SCENE;
    private static final Scene RULES_SCENE;
    private static final Scene GAME_SCENE;
    private static Stage       stage;

    private static final int FIRST_ELEMENT  = 0;
    private static final int SECOND_ELEMENT = 1;

    private static final int KNIGHT_MOVE_LONG  = 2;
    private static final int KNIGHT_MOVE_SHORT = 1;

    private static boolean  nextTileLight = true;
    private static Position lastClickedPos;
    private static Movement thisMove;
    private static Movement nextMove;

    private static boolean singlePlayer = false;

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
        THIS_MOVE_LABEL.getStyleClass().add(MOVE_LABEL_STYLE);
        NEXT_MOVE_LABEL.getStyleClass().add(MOVE_LABEL_STYLE);

        WIN_POSITIONS   = chooseWinPos();

        MENU_SCENE  = createMenuScene();
        RULES_SCENE = createInstructionScene();

        //VBox is just a placeholder for now
        GAME_SCENE = new Scene(new VBox(), DEFAULT_SCENE_WIDTH_PX, DEFAULT_SCENE_HEIGHT_PX);
        prepareGameScene();

        LATCH = new CountDownLatch(SINGLE_THREAD);
    }

    /**
     * Sets up the stage and sets the active scene to the menu.
     * Initializes the main window of the application, sets the scene, and handles the close request event.
     * It also attempts to load and apply the CSS stylesheet to all scenes.
     *
     * @param stage the primary stage for the application
     */
    @Override
    public void start(final Stage stage)
    {
        Platform.setImplicitExit(false);

        stage.setTitle("My Game");
        stage.setScene(MENU_SCENE);

        stage.setOnCloseRequest(event -> {
            stage.hide();
            event.consume();
            LATCH.countDown();
        });

        try
        {
            MENU_SCENE.getStylesheets()
                    .add(getClass().getResource("/styles/tablutStyles.css")
                                 .toExternalForm());
            GAME_SCENE.getStylesheets()
                    .add(getClass().getResource("/styles/tablutStyles.css")
                                 .toExternalForm());
            RULES_SCENE.getStylesheets()
                    .add(getClass().getResource("/styles/tablutStyles.css")
                                 .toExternalForm());
        }
        catch(final NullPointerException e)
        {
            System.out.println("Error: Could not find stylesheet");
        }

        stage.show();
        stage.toFront();
        stage.requestFocus();
    }

    /*
     * prepares some of the games variables for when the game
     * is started or restarted. Ensures the first player to move
     * is the defender and that we have two randomized moves when we start.
     */
    private static void prepareGameStart()
    {
        currentMove = Player.DEFENDER;
        thisMove = selectMovement();
        nextMove = selectMovement();
    }

    /*
     * Creates and returns the menu scene for the game.
     * This scene serves as the starting point where players can choose to start a new game
     * or view the instructions for the game. It features a title and two buttons: one for
     * starting the game and another for accessing the rules.
     *
     * @return the menu scene for the game.
     */
    private static Scene createMenuScene()
    {
        final Button twoPlayBtn;
        final Button singlePlayBtn;
        final Button instructionsButton;
        final Label titleLabel;
        final VBox layout;
        final Scene scene;

        titleLabel = new Label("2 player\nTablut x Chess");
        titleLabel.getStyleClass().add("menuTitle");

        singlePlayBtn = new Button("Single Player");
        twoPlayBtn = new Button("Start Game");
        instructionsButton = new Button("Instructions");

        singlePlayBtn.getStyleClass().add(MENU_BTN_STYLE);
        twoPlayBtn.getStyleClass().add(MENU_BTN_STYLE);
        instructionsButton.getStyleClass().add(MENU_BTN_STYLE);

        layout = new VBox(titleLabel, singlePlayBtn, twoPlayBtn, instructionsButton);
        layout.getStyleClass().add("menuLayout");

        scene = new Scene(layout, DEFAULT_SCENE_WIDTH_PX, DEFAULT_SCENE_HEIGHT_PX);

        twoPlayBtn.setOnAction(e -> {
            singlePlayer = false;
            prepareGameScene();
            stage.setScene(GAME_SCENE);
        });

        singlePlayBtn.setOnAction(e -> {
            singlePlayer = true;
            prepareGameScene();
            stage.setScene(GAME_SCENE);
        });

        instructionsButton.setOnAction(e -> {
            stage.setScene(RULES_SCENE);
        });

        return scene;
    }

    /*
     * Creates and returns the instruction scene for the game.
     * This scene displays the rules and instructions on how to play,
     * providing guidance on the game's mechanics and objectives.
     * Players can read the instructions and go back to the menu screen.
     *
     * @return the instruction scene, which includes the rules of the game.
     */
    private static Scene createInstructionScene()
    {
        final Label instructions;
        final Button backButton;
        final VBox layout;

        instructions = new Label("Instructions:\n\t" +
                                         "- Every player rotation a new chess move is selected at random\n\t" +
                                         "- Use the foresight of what the next move will be to your advantage\n\t" +
                                         "- White's goal is to get their king to one of the green tiles\n\t" +
                                         "- The king moves like a king from chess at all times\n\t" +
                                         "- Black's goal is to capture the king\n\t" +
                                         "- The king cannot be captured on the center tile\n\t" +
                                         "- Once the king leaves the center tile it cannot return");
        instructions.setId("instructionsText");

        backButton = new Button("Back");
        backButton.setId("backButton");

        layout = new VBox(instructions, backButton);
        layout.setId("instructionLayout");

        backButton.setOnAction(e -> stage.setScene(MENU_SCENE));

        return new Scene(layout, DEFAULT_SCENE_WIDTH_PX, DEFAULT_SCENE_HEIGHT_PX);
    }

    /*
     * Creates an Array of Positions that represent the winning positions for the defender's king.
     * These are the positions the defender needs to move their king to in order to win.
     *
     * @return a List of Position objects representing the winning positions.
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
     * Selects the type of movement that will occur next in the game.
     * This method generates a random number to determine whether the next move
     * will be a Knight, Rook, Bishop, or Queen.
     *
     * @return the randomly selected movement type (Knight, Rook, Bishop, or Queen).
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
     * Prepares the game scene by setting up the game board with buttons for each position.
     * It initializes the game state, updates the UI elements to reflect the current game state,
     * and sets up the event handlers for each button.
     *
     * This method also creates a VBox containing labels and HBox containers for rows of buttons,
     * where each button represents a position on the game board.
     *
     * The game state is displayed by updating labels that show the current and next moves.
     * Buttons are initialized with pieces, and each button is given an event handler to respond
     * to clicks during the game.
     *
     * @return a grid of buttons to be used as a board with labels above to show
     * the game state.
     */
    private static void prepareGameScene()
    {
        final VBox vbox;
        prepareGameStart();

        vbox = new VBox();

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

        GAME_SCENE.setRoot(vbox);
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
     * @param col the x coordinate.
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
        if (
            // Top line
            (row == FIRST_ROW && (col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL)) ||

            // Bottom line
            (row == LAST_ROW && (col >= LEFT_CENTER_COL && col <= RIGHT_CENTER_COL)) ||

            // Left T
            (col == FIRST_COL && (row >= TOP_CENTER_ROW && row <= BOTTOM_CENTER_ROW)) ||
            (col == SECOND_COL && row == CENTER_ROW) ||

            // Right T
            (col == LAST_COL && (row >= TOP_CENTER_ROW && row <= BOTTOM_CENTER_ROW)) ||
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

        // Empty space
        return null;
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

        updateButtonImage(btn);

        final ImageView imageView;
        imageView = Piece.getPieceImageView(piece);

        if (piece != null &&
            imageView == null)
        {
            btn.setText(piece.toString());
        }
        else
        {
            btn.setText("");
            btn.setGraphic(imageView);
        }
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
     * Handles the logic for when a board button is clicked.
     * Determines whether the click is a move or a piece selection,
     * validates the action, and initiates appropriate piece movement.
     *
     * @param btn the button that was clicked
     * @param pos the position on the board corresponding to the button
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
     * Highlights all valid one-square moves for the king.
     * The king can move in any of the 8 directions (vertically, horizontally and diagonal),
     * as long as the destination is on the board, not restricted,
     * and either empty or occupied by an opponent.
     *
     * @param pos the current position of the king
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
     * Highlights all valid knight-like moves from the given position.
     * Knight moves follow standard chess rules: 2 squares in one direction and 1 in the other,
     * forming an L-shape. Movement is blocked if the destination is off the board,
     * the center square, or occupied by the current player's own piece.
     *
     * @param pos the current position of the piece
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
    static int[][] getKnightMoves()
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
     * Highlights all valid move positions in a straight line from a given position,
     * following the specified row and column direction.
     * Stops when it hits the edge of the board, a restricted tile, or an occupied space.
     * If the occupied space belongs to the opponent and is not restricted,
     * it is marked as a valid capture.
     *
     * @param pos The starting position.
     * @param rowDelta The change in row for each step (positive for down, negative for up).
     * @param colDelta The change in column for each step (positive for right, negative for left).
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
     * Executes the movement of a piece from its current position to a new position.
     * Updates the board state, visuals, and turn. If images aren't available, text is updated instead.
     * Also checks for victory conditions (e.g., if the king reaches a win tile or if all attackers are gone).
     *
     * @param piece  The piece to be moved.
     * @param newPos The new position to move the piece to.
     * @param btn    The button representing the destination tile, to be updated visually.
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
        btnToClear.setText("");

        if (PIECES[newPos.row][newPos.col] != null &&
                PIECES[newPos.row][newPos.col].isKing())
        {
            delayedWin = true;
        }

        PIECES[newPos.row][newPos.col] = piece;
        updateButtonImage(btn, piece);

        if (currentMove == Player.DEFENDER && singlePlayer)
        {
            AI.makeMove(BOARD, PIECES, thisMove, nextMove);
            updatePlayerTurn();
            //todo make a new check for win condition for AI checking if a King exists
        }

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

    /*
     * Checks if an attacker exists on the board.
     * If there are no more attackers the defender has
     * won.
     * @return true if there is an attacker. Else false.
     */
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
     * Handles the logic for when the defenders win the game.
     * Updates the defender's win statistics, writes the new stats to file,
     * generates a victory message, and displays it to the user.
     */
    private static void defendersWin()
    {
        final List<String> oldStats;
        final List<String> newStats;
        final String message;

        oldStats = TablutStats.readStats(STAT_FILE_NAME);
        newStats = TablutStats.updateStats(Player.DEFENDER, oldStats);
        TablutStats.writeStats(newStats, STAT_FILE_NAME);

        message = generateMessage(Player.DEFENDER, newStats);

        displayAlert(message);
    }

    /*
     * Handles the logic for when the attackers win the game.
     * Updates the attacker's win statistics, writes the new stats to file,
     * generates a victory message, and displays it to the user.
     */
    private static void attackersWin()
    {
        final List<String> oldStats;
        final List<String> newStats;
        final String message;

        oldStats = TablutStats.readStats(STAT_FILE_NAME);
        newStats = TablutStats.updateStats(Player.ATTACKER, oldStats);
        TablutStats.writeStats(newStats, STAT_FILE_NAME);

        message = generateMessage(Player.ATTACKER, newStats);

        displayAlert(message);
    }

    /*
     * Generates a message to be displayed to the user
     * after the game has ended.
     * @param winner the winner of the game.
     * @param stats a list of stats to be displayed as well.
     * @return a message ready to be displayed.
     */
    private static String generateMessage(final Player winner,
                                          final List<String> stats)
    {
        final StringBuilder message;
        message = new StringBuilder();

        if (winner == Player.DEFENDER)
        {
            message.append("The defenders ");
        }
        else if(winner == Player.ATTACKER)
        {
            message.append("The attackers ");
        }
        else
        {
            //this should never appear but just incase :)
            message.append("No one ");
        }

        message.append(" won this round!\nThe stats to date is now:\n")
                .append(stats.getFirst())
                .append("\n")
                .append(stats.get(SECOND_ELEMENT));

        return message.toString();
    }

    /*
     * Displays an end game alert with the outcome of the game.
     * @param message the outcome of the game.
     */
    private static void displayAlert(final String message)
    {
        final Alert      alert;
        final ButtonType continueBtn;
        final ButtonType quitBtn;

        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game over");
        alert.setContentText(message);

        continueBtn = new ButtonType("Continue");
        quitBtn = new ButtonType("Quit");

        alert.getButtonTypes().setAll(continueBtn, quitBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == continueBtn)
            {
                stage.setScene(MENU_SCENE);
            }
            else
            {
                stage.hide();
                LATCH.countDown();
            }
        });
    }

    /*
     * Updates the current player’s turn and switches to the next player.
     * If the current player is the defender, the turn is changed to the attacker.
     * When the attacker’s turn ends, it switches back to the defender,
     * selects the new next movement type, and updates the labels to match the upcoming moves.
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
     * Clears the highlighting for valid move squares and the selected piece square.
     * This ensures after a move is completed or an empty tile is selected that the
     * view is reset for game clarity.
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
                stage = new Stage();
                start(stage);
            });

            LATCH.await();
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
