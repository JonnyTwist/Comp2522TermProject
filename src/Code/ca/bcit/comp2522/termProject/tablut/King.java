package ca.bcit.comp2522.termproject.tablut;

/**
 * Allows creation of a King.
 * In tablut there is only one king so it can be a singleton.
 * @author Jonny Twist
 * @version 1.0
 */
final class King extends Piece
{
    private static King instance;

    /*
     * Private constructor to prevent instantiation from other classes.
     * The constructor ensures that the King is always created as a defender piece
     * and is set to be a king according to Piece.
     */
    private King()
    {
        super(Player.DEFENDER, true);
    }

    /**
     * Retrieves the unique instance of the King.
     * If the instance doesn't already exist, it is created.
     *
     * @return the single instance of the King.
     */
    static King getInstance()
    {
        if (instance == null)
        {
            instance = new King();
        }
        return instance;
    }

    /**
     * Returns a string representation of the King piece,
     * useful for debugging purposes or as a fallback if the image fails to load.
     * The string includes the ownership of the King (Defender or Attacker).
     *
     * @return a string representing the King, including its owner.
     */
    @Override
    public String toString()
    {
        if (getOwner() == Player.DEFENDER)
        {
            return "DK";
        }
        else if (getOwner() == Player.ATTACKER)
        {
            return "AK";
        }
        else
        {
            return "King of unknown player";
        }
    }
}
