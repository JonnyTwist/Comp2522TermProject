package ca.bcit.comp2522.termProject.myGame;

// TablutGame.java

import ca.bcit.comp2522.termProject.Playable;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TablutGame implements Playable
{
    private       Player             currentPlayer;
    private       Piece[][]          board;
    private final List<GameObserver> observers = new ArrayList<>();
    private       Position           selectedPosition;
    private final Player             DEFENDER  = Player.DEFENDER;
    private final Player             ATTACKER  = Player.ATTACKER;

    // Inner classes remain the same (Position, Move, Piece, King, Defender, Attacker)

    public TablutGame() {
        this.board = new Piece[9][9];
        this.currentPlayer = Player.ATTACKER;
        initializeBoard();
    }

    // Add these missing methods:
    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    private void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Inner class for position
    public static class Position implements Serializable {
        final int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        // equals, hashCode, toString
    }

    // Inner class for move
    public static class Move implements Serializable {
        final Position from, to;
        public Move(Position from, Position to) {
            this.from = from;
            this.to = to;
        }
        // equals, hashCode
    }

    // Abstract piece class
    public abstract class Piece implements Serializable {
        protected final Player owner;
        protected Position position;

        public Piece(Player owner, Position position) {
            this.owner = owner;
            this.position = position;
        }

        public Player getOwner()
        {
            return owner;
        }

        public abstract boolean isValidMove(Position newPosition);
    }

    // Concrete piece classes
    public class King extends Piece {
        public King(Player owner, Position position) {
            super(owner, position);
        }

        @Override
        public boolean isValidMove(Position newPosition) {
            // King moves like a rook
            return newPosition.x == position.x || newPosition.y == position.y;
        }
    }

    public class Defender extends Piece {
        public Defender(Player owner, Position position) {
            super(owner, position);
        }

        @Override
        public boolean isValidMove(Position newPosition) {
            // King moves like a rook
            return newPosition.x == position.x || newPosition.y == position.y;
        }
    }

    public class Attacker extends Piece {
        public Attacker(Player owner, Position position) {
            super(owner, position);
        }

        @Override
        public boolean isValidMove(Position newPosition) {
            // King moves like a rook
            return newPosition.x == position.x || newPosition.y == position.y;
        }
    }

    private void initializeBoard() {
        // Set up initial board configuration
        board[4][4] = new King(DEFENDER, new Position(4, 4));

        // Set up defenders
        int[][] defenderPositions = {{3,4}, {4,3}, {4,5}, {5,4}};
        for (int[] pos : defenderPositions) {
            board[pos[0]][pos[1]] = new Defender(DEFENDER, new Position(pos[0], pos[1]));
        }

        // Set up attackers using streams
        IntStream.range(0, 9).forEach(x -> {
            IntStream.range(0, 9).forEach(y -> {
                if ((x == 0 || x == 8 || y == 0 || y == 8) &&
                        !(x == 4 && y == 4)) {
                    board[x][y] = new Attacker(ATTACKER, new Position(x, y));
                }
            });
        });
    }

    public boolean isValidMove(Move move) {
        if (move.from.x < 0 || move.from.x >= 9 || move.from.y < 0 || move.from.y >= 9 ||
                move.to.x < 0 || move.to.x >= 9 || move.to.y < 0 || move.to.y >= 9) {
            return false;
        }

        Piece piece = board[move.from.x][move.from.y];
        if (piece == null || piece.owner != currentPlayer) {
            return false;
        }

        if (!piece.isValidMove(move.to)) {
            return false;
        }

        // Check path is clear
        if (move.from.x == move.to.x) {
            int minY = Math.min(move.from.y, move.to.y);
            int maxY = Math.max(move.from.y, move.to.y);
            return IntStream.range(minY + 1, maxY)
                    .allMatch(y -> board[move.from.x][y] == null);
        } else if (move.from.y == move.to.y) {
            int minX = Math.min(move.from.x, move.to.x);
            int maxX = Math.max(move.from.x, move.to.x);
            return IntStream.range(minX + 1, maxX)
                    .allMatch(x -> board[x][move.from.y] == null);
        }
        return false;
    }

    public void makeMove(Move move) {
        if (!isValidMove(move)) {
            throw new IllegalArgumentException("Invalid move");
        }

        Piece piece = board[move.from.x][move.from.y];
        board[move.from.x][move.from.y] = null;
        board[move.to.x][move.to.y] = piece;
                                      piece.position = move.to;

        checkCaptures(move.to);
        checkWinConditions();

        currentPlayer = (currentPlayer == ATTACKER) ? DEFENDER : ATTACKER;
        notifyObservers();
    }

    private void checkCaptures(Position movedTo) {
        // Check for captures in all four directions
        // Implement Hnefatafl capture rules
    }

    private void checkWinConditions() {
        // Check if king reached edge (defenders win)
        // Check if king is captured (attackers win)
    }

    // NIO Save/Load methods
    public void saveGame(Path path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(path))) {
            out.writeObject(this.board);
            out.writeObject(this.currentPlayer);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame(Path path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(path))) {
            this.board = (Piece[][]) in.readObject();
            this.currentPlayer = (Player) in.readObject();
        }
    }

    // Implement Playable and RNGGame interfaces
    @Override
    public void play() {
        System.out.println("I am here");
        initializeBoard();
        currentPlayer = ATTACKER;
        notifyObservers();
    }
}
