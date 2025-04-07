package ca.bcit.comp2522.termProject.myGame;

/**
 * Allows creation of Pawns.
 * @author Jonny Twist
 * @version 1.0
 */
final class Pawn extends Piece
{
    Pawn(final Player owner)
    {
        super(owner);
    }

    /**
     * toString mainly for debugging but also works
     * in emergencies when the image fails to load.
     * @return the owner of the pawn.
     */
    @Override
    public String toString()
    {
        if (getOwner() == Player.DEFENDER)
        {
            return "D";
        }
        else if (getOwner() == Player.ATTACKER)
        {
            return "A";
        }
        else
        {
            return "Pawn of unknown player";
        }
    }
}
