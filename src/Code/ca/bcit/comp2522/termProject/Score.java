package ca.bcit.comp2522.termProject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores the score of # of games in a txt file and does some calculations
 * for if this is a new record and stuff like that.
 * Record the current time as well.
 */
class Score {


    void formatTime()
    {
        final LocalDateTime currentTime;
        final DateTimeFormatter formatter;
        final String formattedDateTime;

        currentTime = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = currentTime.format(formatter);
    }

}
