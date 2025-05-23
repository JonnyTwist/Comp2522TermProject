package ca.bcit.comp2522.termproject.wordgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tracks statistics for the WordGame.
 * Creates score objects to store data for a game session.
 * Provides methods to:
 * - Append to a score file.
 * - Check if this is a new highscore.
 * - Format and print score data in a readable format.
 *
 * @author Jonny Twist
 * @version 1.0
 */
public final class Score {

    private static final String DEFAULT_PREV_DATE        = "";
    private static final String DATE_TIME_PHRASE         = "Date and Time: ";
    private static final String GAME_PLAYED_PHRASE       = "Games Played: ";
    private static final String TOTAL_SCORE_PHRASE       = "Total Score: ";
    private static final String CORRECT_IN_ONE_PHRASE    = "Correct First Attempts: ";
    private static final String CORRECT_IN_TWO_PHRASE    = "Correct Second Attempts: ";
    private static final String INCORRECT_PHRASE         = "Incorrect Attempts: ";
    private static final String CONGRATS_MSG_START       = "CONGRATULATIONS! You are the" +
                                                           " new high score with an average of ";

    private static final int DOUBLE                   = 2;
    private static final int SMALLEST_NON_NEGATIVE    = 0;
    private static final int MIN_SCORE                = 0;
    private static final int MIN_GAMES_PLAYED         = 0;
    private static final int DEFAULT_PREV_HIGH        = 0;
    private static final int FIRST_ITEM               = 0;
    private static final int SECOND_ITEM              = 1;
    private static final int EXPECTED_SCORE_DATA_SIZE = 5;
    private static final int FINAL_SCORE_DATA_SIZE    = 6;
    private static final int SECOND_DATA_ITEM         = 1;
    private static final int THIRD_DATA_ITEM          = 2;
    private static final int FOURTH_DATA_ITEM         = 3;
    private static final int FIFTH_DATA_ITEM          = 4;
    private static final int MIN_CORRECT_IN_ONE       = 0;
    private static final int MIN_CORRECT_IN_TWO       = 0;
    private static final int MIN_INCORRECT            = 0;

    private final String dateTimePlayed;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectSecondAttempt;

    /**
     * Constructor for the score object that doesn't take a time.
     * Calls the other constructor and hands in now() as the time.
     *
     * @param numGamesPlayed the number of games played.
     * @param numCorrectFirstAttempt the number of times the user
     *                               was correct on the first attempt.
     * @param numCorrectSecondAttempt the number of times the user
     *                                was correct on the second attempt.
     * @param numIncorrectSecondAttempt the number of times the user
     *                                  was incorrect on both attempts.
     */
    public Score(final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectSecondAttempt)
    {
        this(LocalDateTime.now(),
             numGamesPlayed,
             numCorrectFirstAttempt,
             numCorrectSecondAttempt,
             numIncorrectSecondAttempt);
    }

    /**
     * Constructor for the complete score object.
     *
     * @param time the time the game ended.
     * @param numGamesPlayed the number of games played.
     * @param numCorrectFirstAttempt the number of times the user
     *                               was correct on the first attempt.
     * @param numCorrectSecondAttempt the number of times the user
     *                                was correct on the second attempt.
     * @param numIncorrectSecondAttempt the number of times the user
     *                                  was incorrect on both attempts.
     */
    public Score(final LocalDateTime time,
                 final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectSecondAttempt)
    {
        validateDateTime(time);
        validateNotNegative(numGamesPlayed);
        validateNotNegative(numCorrectFirstAttempt);
        validateNotNegative(numCorrectSecondAttempt);
        validateNotNegative(numIncorrectSecondAttempt);

        dateTimePlayed = formatTime(time);
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectSecondAttempt = numIncorrectSecondAttempt;
    }

