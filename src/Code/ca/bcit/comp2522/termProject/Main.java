package ca.bcit.comp2522.termProject;

import javafx.application.Platform;

import java.util.Scanner;

/**
 * todo update the javaDoc
 * Offers a menu (in the terminal; i.e. no GUI) in an infinite loop until the user presses
 * Q (or q):
 * Press W to play the Word game.
 * Press N to play the Number game.
 * Press M to play the <your game's name> game.
 * Press Q to quit.
 * If the user enters wrong data (not w, W, n, N, M, m, q, or Q), give an error message
 * and re-prompt.
 * @author Jonny Twist
 * @version 1.0
 */
final class Main
{

    private static final String QUIT = "q";
    private static final String WORD_GAME = "w";
    private static final String NUMBER_GAME = "n";
    //todo maybe change name once I know what my game is
    private static final String MY_GAME = "m";

    /*
     * Gets and validates the users input for choice of game / quit.
     * @return The user's choice of what they want to play.
     */
    private static String getUserGameChoice()
    {
        boolean validInput = false;
        String userInput;
        final Scanner scan;

        scan = new Scanner(System.in);
        userInput = scan.next();

        while (!validInput)
        {
            if(userInput.equalsIgnoreCase(WORD_GAME)    ||
               userInput.equalsIgnoreCase(NUMBER_GAME)  ||
               userInput.equalsIgnoreCase(MY_GAME)      ||
               userInput.equalsIgnoreCase(QUIT))
            {
                validInput = true;
            }
            else
            {
                System.out.println("ERROR: Not a valid input");
                promptGameChoice();
                userInput = scan.next();
            }
        }

        return userInput;
    }

    /*
     * Prompts the user to enter what game they want to play (or quit).
     */
    private static void promptGameChoice()
    {
        System.out.println(System.lineSeparator());
        System.out.println("What game would you like to play?");
        System.out.println("\t-  Word Game (" + WORD_GAME.toUpperCase() + ")");
        System.out.println("\t-  Number Game (" + NUMBER_GAME.toUpperCase() + ")");
        //todo change game name once I know what it is
        System.out.println("\t-  Tablut (" + MY_GAME.toUpperCase() + ")");
        System.out.println("\t-  Quit (" + QUIT.toUpperCase() + ")");
        System.out.println("Game choice: ");
    }

    /*
     * Launches games.
     * @param gameType the type of game we will try to launch.
     * @param <T> must implement the playable interface.
     */
    private static <T extends Playable> void launchGame(final Class<T> gameType)
    {
        try
        {
            final Playable game;
            game = gameType.getConstructor()
                    .newInstance();
            game.play();
        }
        catch (final Exception ex)
        {
            System.out.println("Game launch failed: " + ex);
        }
    }

    /**
     * Drives the program.
     * Allows users to interact with the program using a loop asking them what games
     * they want to play.
     * @param args unused.
     */
    public static void main(final String[] args)
    {
        String userChoice;

        //open the application thread
        Platform.startup(() -> {});

        do
        {

            promptGameChoice();

            userChoice = getUserGameChoice();

            switch (userChoice.toLowerCase())
            {
                case WORD_GAME -> {
                    launchGame(WordGame.class);
                }
                case NUMBER_GAME -> {
                    launchGame(NumberGame.class);
                }
                case MY_GAME -> {
                    launchGame(TablutSpinoff.class);
                }
                case QUIT -> {
                    System.out.println("Quitting...");
                }
                default -> {
                    System.out.println("HOW DID I GET HERE!!!");
                }
            }

        } while (!(userChoice.equalsIgnoreCase(QUIT)));

        //close the thread
        Platform.exit();

    }
}