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
    private static final int   IMAGE_PADDING_PX = 10;
    private static final Image ATTACKER_IMAGE;
    private static final Image DEFENDER_IMAGE;
    private static final Image KING_IMAGE;

    static
    {
        ATTACKER_IMAGE = new Image("images/attacker.png");
        DEFENDER_IMAGE = new Image("images/defender.png");
        KING_IMAGE = new Image("images/king.png");
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

    /**
     * A toString method that I use for debugging only.
     * @return the type of the piece.
     */
    @Override
    public String toString()
    {
        if (isKing)
        {
            return owner == Player.DEFENDER ? "D_K" : "A_K"; // Defender King, Attacker King (A_K is not real)
        }
        return owner == Player.DEFENDER ? "D" : "A"; // Defender, Attacker
    }

    /**
     * todo comment
     * @param piece
     * @return
     */
    //todo make non static maybe make hand in image path
    static ImageView getPieceImageView(final Piece piece)
    {
        ImageView imageView;

        imageView = null;

        if (piece != null) {
            if (piece.isKing()) {
                imageView = new ImageView(KING_IMAGE);
            } else if (piece.getOwner() == Player.DEFENDER) {
                imageView = new ImageView(DEFENDER_IMAGE);
            } else if (piece.getOwner() == Player.ATTACKER) {
                imageView = new ImageView(ATTACKER_IMAGE);
            }

            if (imageView != null) {
                // Make the image slightly smaller than the button
                imageView.setFitWidth(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);
                imageView.setFitHeight(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);
            }
        }

        return imageView;
    }
}