    /*
     * Validates that the date time is not in the future.
     * Note: does not validate that it is not null because that is
     * handled by the formatTime method.
     * @param time the time to validate.
     */
    private static void validateDateTime(final LocalDateTime time)
    {
        if (time != null && time.isAfter(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("Time cannot be in the future");
        }
    }

    /*
     * Validates that a number is not negative
     * @param num the number to be validated
     */
    private static void validateNotNegative(final int num)
    {
        if (num < SMALLEST_NON_NEGATIVE)
        {
            throw new IllegalArgumentException("No numbers in a score object can be negative");
        }
    }

    /*
     * Validates that a score object is not null and contains valid instance data.
     * @param score the score to validate
     */
    private static void validateScore(final Score score)
    {
        if (score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }

        validateNotNegative(score.numGamesPlayed);
        validateNotNegative(score.numCorrectFirstAttempt);
        validateNotNegative(score.numCorrectSecondAttempt);
        validateNotNegative(score.numIncorrectSecondAttempt);
    }

    /*
     * Validates that a file name is not null or blank.
     * @param fileName the fileName to validate
     */
    private static void validateFileName(final String fileName)
    {
        if (fileName == null || fileName.isBlank())
        {
            throw new IllegalArgumentException("File name cannot be null or blank");
        }
    }

    /**
     * getter for the dateTime played.
     * @return the dateTime played.
     */
    public String getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    /**
     * Getter for the number of games played.
     * @return the number of games played.
     */
    public int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Getter for the number of times the user was correct on the first attempt.
     * @return return the number of times the user was correct on the first attempt.
     */
    public int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    /**
     * Getter for the number of times the user was correct on the second attempt.
     * @return return the number of times the user was correct on the second attempt.
     */
    public int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    /**
     * Getter for the number of times the user was incorrect on both attempts.
     * @return return the number of times the user was in incorrect on both attempts.
     */
    public int getNumIncorrectSecondAttempt()
    {
        return numIncorrectSecondAttempt;
    }

    /**
     * Calculates the total score.
     * Note: this is not the average score.
     * @return the total score ((first try x 2) + (second try x 1)).
     */
    public int getScore()
    {
        return (numCorrectFirstAttempt * DOUBLE) + numCorrectSecondAttempt;
    }

    /**
     * Getter for the average score (total score divided by number of games played)
     * @return the average score of a Score object.
     */
    public double getAvgScore()
    {
        return (double) getScore() / numGamesPlayed;
    }

    /*
     * Formats the provided LocalDateTime as a string in the format "yyyy-MM-dd HH:mm:ss".
     * If the provided time is null, the current system time is used.
     *
     * @param time the LocalDateTime to format, or null to use the current time
     * @return the formatted date and time as a string
     */
    private String formatTime(final LocalDateTime time)
    {
        final LocalDateTime currentTime;
        final DateTimeFormatter formatter;
        final String formattedDateTime;

        if (time == null)
        {
            currentTime = LocalDateTime.now();
        }
        else
        {
            currentTime = time;
        }

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = currentTime.format(formatter);

        return formattedDateTime;
    }

    /**
     * Appends Score objects onto the end of a file.
     * Also calls the checkHighscore message before this so we
     * know if this is a new record.
     *
     * @param score the score object to be appended onto the file.
     * @param fileName the name of the file to append the score object onto.
     */
    public static void appendScoreToFile(final Score score,
                                         final String fileName)
    {
        validateScore(score);
        validateFileName(fileName);

        final int finalScore;
        final double scoreAvg;
        final Path filePath;

        finalScore = score.getScore();
        scoreAvg = (double) finalScore / score.numGamesPlayed;
        filePath = Paths.get(fileName);

        checkHighscore(scoreAvg,
                       filePath,
                       fileName);

        final StringBuilder dataOutput;
        dataOutput = new StringBuilder();

        dataOutput.append(DATE_TIME_PHRASE)
                .append(score.dateTimePlayed)
                .append(System.lineSeparator())
                .append(GAME_PLAYED_PHRASE)
                .append(score.numGamesPlayed)
                .append(System.lineSeparator())
                .append(CORRECT_IN_ONE_PHRASE)
                .append(score.numCorrectFirstAttempt)
                .append(System.lineSeparator())
                .append(CORRECT_IN_TWO_PHRASE)
                .append(score.numCorrectSecondAttempt)
                .append(System.lineSeparator())
                .append(INCORRECT_PHRASE)
                .append(score.numIncorrectSecondAttempt)
                .append(System.lineSeparator())
                .append(TOTAL_SCORE_PHRASE)
                .append(finalScore)
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        try
        {
            Files.writeString(
                    filePath,
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

    /*
     * Gets the previous highscore from the specified file then
     * hands it off to compareScores to handle the printing of
     * if this is a new high score.
     * @param scoreAvg the average score of the play though that just ended.
     * @param dataPath the path that the data is stored in.
     * @param fileName the name of the file that the data is stored in.
     */
    private static void checkHighscore(final double scoreAvg,
                                       final Path dataPath,
                                       final String fileName)
    {
        //if path doesn't exist then it's a new highscore
        if (!Files.exists(dataPath))
        {
            compareScores(scoreAvg,
                          DEFAULT_PREV_HIGH,
                          DEFAULT_PREV_DATE);
            return;
        }

        final List<Score> allScores;
        String bestDateTime;
        double prevHighScore;

        allScores = readScoresFromFile(fileName);
        bestDateTime = DEFAULT_PREV_DATE;
        prevHighScore = MIN_SCORE;

        //if the file is empty this is a new highscore (handled by compare scores)
        if (allScores.isEmpty())
        {
            compareScores(scoreAvg,
                          prevHighScore,
                          bestDateTime);
            return;
        }

        //get the previous highscore and the time that was placed
        for (final Score score : allScores)
        {
            double scoreTotal;
            if (score.numGamesPlayed <= MIN_GAMES_PLAYED)
            {
                scoreTotal = MIN_SCORE;
            }
            else
            {
                scoreTotal = score.getAvgScore();
            }

            if (scoreTotal > prevHighScore)
            {
                bestDateTime = score.dateTimePlayed;
                prevHighScore = scoreTotal;
            }
        }

        compareScores(scoreAvg,
                      prevHighScore,
                      bestDateTime);
    }


    /*
     * Compares the new score with the previous highscore and prints a message.
     * If no previous high score exists, a congratulatory message is printed.
     * If the new score beats the previous high, a congratulatory message
     * including details about the old highscore is printed.
     * Otherwise, a message stating the high score remains is shown.
     *
     * @param scoreAvg the new score that has been placed.
     * @param prevHighScoreAvg the previous highscore.
     * @param bestDateTime the time the previous highscore was placed.
     */
    private static void compareScores(final double scoreAvg,
                                      final double prevHighScoreAvg,
                                      final String bestDateTime)
    {
        final String date;
        final String time;

        if (bestDateTime.isBlank())
        {
            System.out.println(CONGRATS_MSG_START +
                                       scoreAvg +
                                       " points per game; there was no previous highscore");
        }
        else if (scoreAvg > prevHighScoreAvg)
        {
            date = bestDateTime.split(" ")[FIRST_ITEM];
            time = bestDateTime.split(" ")[SECOND_ITEM];

            System.out.println(CONGRATS_MSG_START + scoreAvg +
                                       " points per game; the previous record was " +
                                       prevHighScoreAvg + " points per game and was set " +
                                       date + " at " + time + ".");
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

    /**
     * Reads a file from the given path and extracts a list of valid Score objects.
     * Groups lines of score data and parses each group into a Score using createScoreObject.
     * Skips blank lines and invalid entries. Returns an empty list if the file doesn't exist
     * or an error occurs while reading.
     * @param path the path to the file in String form.
     * @return A list of score object that have been read in from the file.
     */
    public static List<Score> readScoresFromFile(final String path)
    {
        final Path  dataPath;
        final List<Score> scores;

        dataPath  = Paths.get(path);
        scores = new ArrayList<>();

        //if the file doesn't exist then there is nothing to read. return empty list
        if (!Files.exists(dataPath))
        {
            return scores;
        }

        final List<String> scoreData;

        scoreData = new ArrayList<>();


        try (Stream<String> lines = Files.lines(dataPath)) {
            lines.forEach(line -> {
                if (!line.isBlank())
                {
                    scoreData.add(line);

                    if (scoreData.size() == FINAL_SCORE_DATA_SIZE ||
                        line.contains(TOTAL_SCORE_PHRASE))
                    {
                        final Score newScore = createScoreObject(scoreData);

                        if (newScore != null)
                        {
                            scores.add(newScore);
                        }
                        scoreData.clear();
                    }
                }
            });
        }
        catch (final IOException ex)
        {
            System.out.println("Failed to read file: " + ex);
        }

        return scores;
    }

    /*
     * Attempts to create a string object from a list of strings.
     *
     * The method expects the list to contain a fixed number of lines (as defined by
     * EXPECTED_SCORE_DATA_SIZE), each starting with a specific phrase:
     * - DATE_TIME_PHRASE for the date and time the game was played
     * - GAME_PLAYED_PHRASE for the number of games played
     * - CORRECT_IN_ONE_PHRASE for the number of correct answers on the first attempt
     * - CORRECT_IN_TWO_PHRASE for the number of correct answers on the second attempt
     * - INCORRECT_PHRASE for the number of incorrect answers
     *
     * It validates that:
     * 1. The list is the expected size.
     * 2. Each string starts with the appropriate identifying phrase.
     * 3. Each numeric value can be parsed and is greater than or equal to its corresponding
     *    minimum allowed value (e.g., MIN_GAMES_PLAYED).
     *
     * If all conditions are met, a new Score object is created and returned.
     * Otherwise, the method logs a message and returns null to indicate that the
     * data was corrupted or invalid.
     *
     * @param scoreData A list of data that will be parsed into a Score object.
     * @return the new score object if it can be created. Else null.
     */
    private static Score createScoreObject(final List<String> scoreData)
    {
        final String dateTimeLine;
        final String gamePlayedLine;
        final String corInOneLine;
        final String corInTwoLine;
        final String incorrectLine;

        final LocalDateTime dateTime;
        final int gamesPlayed;
        final int correctInOne;
        final int correctInTwo;
        final int incorrect;

        final DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (scoreData.size() < EXPECTED_SCORE_DATA_SIZE)
        {
            System.out.println("Corrupted data skipped");
            return null;
        }

        dateTimeLine = scoreData.getFirst();
        gamePlayedLine = scoreData.get(SECOND_DATA_ITEM);
        corInOneLine = scoreData.get(THIRD_DATA_ITEM);
        corInTwoLine = scoreData.get(FOURTH_DATA_ITEM);
        incorrectLine = scoreData.get(FIFTH_DATA_ITEM);


        if (dateTimeLine.startsWith(DATE_TIME_PHRASE) &&
            gamePlayedLine.startsWith(GAME_PLAYED_PHRASE) &&
            corInOneLine.startsWith(CORRECT_IN_ONE_PHRASE) &&
            corInTwoLine.startsWith(CORRECT_IN_TWO_PHRASE) &&
            incorrectLine.startsWith(INCORRECT_PHRASE))
        {
            try
            {
                dateTime = LocalDateTime.parse(dateTimeLine.substring(DATE_TIME_PHRASE.length()), formatter);
                gamesPlayed = Integer.parseInt(gamePlayedLine.substring(GAME_PLAYED_PHRASE.length()));
                correctInOne = Integer.parseInt(corInOneLine.substring(CORRECT_IN_ONE_PHRASE.length()));
                correctInTwo  = Integer.parseInt(corInTwoLine.substring(CORRECT_IN_TWO_PHRASE.length()));
                incorrect  = Integer.parseInt(incorrectLine.substring(INCORRECT_PHRASE.length()));

                if (gamesPlayed < MIN_GAMES_PLAYED ||
                    correctInOne < MIN_CORRECT_IN_ONE ||
                    correctInTwo < MIN_CORRECT_IN_TWO ||
                    incorrect < MIN_INCORRECT) {
                    System.out.println("Invalid score data (negative values). Corrupted data skipped.");
                    return null;
                }

                return new Score(dateTime,
                                 gamesPlayed,
                                 correctInOne,
                                 correctInTwo,
                                 incorrect);
            }
            catch (final Exception ex)
            {
                System.out.println("Failed to parse lines. Corrupted data skipped: " + ex);
                return null;
            }
        }
        else
        {
            System.out.println("Corrupted data skipped");
            return null;
        }
    }

    /**
     * Returns a string representation of the Score object in a structured and readable format.
     * The return includes:
     * - The date and time when the game was played.
     * - The total number of games played during the session.
     * - The number of questions answered correctly on the first attempt.
     * - The number of questions answered correctly on the second attempt.
     * - The number of questions that were still incorrect after the second attempt.
     * - The final calculated score.
     *
     * @return all instance variables and the formatted final score
     * in the specified format.
     */
    @Override
    public String toString()
    {
        final StringBuilder sb;
        final int finalScore;

        sb = new StringBuilder();
        finalScore = getScore();


        sb.append(DATE_TIME_PHRASE)
                .append(dateTimePlayed)
                .append("\n")
                .append(GAME_PLAYED_PHRASE)
                .append(numGamesPlayed)
                .append("\n")
                .append(CORRECT_IN_ONE_PHRASE)
                .append(numCorrectFirstAttempt)
                .append("\n")
                .append(CORRECT_IN_TWO_PHRASE)
                .append(numCorrectSecondAttempt)
                .append("\n")
                .append(INCORRECT_PHRASE)
                .append(numIncorrectSecondAttempt)
                .append("\n")
                .append("Score: ")
                .append(finalScore)
                .append(" points\n");

        return sb.toString();
    }
}
