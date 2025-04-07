package ca.bcit.comp2522.termProject.myGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An abstract class to define what all pieces have.
 * @author Jonny Twist
 * @version 1.0
 */
abstract class Piece
{
    private static final int IMAGE_PADDING_PX = 10;
    private static Image attackerImage;
    private static Image defenderImage;
    private static Image kingImage;

    static
    {
        initializeImages();
    }

    private final Player owner;
    private final boolean isKing;

    /**
     * Constructor for a Piece object.
     * @param owner the owner of the piece.
     * @param isKing if the piece is the king or not.
     */
    Piece(final Player owner,
          final boolean isKing)
    {
        validateOwner(owner);

        this.owner = owner;
        this.isKing = isKing;
    }

    /**
     * Constructor for a piece that does not pass if it is a king.
     * Assumes the piece is not a king in that case.
     * @param owner the owner of the piece.
     */
    Piece(final Player owner)
    {
        this(owner, false);
    }

    /*
     * Validates that all pieces have an owner.
     * @param owner the owner to validate.
     */
    private static void validateOwner(final Player owner)
    {
        if (owner == null)
        {
            throw new IllegalArgumentException("Pieces must have owners");
        }
    }

    /**
     * Getter for the owner.
     * @return the owner.
     */
    public final Player getOwner()
    {
        return owner;
    }

    /**
     * Getter for if the piece is the king.
     * @return if the piece is the king return true. Else false.
     */
    public final boolean isKing()
    {
        return isKing;
    }

    /*
     * Tries to create the images and if it cant find them
     * makes the images null.
     */
    private static void initializeImages()
    {
        try
        {
            attackerImage = new Image("images/attacker.png");
        }
        catch (final Exception ex)
        {
            attackerImage = null;
        }
        try
        {
            defenderImage = new Image("images/defender.png");
        }
        catch (final Exception ex)
        {
            defenderImage = null;
        }
        try
        {
            kingImage = new Image("images/king.png");
        }
        catch (final Exception ex)
        {
            kingImage = null;
        }
    }

    /**
     * A toString method that I use for debugging only.
     * @return the type of the piece.
     */
    @Override
    public String toString()
    {
        if (isKing)
        {
            // Defender King, Attacker King (A_K is not real in tablut)
            if (owner == Player.DEFENDER)
            {
                return "D_K";
            }
            else
            {
                return "A_K";
            }
        }
        // Defender, Attacker
        if (owner == Player.DEFENDER)
        {
            return "D";
        }
        else
        {
            return "A";
        }
    }

    /**
     * Gets the image that corresponds with the piece.
     * If the piece does not have an image or the image failed to load
     * return null so that the other class can handle it,
     * @param piece the piece to get the image of.
     * @return the image of the piece.
     */
    static ImageView getPieceImageView(final Piece piece)
    {
        if (piece == null)
        {
            return null;
        }

        final ImageView imageView;
        Image image;

        image = null;

        if (piece.isKing())
        {
            image = kingImage;
        }
        else if (piece.getOwner() == Player.DEFENDER)
        {
            image = defenderImage;
        }
        else if (piece.getOwner() == Player.ATTACKER)
        {
            image = attackerImage;
        }

        // image file failed to load earlier
        // let it be handled by the class we return to
        if (image == null)
        {
            return null;
        }

        imageView = new ImageView(image);

        //make the image slightly smaller than the button
        imageView.setFitWidth(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);
        imageView.setFitHeight(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);

        return imageView;
    }
}
