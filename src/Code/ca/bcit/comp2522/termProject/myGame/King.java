package ca.bcit.comp2522.termProject.myGame;

/**
 * Allows creation of a King.
 * In tablut there is only one king so it can be a singleton.
 * @author Jonny Twist
 * @version 1.0
 */
final class King extends Piece
{
    private static King instance;

    private King()
    {
        super(Player.DEFENDER, true);
    }

    static King getInstance()
    {
        if (instance == null)
        {
            instance = new King();
        }
        return instance;
    }

    /**
     * toString mainly for debugging but also works in
     * emergencies if the king image fails to load.
     * @return the owner and piece type.
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
