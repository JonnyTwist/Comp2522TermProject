package ca.bcit.comp2522.termProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalInt;

/**
 * todo comment all methods and comment better
 * Stores the score of # of games in a txt file and does some calculations
 * for if this is a new record and stuff like that.
 * Record the current time as well.
 */
class Score {

    //todo remove all magic num and replace with constants

    private static final int DOUBLE = 2;
    private static final int  SMALLEST_NON_NEGATIVE = 0;
    private static final Path DATA_DIRECTORY_PATH;
    private static final Path DATA_PATH;

    static
    {
        DATA_DIRECTORY_PATH = Paths.get("score");
        DATA_PATH = Paths.get("score", "score.txt");
    }

    private final String dateTimePlayed;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectSecondAttempt;


    Score(final int numGamesPlayed,
          final int numCorrectFirstAttempt,
          final int numCorrectSecondAttempt,
          final int numIncorrectSecondAttempt)
    {
        validateNotNegative(numGamesPlayed);
        validateNotNegative(numCorrectFirstAttempt);
        validateNotNegative(numCorrectSecondAttempt);
        validateNotNegative(numIncorrectSecondAttempt);

        dateTimePlayed = formatTime();
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectSecondAttempt = numIncorrectSecondAttempt;
    }

    private static void validateNotNegative(final int num)
    {
        if (num < SMALLEST_NON_NEGATIVE)
        {
            throw new IllegalArgumentException("No numbers in a score object can be negative");
        }
    }

    public final String getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    public final int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    public final int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    public final int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    public final int getNumIncorrectSecondAttempt()
    {
        return numIncorrectSecondAttempt;
    }

    /*
     * Gets the current dateTime and returns it formatted as a String.
     * @return the current time as a String.
     */
    private String formatTime()
    {
        final LocalDateTime currentTime;
        final DateTimeFormatter formatter;
        final String formattedDateTime;

        currentTime = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = currentTime.format(formatter);

        return formattedDateTime;
    }

    /**
     * Records the score into score.txt.
     */
    void recordScore()
    {
        final int finalScore;
        final double scoreAvg;

        finalScore = (numCorrectFirstAttempt * DOUBLE) + numCorrectSecondAttempt;
        scoreAvg = (double) finalScore / numGamesPlayed;

        try {
            Files.createDirectories(DATA_DIRECTORY_PATH);
        } catch (final IOException e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }

        checkHighscore(scoreAvg);

        final StringBuilder dataOutput;
        dataOutput = new StringBuilder();

        dataOutput.append("Date and Time: " )
                .append(dateTimePlayed)
                .append(System.lineSeparator())
                .append("Games Played: ")
                .append(numGamesPlayed)
                .append(System.lineSeparator())
                .append("Correct First Attempts: ")
                .append(numCorrectFirstAttempt)
                .append(System.lineSeparator())
                .append("Correct Second Attempts: ")
                .append(numCorrectSecondAttempt)
                .append(System.lineSeparator())
                .append("Incorrect Attempts: ")
                .append(numIncorrectSecondAttempt)
                .append(System.lineSeparator())
                .append("Total Score: ")
                .append(finalScore)
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        try
        {
            Files.writeString(
                    DATA_PATH,
                    dataOutput,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        }
        catch(final IOException e)
        {
            System.out.println("error writing score file: " + e.getMessage());
        }

        //todo check if new highscore using streams and filtering by contains and get max
        // double check if it is total score or average score
    }

    private static void checkHighscore(final double scoreAvg)
    {
        if (!Files.exists(DATA_PATH))
        {
            //todo make this path better
            System.out.println("new highscore");
            return;
        }
        System.out.println("I am here");

        final OptionalInt prevHighScore;

        try
        {
            //todo this needs changed to divide by games played
            // perhaps stream to a list then group from there... need date as well
            prevHighScore = Files.readAllLines(DATA_PATH)
                    .stream()
                    .filter(line -> line.contains("Total Score:"))
                    .filter(line -> line.indexOf(":") == line.lastIndexOf(":"))
                    .map(line -> line.split(":", 2)[1].trim())
                    .mapToInt(Integer::parseInt)
                    .max();

            prevHighScore.ifPresent(prevHigh->compareScores(scoreAvg, prevHigh));
        }
        catch (final IOException ex)
        {
            System.out.println("problem reading file: " + ex);
        }
    }

    //todo ensure the prevHigh comes in as a double
    private static void compareScores(final double scoreAvg,
                                      final double prevHighScoreAvg)
    {
        //todo this
    }
}
