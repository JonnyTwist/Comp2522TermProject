package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.tablut.TablutSpinoff;
import ca.bcit.comp2522.termproject.numbergame.NumberGame;
import ca.bcit.comp2522.termproject.wordgame.WordGame;
import javafx.application.Platform;

import java.util.Scanner;

/**
 * Main class that drives the program by allowing the user to choose and play different games.
 * This class provides a terminal-based menu where users can select between multiple games.
 * It runs in an infinite loop to keep prompting the user for input until they choose to quit by pressing 'Q'.
 * Available game options:
 * - Press 'W' to play the Word game.
 * - Press 'N' to play the Number game.
 * - Press 'M' to play a Tablut x Chess.
 * - Press 'Q' to quit the application.
 * If the user provides invalid input, the program will prompt again for a valid choice.
 *
 * @author Jonny Twist
 * @version 1.0
 */
final class Main
{

    private static final String QUIT = "q";
    private static final String WORD_GAME = "w";
    private static final String NUMBER_GAME    = "n";
    private static final String TABLUT_SPINOFF = "m";

    /*
     * Collects and validates the user's input for game selection or quit option.
     * Loops until the user enters a valid input (one of the predefined options).
     *
     * If the user enters an invalid choice, an error message is displayed and the user is prompted again.
     *
     * @return The user's input choice.
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
               userInput.equalsIgnoreCase(TABLUT_SPINOFF)      ||
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
     * Displays the game options to the user in the terminal.
     * Prompts the user with a list of available games and the option to quit.
     * This method provides an easy-to-read interface for game selection.
     */
    private static void promptGameChoice()
    {
        System.out.println(System.lineSeparator());
        System.out.println("What game would you like to play?");
        System.out.println("\t-  Word Game (" + WORD_GAME.toUpperCase() + ")");
        System.out.println("\t-  Number Game (" + NUMBER_GAME.toUpperCase() + ")");
        System.out.println("\t-  Tablut x Chess (" + TABLUT_SPINOFF.toUpperCase() + ")");
        System.out.println("\t-  Quit (" + QUIT.toUpperCase() + ")");
        System.out.println("Game choice: ");
    }

    /*
     * Launches a specified game based on the given game type class.
     * This method is capable of launching any came that implements the Playable interface.
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
                case TABLUT_SPINOFF -> {
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