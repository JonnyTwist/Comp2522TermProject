package ca.bcit.comp2522.termproject.tablut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for Tablut game to manage reading, writing and formatting
 * of the game statistics.
 * @author Jonny Twist
 * @version 1.0
 */
final class TablutStats
{
    private static final int DEFAULT_STAT_WINS  = 0;
    private static final int EXPECTED_STATS_LEN = 2;
    private static final int SECOND_ELEMENT     = 1;
    private static final int INCREMENT          = 1;

    private static final String ATTACK_WIN_STR = "Attacker Wins: ";
    private static final String DEFEND_WIN_STR = "Defender Wins: ";


    /**
     * Reads from a specified file looking for two specific lines
     * that contains a count of the number of attacker wins and the
     * count of the number of defender wins.
     * The method ensures that there is exactly one line for each of the following stats:
     * - Attacker Wins
     * - Defender Wins
     * If either stat is missing or malformed, the default value for that stat will be used
     *
     * @param fileName the file that we will search for and through.
     * @return a List of stats containing the defender and attacker win count.
     */
    static List<String> readStats(final String fileName)
    {
        validateFileName(fileName);

        final List<String> stats;
        final Path filePath;
        final List<String> filteredLines;

        filePath = Paths.get(fileName);
        stats = new ArrayList<>();

        if (!Files.exists(filePath))
        {
            stats.add(ATTACK_WIN_STR + DEFAULT_STAT_WINS);
            stats.add(DEFEND_WIN_STR + DEFAULT_STAT_WINS);
            return stats;
        }

        try
        {
            filteredLines = Files.readAllLines(filePath)
                            .stream()
                            .filter(line-> line.startsWith(ATTACK_WIN_STR) ||
                                    line.startsWith(DEFEND_WIN_STR))
                            .toList();

            boolean attackFound = false;
            boolean defendFound = false;

            for (final String line : filteredLines)
            {
                if (!attackFound &&
                    line.startsWith(ATTACK_WIN_STR))
                {
                    stats.add(line);
                    attackFound = true;
                }
                else if (!defendFound &&
                         line.startsWith(DEFEND_WIN_STR))
                {
                    stats.add(line);
                    defendFound = true;
                }
            }

            // If either stat is missing, add the default value
            if (!attackFound) {
                stats.add(ATTACK_WIN_STR + DEFAULT_STAT_WINS);
            }
            if (!defendFound) {
                stats.add(DEFEND_WIN_STR + DEFAULT_STAT_WINS);
            }
        }
        catch (final IOException ex)
        {
            System.out.println("Error reading the file: " + ex);
            stats.add(ATTACK_WIN_STR + DEFAULT_STAT_WINS);
            stats.add(DEFEND_WIN_STR + DEFAULT_STAT_WINS);
        }

        return stats;
    }

