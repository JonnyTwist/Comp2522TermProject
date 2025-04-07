package ca.bcit.comp2522.termproject.wordgame;

import ca.bcit.comp2522.termproject.Playable;

import java.util.Scanner;
import java.util.Random;

//todo javadoc

/**
 * todo javadoc
 * @author Jonny Twist
 * @version 1.0
 */
public final class WordGame implements Playable
{
    private static final int    INCORRECT_BOTH_TIMES = 2;
    private static final int    CORRECT_IN_ONE_GUESS = 0;
    private static final int    CORRECT_IN_TWO_GUESS = 1;
    private static final int    INITIAL_ROUND_NUMBER = 1;
    private static final int    ROUNDS_PER_GAME      = 10;
    private static final int    NUM_DIFF_ROUND_TYPES = 3;
    private static final int    NUM_DIFF_FACTS       = 3;
    private static final int    GIVEN_CAPITAL_ROUND  = 0;
    private static final int    GIVEN_COUNTRY_ROUND  = 1;
    private static final int    FACTS_ROUND          = 2;
    private static final int    MAX_GUESSES          = 2;
    private static final int    RESET                = 0;
    private static final String PLAY_AGAIN           = "yes";
    private static final String QUIT                 = "no";
    private static final String SCORE_PATH           = "score.txt";

    private static int wordGamesPlayed     = 0;
    private static int correctInOneCounter = 0;
    private static int correctInTwoCounter = 0;
    private static int incorrectCounter    = 0;

    /**
     * The starting point of the word game.
     * Runs 10 rounds then asks the user if they want to play again.
     * If they don't want to play again then the score is saved and
     * the static variables are reset.
     */
    @Override
    public void play()
    {
        String      keepPlaying;
        final Score score;

        do
        {
            doTenRounds();

            wordGamesPlayed++;

            reportTotalScore();

            keepPlaying = askUserIfTheyWantToPlayMore();

        } while (!keepPlaying.equalsIgnoreCase(QUIT));

        score = new Score(wordGamesPlayed,
                          correctInOneCounter,
                          correctInTwoCounter,
                          incorrectCounter);

        Score.appendScoreToFile(score, SCORE_PATH);

        wordGamesPlayed     = RESET;
        correctInOneCounter = RESET;
        correctInTwoCounter = RESET;
        incorrectCounter    = RESET;
    }

    /*
     * Asks the user if they want to play more.
     * Ensures that the users answer is valid.
     * @return the result of their choice.
     */
    private static String askUserIfTheyWantToPlayMore()
    {

        String userInput;
        final Scanner scan;

        scan = new Scanner(System.in);

        System.out.print("Would you like to play again? (yes/no)\nAnswer: ");
        userInput = scan.next();

        while(!userInput.equalsIgnoreCase(QUIT) &&
                !userInput.equalsIgnoreCase(PLAY_AGAIN))
        {
            System.out.println("Invalid answer!");
            System.out.print("Would you like to play again? (yes/no)\nAnswer: ");
            userInput = scan.next();
        }

        return userInput;
    }

    /*
     * todo comment better and remove javadoc
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
                case GIVEN_CAPITAL_ROUND -> {
                    roundResult = roundOfCapital(country);
                }
                case GIVEN_COUNTRY_ROUND -> {
                    roundResult = roundOfCountry(country);
                }
                case FACTS_ROUND -> {
                    final int chosenFact = rand.nextInt(NUM_DIFF_FACTS);
                    roundResult = roundOfFacts(country, chosenFact);
                }
                default -> {
                    throw new IllegalArgumentException("CODE HAS REACHED A SPOT IT SHOULDN'T");
                }
            }

            if (roundResult == CORRECT_IN_ONE_GUESS)
            {
                correctInOneCounter++;
            }
            else if (roundResult == CORRECT_IN_TWO_GUESS) {
                correctInTwoCounter++;
            }
            else
            {
                incorrectCounter++;
            }
        }
    }

    /*
     * prints the total score of word game.
     */
    private static void reportTotalScore()
    {
        System.out.println();
        System.out.println("\t-\t" + wordGamesPlayed + " word games played");
        System.out.println("\t-\t" + correctInOneCounter + " correct answers on the first attempt");
        System.out.println("\t-\t" + correctInTwoCounter + " correct answers on the second attempt");
        System.out.println("\t-\t" + incorrectCounter + " incorrect answers on two attempts each");
        System.out.println();
    }

    /*
     * Does one round of:
     * The program will print a capital city, and ask the user what country it is the
     * capital of.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfCapital(final Country country)
    {
        final String answer;
        final String countryCapital;
        final int roundResult;

        answer = country.getName();
        countryCapital = country.getCapitalCityName();

        System.out.println("NAME THE COUNTRY ROUND!");
        //todo remove after test
        System.out.println(answer);
        System.out.println("What is the country that has a capital named " + countryCapital + "?");

        roundResult = playRound(answer);

        return roundResult;
    }

    /*
     * Does one round of:
     * The program will print the country name, and ask the user what is its capital
     * city.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfCountry(final Country country)
    {
        final String answer;
        final String countryName;
        final int roundResult;

        answer = country.getCapitalCityName();
        countryName = country.getName();

        System.out.println("NAME THE CAPITAL ROUND!");
        //todo remove after test
        System.out.println(answer);
        System.out.println("What is the capital of " + countryName + "?");

        roundResult = playRound(answer);

        return roundResult;
    }

    /*
     * Does one round of:
     * The program will print one of the three facts, and ask the user which country
     * is being described.
     * @param country the country that this round will use.
     * @return the result of the round.
     */
    private static int roundOfFacts(final Country country,
                                    final int chosenFact)
    {

        final String answer;
        final String fact;
        final int roundResult;

        answer = country.getName();
        fact = country.getFacts()[chosenFact];

        System.out.println("FACT ROUND!");
        //todo remove after test
        System.out.println(answer);
        System.out.println(fact);
        System.out.println("What is the name of the country?");

        roundResult = playRound(answer);

        return roundResult;
    }

    /*
     * PLays a single round of any round type. An answer is inputted then
     * follows the rules of two chances to guess before giving correct answer.
     * @param answer the correct answer for the question.
     * @return the result of the round.
     */
    private static int playRound(final String answer)
    {
        int guessCount;
        final Scanner scan;
        String userInput;

        scan = new Scanner(System.in);

        for (guessCount = 0; guessCount < MAX_GUESSES; guessCount++)
        {
            userInput = scan.nextLine().trim();

            if (userInput.equalsIgnoreCase(answer))
            {
                System.out.println("Correct");
                break;
            }
            else
            {
                System.out.println("Incorrect");
            }
        }

        if (guessCount == INCORRECT_BOTH_TIMES)
        {
            System.out.println("The answer was: " + answer);
        }

        return guessCount;
    }
}
