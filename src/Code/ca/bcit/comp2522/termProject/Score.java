package ca.bcit.comp2522.termProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * todo comment all methods and comment better
 * Stores the score of # of games in a txt file and does some calculations
 * for if this is a new record and stuff like that.
 * Record the current time as well.
 */
class Score {

    //todo remove all magic num and replace with constants

    private static final Path   DATA_DIRECTORY_PATH;
    private static final Path   DATA_PATH;
    private static final String DEFAULT_PREV_DATE     = "";
    private static final String DATE_TIME_PHRASE      = "Date and Time: ";
    private static final String GAME_PLAYED_PHRASE    = "Games Played: ";
    private static final String TOTAL_SCORE_PHRASE    = "Total Score: ";
    private static final int    INCREMENT_AMOUNT      = 3;
    private static final int    DOUBLE                = 2;
    private static final int    SMALLEST_NON_NEGATIVE = 0;
    private static final int    MIN_SCORE             = 0;
    private static final int    MIN_GAMES_PLAYED      = 0;
    private static final int    DEFAULT_PREV_HIGH     = 0;
    private static final int    FIRST_ITEM            = 0;
    private static final int    SECOND_ITEM           = 1;
    private static final int    OFFSET_BY_ONE         = 1;
    private static final int    OFFSET_BY_TWO         = 2;
    private static final int    SPLIT_LIMIT           = 2;


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

        dataOutput.append(DATE_TIME_PHRASE)
                .append(dateTimePlayed)
                .append(System.lineSeparator())
                .append(GAME_PLAYED_PHRASE)
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
                .append(TOTAL_SCORE_PHRASE)
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
    }

    //todo look at modularizing this
    private static void checkHighscore(final double scoreAvg)
    {
        //if path doesn't exist then it's a new highscore
        if (!Files.exists(DATA_PATH))
        {
            compareScores(scoreAvg,
                          DEFAULT_PREV_HIGH,
                          DEFAULT_PREV_DATE);
            return;
        }
        System.out.println("I am here");

        final List<String> filteredData;
        String bestDateTime = "";
        double prevHighScore = MIN_SCORE;

        try
        {
            filteredData = Files.readAllLines(DATA_PATH)
                    .stream()
                    .filter(line -> line.startsWith(TOTAL_SCORE_PHRASE) ||
                            line.startsWith(DATE_TIME_PHRASE) ||
                            line.startsWith(GAME_PLAYED_PHRASE))
                    .filter(line -> line.indexOf(": ") == line.lastIndexOf(": "))
                    .toList();

            System.out.println(filteredData);

            for (int i = 0; i < filteredData.size(); i += INCREMENT_AMOUNT)
            {
                final String dateTime;
                final int gamesPlayed;
                final int totalScore;


                //todo remove magic num here
                dateTime = filteredData.get(i)
                                .split(": ", SPLIT_LIMIT)[SECOND_ITEM]
                                .trim();

                gamesPlayed = Integer.parseInt(
                                filteredData.get(i + OFFSET_BY_ONE)
                                .split(": ", SPLIT_LIMIT)[SECOND_ITEM]
                                .trim()
                                );

                totalScore = Integer.parseInt(
                                filteredData.get(i + OFFSET_BY_TWO)
                                .split(": ", SPLIT_LIMIT)[SECOND_ITEM]
                                .trim()
                                );

                //logically there should never be 0 games played but just incase I will check
                if (gamesPlayed > MIN_GAMES_PLAYED) {
                    double averageScore = (double) totalScore / gamesPlayed;

                    if (averageScore > prevHighScore) {
                        prevHighScore = averageScore;
                        bestDateTime = dateTime;
                    }
                }
            }

            compareScores(scoreAvg,
                          prevHighScore,
                          bestDateTime);

        }
        catch (final IOException ex)
        {
            System.out.println("problem reading file: " + ex);
        }
    }


    //todo ensure the prevHigh comes in as a double
    private static void compareScores(final double scoreAvg,
                                      final double prevHighScoreAvg,
                                      final String bestDateTime)
    {
        final String date;
        final String time;

        if (bestDateTime.isBlank())
        {
            System.out.println("CONGRATULATIONS! You are the new high score with an average of " +
                                       scoreAvg + " points per game; there was no previous highscore");
        }
        else if (scoreAvg > prevHighScoreAvg)
        {
            date = bestDateTime.split(" ")[FIRST_ITEM];
            time = bestDateTime.split(" ")[SECOND_ITEM];

            System.out.println("CONGRATULATIONS! You are the new high score with an average of " +
                                       scoreAvg + " points per game; the previous record was " +
                                       prevHighScoreAvg + " points per game and was set " + date +
                                       " at " + time + ".");
        }
        else
        {
            date = bestDateTime.split(" ")[FIRST_ITEM];
            time = bestDateTime.split(" ")[SECOND_ITEM];

            System.out.println("You did not beat the high score of " +
                                       prevHighScoreAvg + " points per game from "
                                       + date + " at " + time + ".");
        }
    }
}