    /**
     * Writes the updated statistics to a specified file.
     * This method takes a list of stats and writes them to a file, ensuring that the file is created
     * if it doesn't already exist and truncates the existing file if necessary. It validates the
     * statistics and the file name before attempting to write the data to the file.
     * If an error occurs during the file writing process, an error message is printed to the console.
     *
     * @param stats the stats to write into the file. Contains attacker wins and defender wins.
     * @param fileName the name of the file to write our stats to.
     */
    static void writeStats(final List<String> stats, final String fileName)
    {
        validateStats(stats);
        validateFileName(fileName);

        final String dataOutput;
        final Path filePath;

        filePath = Paths.get(fileName);
        dataOutput = formatOutput(stats);

        try
        {
            Files.writeString(
                    filePath,
                    dataOutput,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }
        catch(final IOException e)
        {
            System.out.println("error writing stats to file: " + e.getMessage());
        }
    }

    /*
     * Formats the statistics list into a string for output. The format consists of two lines:
     * one for the attacker wins and one for the defender wins. Each line contains the stat label
     * followed by the corresponding value. The statistics are validated
     * before formatting the output string.
     *
     * @param stats the list of statistics to be formatted. The list must contain exactly two elements:
     *              one for attacker wins and one for defender wins.
     * @return a string representing the formatted statistics, with each stat on a new line.
     */
    private static String formatOutput(final List<String> stats)
    {
        validateStats(stats);

        final StringBuilder output;

        output = new StringBuilder();

        output.append(stats.getFirst())
                .append("\n")
                .append(stats.get(SECOND_ELEMENT));

        return output.toString();
    }

    /**
     * Updates the statistics by incrementing the statistic of a specified player.
     * Validates input to ensure that stats are valid and that the winner is valid.
     *
     * @param winner the player who won the game and will be incremented.
     * @param stats the list of stats to update.
     * @return an updated version of the statistics.
     */
    static List<String> updateStats(final Player winner,
                                    final List<String> stats)
    {
        validateWinner(winner);
        validateStats(stats);

        for (int i = 0; i < stats.size(); i++)
        {
            final String line = stats.get(i);
            if (line == null)
            {
                continue;
            }

            if (winner == Player.ATTACKER && line.startsWith(ATTACK_WIN_STR))
            {
                final int count = extractStatCount(line, ATTACK_WIN_STR);
                stats.set(i, ATTACK_WIN_STR + (count + INCREMENT));
            }
            else if (winner == Player.DEFENDER && line.startsWith(DEFEND_WIN_STR))
            {
                final int count = extractStatCount(line, DEFEND_WIN_STR);
                stats.set(i, DEFEND_WIN_STR + (count + INCREMENT));
            }
        }

        return stats;
    }

    /*
     * Extracts the integer value from a string that contains a stat count
     * by removing the specified prefix and parsing the remaining string to an integer.
     * If the parsing fails then return the DEFAULT_STAT_WINS
     *
     * @param line the string containing the stat.
     * @param prefix the prefix to remove from the beginning of the line before parsing the integer.
     * @return the extracted integer value, or DEFAULT_STAT_WINS if parsing fails.
     */
    private static int extractStatCount(final String line, final String prefix)
    {
        try
        {
            return Integer.parseInt(line.substring(prefix.length()).trim());
        }
        catch (NumberFormatException e)
        {
            return DEFAULT_STAT_WINS;
        }
    }

    /*
     * Validates that the winner is one of the players in the tablut game.
     * @param winner the winner to validate.
     */
    private static void validateWinner(final Player winner)
    {
        if (winner != Player.DEFENDER &&
            winner != Player.ATTACKER)
        {
            throw new IllegalArgumentException("Player is not a defender or attacker");
        }
    }

    /*
     * Validates that the stats is not null, contains the expected number of stats
     * and that the statistics themselves are valid.
     * @param stats the statistics to validate.
     */
    private static void validateStats(final List<String> stats)
    {
        final String stat1;
        final String stat2;

        if (stats == null)
        {
            throw new IllegalArgumentException("Stats cannot be null!");
        }

        if (stats.size() != EXPECTED_STATS_LEN)
        {
            throw new IllegalArgumentException("UNEXPECTED STATS LENGTH");
        }

        stat1 = stats.getFirst();
        stat2 = stats.get(SECOND_ELEMENT);

        validateStat(stat1);
        validateStat(stat2);
    }

    /*
     * Validates a single statistic to ensure that it
     * starts with one of the expected prefixes.
     * @param stat the stat to validate.
     */
    private static void validateStat(final String stat)
    {
        if (!(stat.startsWith(ATTACK_WIN_STR) ||
                stat.startsWith(DEFEND_WIN_STR)))
        {
            throw new IllegalArgumentException("Invalid stat");
        }
    }

    /*
     * Validates that a file name is not null or blank.
     * @param fileName the file name to validate.
     */
    private static void validateFileName(final String fileName)
    {
        if (fileName == null || fileName.isBlank())
        {
            throw new IllegalArgumentException("File name cannot be null or blank!");
        }
    }
}
