package ca.bcit.comp2522.termProject;

import java.util.Scanner;
import java.util.Random;

//NEED TO READ MORE ON HOW THIS WORKS SOMETHING WITH THE a-z FILES
class WordGame
{
    private static final int INITIAL_ROUND_NUMBER = 1;
    private static final int ROUNDS_PER_GAME      = 10;
    private static final int NUM_DIFF_ROUND_TYPES = 3;
    private static final int NUM_DIFF_FACTS      = 3;
    private static final int GIVEN_CAPITAL_ROUND = 0;
    private static final int GIVEN_COUNTRY_ROUND = 1;
    private static final int FACTS_ROUND         = 2;

    private static int correctInOneCounter = 0;
    private static int correctInTwoCounter = 0;
    private static int incorrectCounter = 0;
    private static final String PLAY_AGAIN = "yes";
    private static final String QUIT = "no";

    static void playWordGame()
    {
        String keepPlaying;
        final Scanner scan;

        scan = new Scanner(System.in);


        do
        {
            doTenRounds();

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

    /**
     * todo javadoc better
     * Loops 10 rounds of the word game and records results.
     */
    private static void doTenRounds()
    {
        final Random rand;
        int countryIndex;
        Country country;
        int roundType;
        int roundResult;

        rand = new Random();


        for (int roundNum = INITIAL_ROUND_NUMBER; roundNum <= ROUNDS_PER_GAME; roundNum++)
        {

            countryIndex = rand.nextInt(World.countriesValues.size());
            country = World.countriesValues.get(countryIndex);

            roundType = rand.nextInt(NUM_DIFF_ROUND_TYPES);

            System.out.printf("\nRound %d:\n", roundNum);

            switch (roundType)
            {
                case GIVEN_CAPITAL_ROUND ->
                    roundResult = roundOfCapital(country);
                case GIVEN_COUNTRY_ROUND ->
                    roundResult = roundOfCountry(country);
                case FACTS_ROUND ->
                    roundResult = roundOfFacts(country);
            }

            //todo the calculations after the rounds

        }
    }

    /**
     * todo implement
     * Does one round of:
     * The program will print a capital city, and ask the user what country it is the
     * capital of.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfCapital(final Country country)
    {
        final String answer;
        return 0;
    }

    /**
     * todo implement
     * Does one round of:
     * The program will print the country name, and ask the user what is its capital
     * city.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfCountry(final Country country)
    {
        final String answer;
        return 0;
    }

    /**
     * todo implement
     * Does one round of:
     * The program will print one of the three facts, and ask the user which country
     * is being described.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfFacts(final Country country)
    {
        final String answer;
        return 0;
    }
}
