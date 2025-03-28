package ca.bcit.comp2522.termProject;

// AI IS ALLOWED ON MY OWN GAME
// MUST BE UNIQUE

//todo make game 2 player (tablut) viking chess
public final class MyGame implements Playable
{
    private static final int BOARD_SIZE = 9;
    private static final int[][] board;

    static
    {
        board = new int[BOARD_SIZE][BOARD_SIZE];
    }

    public void play()
    {
        System.out.println("Welcome to my game!!");
    }
}
