package ca.bcit.comp2522.termProject.wordGame;

/**
 * Creates country objects to store the name, capital's name and facts about
 * the country.
 * Used in the word game.
 * @author Jonny Twist
 * @version 1.0
 */
class Country
{

    static final int REQUIRED_NUM_FACTS = 3;

    /*
    String name (e.g. Canada)
    String capitalCityName (e.g. Ottawa)
    Array (not ArrayList) facts; for example:
    0: Home to the longest coastline in the world.
    1: Famous for its maple syrup production, accounting for 71% of the world's supply.
    2: One of the most multicultural nations in the world, with more than 200 ethnic
    origins represented.
     */
    private final String name;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * todo maybe change to allow more facts about the countries
     * Constructor for country objects.
     * @param name the name of the country.
     * @param capitalCityName the capital of the country.
     * @param facts three facts about the country.
     */
    Country(final String name,
            final String capitalCityName,
            final String[] facts)
    {
        validateCountryName(name);
        validateCountryCapitalName(capitalCityName);
        validateFacts(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /*
     * Validates that the country name is not null or blank.
     * @param name the name to validate.
     */
    private static void validateCountryName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Country names cannot be null or blank!");
        }
    }

    /*
     * Validates that the capital city name is not null or blank.
     * @param capitalCityName the capital name to validate.
     */
    private static void validateCountryCapitalName(final String capitalCityName)
    {
        if (capitalCityName == null || capitalCityName.isBlank())
        {
            throw new IllegalArgumentException("Capital names cannot be null or blank!");
        }
    }

    /*
     * validates that the counties facts are not null and that it has exactly three facts.
     * @param facts the facts array to be validated.
     */
    private static void validateFacts(final String[] facts)
    {
        if (facts == null || facts.length != REQUIRED_NUM_FACTS)
        {
            throw new IllegalArgumentException("Facts cannot be null and must " +
                                               "contain " + REQUIRED_NUM_FACTS + " facts!");
        }
    }

    /**
     * Getter for the name of the country.
     * @return the name of the country.
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Getter for the capital of the country.
     * @return the capital of the country.
     */
    public final String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Getter for the country's facts.
     * @return the facts array.
     */
    public final String[] getFacts()
    {
        return facts;
    }
}
