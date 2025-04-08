package ca.bcit.comp2522.termproject.tablut;

/**
 * Represents a Pawn piece in the game of Tablut.
 * The Pawn class allows the creation of Pawns, which can be owned by either the attacker or the defender.
 * Pawns are the standard pieces used in the game and move according to the rules defined in the game.
 *
 * @author Jonny Twist
 * @version 1.0
 */
final class Pawn extends Piece
{
    /**
     * Constructor to create a new Pawn piece.
     * This constructor initializes the Pawn's owner, which is provided as a parameter.
     *
     * @param owner the player who owns this pawn.
     */
    Pawn(final Player owner)
    {
        super(owner);
    }

    /**
     * Returns a string representation of the Pawn piece.
     * This method is mainly used for debugging purposes, and it also serves as a fallback
     * if the image associated with the Pawn fails to load.
     * The returned string reflects the owner of the Pawn piece.
     *
     * @return a string indicating the owner of the Pawn ("D" for Defender, "A" for Attacker).
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
