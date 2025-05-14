package ca.bcit.comp2522.termproject.tablut;

import javafx.scene.control.Button;

import static ca.bcit.comp2522.termproject.tablut.TablutSpinoff.*;

class AI
{

    private static final int FIRST_ELEMENT = 0;
    private static final int SECOND_ELEMENT = 1;

    private static Button[][] board;
    private static Piece[][] pieces;
    private static Movement thisMove;
    private static Movement nextMove;

    static void makeMove(final Button[][] b,
                         final Piece[][] p,
                         final Movement tm,
                         final Movement nm)
    {
        validateBoard(b);
        validatePieces(b, p);
        validateMovement(tm);
        validateMovement(nm);

        board = b;
        pieces = p;
        thisMove = tm;
        nextMove = nm;

        checkForWinningMove();
    }

    private static void validateBoard(final Button[][] board)
    {
        if (board.length != board[0].length)
        {
            throw new IllegalArgumentException("Board is not a square");
        }
    }

    private static void validatePieces(final Button[][] board,
                                       final Piece[][] pieces)
    {
        if (board.length != pieces.length || board[0].length != pieces[0].length)
        {
            throw new IllegalArgumentException("Board size does not match pieces size");
        }

        if (pieces.length != pieces[0].length)
        {
            throw new IllegalArgumentException("Pieces is not a square");
        }
    }

    private static void validateMovement(final Movement movement)
    {
        if (movement != Movement.KNIGHT &&
            movement != Movement.ROOK &&
            movement != Movement.BISHOP &&
            movement != Movement.QUEEN)
        {
            throw new IllegalArgumentException("Unknown Movement");
        }
    }

    private static void checkForWinningMove()
    {
        for(int row = 0; row < TablutSpinoff.BOARD_SIZE; row++)
        {
            for (int col = 0; col < TablutSpinoff.BOARD_SIZE; col++)
            {
                if (pieces[row][col] != null && pieces[row][col].getOwner() == Player.ATTACKER)
                {
                    checkCanCapKing(row, col);
                }
            }
        }
    }

    private static void checkCanCapKing(final int row,
                                        final int col)
    {
        switch (thisMove)
        {
            case KNIGHT ->
                checkKnightMove(row, col);
            case QUEEN ->
            {
                checkRookMove(row, col);
                checkBishopMove(row, col);
            }
            case ROOK ->
                checkRookMove(row, col);
            case BISHOP ->
                checkBishopMove(row, col);
        }
    }

    private static void checkKnightMove(final int row,
                                        final int col)
    {
        final int[][] knightMoves = getKnightMoves();

        for (final int[] move : knightMoves)
        {
            final int newRow;
            final int newCol;

            newRow = row + move[FIRST_ELEMENT];
            newCol = col + move[SECOND_ELEMENT];

            if (newRow >= FIRST_ROW && newRow <= LAST_ROW &&
                    newCol >= FIRST_COL && newCol <= LAST_COL &&
                    (newCol != CENTER_COL || newRow != CENTER_ROW) &&
                    ((pieces[newRow][newCol] == null || pieces[newRow][newCol].getOwner() != currentMove) && pieces[newRow][newCol].isKing()))
            {
                //todo def change
                System.out.println("King can be captured by Knight from " + row + ", " + col);
            }
        }
    }

    private static void checkRookMove(final int row,
                                      final int col)
    {
        checkDirection(row, col, DOWN, NO_MOVEMENT);
        checkDirection(row, col, UP, NO_MOVEMENT);
        checkDirection(row, col, NO_MOVEMENT, RIGHT);
        checkDirection(row, col, NO_MOVEMENT, LEFT);
    }

    private static void checkBishopMove(final int row,
                                        final int col)
    {
        checkDirection(row, col, DOWN, RIGHT);
        checkDirection(row, col, DOWN, LEFT);
        checkDirection(row, col, UP, RIGHT);
        checkDirection(row, col, UP, LEFT);
    }

    /*
     * Highlights all valid move positions in a straight line from a given position,
     * following the specified row and column direction.
     * Stops when it hits the edge of the board, a restricted tile, or an occupied space.
     * If the occupied space belongs to the opponent and is not restricted,
     * it is marked as a valid capture.
     *
     * todo fix javadoc here
     * @param pos The starting position.
     * @param rowDelta The change in row for each step (positive for down, negative for up).
     * @param colDelta The change in column for each step (positive for right, negative for left).
     */
    private static void checkDirection(final int r,
                                       final int c,
                                       final int rowDelta,
                                       final int colDelta)
    {
        int row;
        int col;

        row = r + rowDelta;
        col = c + colDelta;

        while (row >= FIRST_ROW && row <= LAST_ROW && col >= FIRST_COL && col <= LAST_COL)
        {
            //todo change logic to check for king capture

            final boolean isRestricted;
            isRestricted = board[row][col].getStyleClass().contains(RESTRICTED);

            if (pieces[row][col] != null && pieces[row][col].isKing())
            {
                System.out.println("King is capturable from " + r + ", " + c);
            }

            if (pieces[row][col] != null || isRestricted)
            {
                break;
            }

            row += rowDelta;
            col += colDelta;
        }
    }


}
