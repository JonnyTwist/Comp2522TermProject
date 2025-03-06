package ca.bcit.comp2522.termProject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * todo comment all methods and comment better
 * Stores the score of # of games in a txt file and does some calculations
 * for if this is a new record and stuff like that.
 * Record the current time as well.
 */
class Score {

    private static final int SMALLEST_NON_NEGATIVE = 0;

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

    //todo make a method that adds to / creates a score.txt then call that in WordGame

}
