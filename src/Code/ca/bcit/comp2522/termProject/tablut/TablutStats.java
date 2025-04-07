package ca.bcit.comp2522.termproject.tablut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class TablutStats
{
    private static final int DEFAULT_STAT_WINS  = 0;
    private static final int EXPECTED_STATS_LEN = 2;
    private static final int FIRST_ELEMENT      = 0;
    private static final int SECOND_ELEMENT     = 1;

    private static final String ATTACK_WIN_STR = "Attacker Wins: ";
    private static final String DEFEND_WIN_STR = "Defender Wins: ";


    static List<String> readStats(final String fileName)
    {
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
                            .filter(Objects::nonNull)
                            .filter(line-> line.startsWith(ATTACK_WIN_STR) ||
                                    line.startsWith(DEFEND_WIN_STR))
                            .toList();
        }
        catch (final IOException ex)
        {
            System.out.println("Error reading the file: " + ex);
            stats.clear();
            stats.add(ATTACK_WIN_STR + DEFAULT_STAT_WINS);
            stats.add(DEFEND_WIN_STR + DEFAULT_STAT_WINS);
        }

        validateFileName(fileName);
        return stats;
    }

    static void writeStats(final int[] stats, final String fileName)
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
                    StandardOpenOption.WRITE
            );
        }
        catch(final IOException e)
        {
            System.out.println("error writing stats to file: " + e.getMessage());
        }
    }

    private static void validateStats(final int[] stats)
    {
        if (stats.length != EXPECTED_STATS_LEN)
        {
            throw new IllegalArgumentException("UNEXPECTED STATS LENGTH");
        }
    }

    private static void validateFileName(final String fileName)
    {
        if (fileName == null || fileName.isBlank())
        {
            throw new IllegalArgumentException("File name cannot be null or blank!");
        }
    }

    private static String formatOutput(final int[] stats)
    {
        validateStats(stats);

        final StringBuilder output;
        final int attackerWinCount;
        final int defenderWinCount;

        output = new StringBuilder();
        attackerWinCount = stats[FIRST_ELEMENT];
        defenderWinCount = stats[SECOND_ELEMENT];

        output.append(ATTACK_WIN_STR)
                .append(attackerWinCount)
                .append(System.lineSeparator())
                .append(DEFEND_WIN_STR)
                .append(defenderWinCount);

        return output.toString();
    }
}
