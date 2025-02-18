package ca.bcit.comp2522.termProject;

import java.util.Scanner;
import java.util.Random;

//NEED TO READ MORE ON HOW THIS WORKS SOMETHING WITH THE a-z FILES
class WordGame
{
    private static int correctInOneCounter;
    private static int correctInTwoCounter;
    private static int incorrectCounter;
    private static final String PLAY_AGAIN = "yes";
    private static final String QUIT = "no";

    static
    {
        correctInOneCounter = 0;
        correctInTwoCounter = 0;
        incorrectCounter = 0;
    }

    static void playWordGame()
    {
        String keepPlaying;
        final Scanner scan;
        scan = new Scanner(System.in);

        do
        {



            //END OF GAME
            System.out.print("Would you like to play again? (yes/no)\nAnswer: ");
            keepPlaying = scan.next();

            while(!keepPlaying.equalsIgnoreCase(QUIT) &&
                  !keepPlaying.equalsIgnoreCase(PLAY_AGAIN))
            {
                System.out.println("Invalid answer!");
                System.out.print("Would you like to play again? (yes/no)\nAnswer: ");
                keepPlaying = scan.next();
            }

        } while (!keepPlaying.equalsIgnoreCase(QUIT));

        //todo record the score
    }
}
