package ca.bcit.comp2522.termproject.wordgame;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class initializes and populates a map of countries with associated
 * capitals and facts by reading data from text files. It provides access to
 * this data for use in game rounds in word game.
 *
 * @author Jonny Twist
 * @version 1.0
 */
final class World
{
    private static final char[] files = { 'a', 'b', 'c', 'd', 'e', 'f',
                                    'g', 'h', 'i', 'j', 'k', 'l',
                                    'm', 'n', 'o', 'p', 'q', 'r',
                                    's', 't', 'u', 'v', 'y', 'z'
    };

    private static final int COUNTRY_INDEX = 0;
    private static final int CAPITAL_INDEX = 1;
    private static final int FACTS_PER_COUNTRY = 3;
    private static final int FIRST_FACT_INDEX = 0;
    private static final int SECOND_FACT_INDEX = 1;
    private static final int THIRD_FACT_INDEX = 2;

    static Map<String, Country> countries;
    static List<Country>        countriesValues;

    static
    {
        countries = new HashMap<>();

        try
        {
            fillCountiesIntoMap(countries);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("ERROR!!! FILE NOT FOUND: " + ex);
        }

        countriesValues = new ArrayList<>(countries.values());
    }

    /*
     * Reads country data from text files and populates the provided map with
     * country information, including country name, capital, and facts.
     * It processes each text file named from 'a.txt' to 'z.txt', where each
     * file contains data for multiple countries.
     *
     * @param countries the map to populate with country data.
     */
    private static void fillCountiesIntoMap(final Map<String, Country> countries)
            throws FileNotFoundException
    {
        for (final char file : files)
        {
            final Scanner fileScanner;
            final String fileName;

            fileName = "src/resources/" + file + ".txt";

            fileScanner = new Scanner(new File(fileName));

            while (fileScanner.hasNext())
            {
                final String countryAndCapitalOrBlank;

                countryAndCapitalOrBlank= fileScanner.nextLine();

                if (countryAndCapitalOrBlank.isBlank())
                {
                    continue;
                }

                final String[] countryAndCapital;
                final String[] facts;
                final String countryName;
                final String countryCapital;
                final String countryFact1;
                final String countryFact2;
                final String countryFact3;
                final Country countryToAdd;

                countryAndCapital = countryAndCapitalOrBlank.split(":");
                countryName = countryAndCapital[COUNTRY_INDEX];
                countryCapital = countryAndCapital[CAPITAL_INDEX];

                countryFact1 = fileScanner.nextLine();
                countryFact2 = fileScanner.nextLine();
                countryFact3 = fileScanner.nextLine();
                facts = new String[FACTS_PER_COUNTRY];
                facts[FIRST_FACT_INDEX] = countryFact1;
                facts[SECOND_FACT_INDEX] = countryFact2;
                facts[THIRD_FACT_INDEX] = countryFact3;

                countryToAdd = new Country(countryName, countryCapital, facts);

                countries.put(countryName, countryToAdd);
            }

            fileScanner.close();
        }
    }
}
