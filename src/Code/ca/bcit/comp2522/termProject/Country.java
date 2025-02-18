package ca.bcit.comp2522.termProject;

/**
 * Used in the word game
 */
class Country
{

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

    private static void validateCountryName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Country names cannot be null or blank!");
        }
    }

    private static void validateCountryCapitalName(final String capitalCityName)
    {
        if (capitalCityName == null || capitalCityName.isBlank())
        {
            throw new IllegalArgumentException("Capital names cannot be null or blank!");
        }
    }

    private static void validateFacts(final String[] facts)
    {
        if (facts == null || facts.length == 0)
        {
            throw new IllegalArgumentException("Facts cannot be null and must have at least one fact!");
        }
    }

    public final String getName()
    {
        return name;
    }

    public final String getCapitalCityName()
    {
        return capitalCityName;
    }

    public final String[] getFacts()
    {
        return facts;
    }
}
