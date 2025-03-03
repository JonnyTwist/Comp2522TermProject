package ca.bcit.comp2522.termProject;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Used in the word game?
 */
class World
{
    private static final char[] files = { 'a', 'b', 'c', 'd', 'e', 'f',
                                    'g', 'h', 'i', 'j', 'k', 'l',
                                    'm', 'n', 'o', 'p', 'q', 'r',
                                    's', 't', 'u', 'v', 'y', 'z'
    };

    private static Map<String, Country> countries;

    static
    {
        countries = new HashMap<>();

        //todo figure out how to get rid of this error
        try
        {
            fillCountiesIntoMap(countries);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("ERROR!!! FILE NOT FOUND: " + ex);
        }

    }

    //todo create a method to get all the data and store in hashmap

    private static void fillCountiesIntoMap(final Map<String, Country> countries)
            throws FileNotFoundException
    {
        for (final char file : files)
        {
            final Scanner fileScanner;
            final String fileName;

            fileName = file + ".txt";

            fileScanner = new Scanner(new File(fileName));

            while (fileScanner.hasNext())
            {
                final String line;
                //final Scanner lineScanner;

                line = fileScanner.nextLine();
//                lineScanner = new Scanner(line);
//
//                if (line.contains(":"))
//                {
//                    lineScanner.useDelimiter(":");
//                }

                if (line.isBlank())
                {
                    continue;
                }
                else if (line.contains(":"))
                {
                    final String[] countryAndCapital;
                    countryAndCapital = line.split(":");
                    //todo I now have the country and cap
                }
                else
                {
                    //todo it is a clue thing
                }
            }
        }
    }

}
