package ca.bcit.comp2522.termProject;

import java.io.FileOutputStream;
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
 * todo comment all methods and comment better
 * Stores the score of # of games in a txt file and does some calculations
 * for if this is a new record and stuff like that.
 * Record the current time as well.
 */
public final class Score {

    //todo remove all magic num and replace with constants

    private static final String DEFAULT_PREV_DATE        = "";
    private static final String DATE_TIME_PHRASE         = "Date and Time: ";
    private static final String GAME_PLAYED_PHRASE       = "Games Played: ";
    private static final String TOTAL_SCORE_PHRASE       = "Total Score: ";
    private static final String CORRECT_IN_ONE_PHRASE    = "Correct First Attempts: ";
    private static final String CORRECT_IN_TWO_PHRASE    = "Correct Second Attempts: ";
    private static final String INCORRECT_PHRASE         = "Incorrect Attempts: ";
    private static final int    INCREMENT_AMOUNT         = 3;
    private static final int    DOUBLE                   = 2;
    private static final int    SMALLEST_NON_NEGATIVE    = 0;
    private static final int    MIN_SCORE                = 0;
    private static final int    MIN_GAMES_PLAYED         = 0;
    private static final int    DEFAULT_PREV_HIGH        = 0;
    private static final int    FIRST_ITEM               = 0;
    private static final int    SECOND_ITEM              = 1;
    private static final int    OFFSET_BY_ONE            = 1;
    private static final int    OFFSET_BY_TWO            = 2;
    private static final int    SPLIT_LIMIT              = 2;
    private static final int    EXPECTED_SCORE_DATA_SIZE = 5;
    private static final int    SECOND_DATA_ITEM         = 1;
    private static final int    THIRD_DATA_ITEM          = 2;
    private static final int    FOURTH_DATA_ITEM         = 3;
    private static final int    FIFTH_DATA_ITEM          = 4;

    private final String dateTimePlayed;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectSecondAttempt;


    public Score(final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectSecondAttempt)
    {
        validateNotNegative(numGamesPlayed);
        validateNotNegative(numCorrectFirstAttempt);
        validateNotNegative(numCorrectSecondAttempt);
        validateNotNegative(numIncorrectSecondAttempt);

        dateTimePlayed = formatTime(null);
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectSecondAttempt = numIncorrectSecondAttempt;
    }

    public Score(final LocalDateTime time,
                 final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectSecondAttempt)
    {
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
     * Records the score into score.txt.
     */
    public static void appendScoreToFile(final Score score,
                                         final String fileName)
    {
        final int finalScore;
        final double scoreAvg;
        final Path filePath;

        finalScore = score.getScore();
        scoreAvg = (double) finalScore / score.numGamesPlayed;
        filePath = Paths.get(fileName);

        checkHighscore(scoreAvg,
                       filePath);

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

    public final int getScore()
    {
        return (numCorrectFirstAttempt * DOUBLE) + numCorrectSecondAttempt;
    }

    //todo look at modularizing this
    private static void checkHighscore(final double scoreAvg,
                                       final Path dataPath)
    {
        //if path doesn't exist then it's a new highscore
        if (!Files.exists(dataPath))
        {
            compareScores(scoreAvg,
                          DEFAULT_PREV_HIGH,
                          DEFAULT_PREV_DATE);
            return;
        }

        //todo change this to use the readDataMethod

        final List<String> filteredData;
        String bestDateTime = "";
        double prevHighScore = MIN_SCORE;

        try
        {
            filteredData = Files.readAllLines(dataPath)
                    .stream()
                    .filter(line -> line.startsWith(TOTAL_SCORE_PHRASE) ||
                            line.startsWith(DATE_TIME_PHRASE) ||
                            line.startsWith(GAME_PLAYED_PHRASE))
                    .filter(line -> line.indexOf(": ") == line.lastIndexOf(": "))
                    .toList();

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

        try
        {
            Files.lines(dataPath).forEach(line -> {
                if(!line.isBlank())
                {
                    scoreData.add(line);

                    if(scoreData.size() == 6 || line.contains(TOTAL_SCORE_PHRASE))
                    {
                        final Score newScore = createScoreObject(scoreData);

                        if(newScore != null)
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
                .append(" points")
                .append(System.lineSeparator());

        return sb.toString();
    }
}
