package ca.bcit.comp2522.termProject.myGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private final TablutSpinoff.Player owner;
    private final boolean              isKing;

    /**
     * Constructor for a Piece object.
     * @param owner the owner of the piece.
     * @param isKing if the piece is the king or not.
     */
    Piece(final TablutSpinoff.Player owner,
          final boolean isKing) {
        validateOwner(owner);

        this.owner = owner;
        this.isKing = isKing;
    }

    private static void validateOwner(final TablutSpinoff.Player owner)
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
    public final TablutSpinoff.Player getOwner() {
        return owner;
    }

    /**
     * Getter for if the piece is the king.
     * @return if the piece is the king return true. Else false.
     */
    public final boolean isKing() {
        return isKing;
    }

    /**
     * A toString method that I use for debugging.
     * @return the type of the piece.
     */
    @Override
    public String toString() {
        if (isKing) {
            return owner == TablutSpinoff.Player.DEFENDER ? "D_K" : "A_K"; // Defender King, Attacker King (A_K is not real)
        }
        return owner == TablutSpinoff.Player.DEFENDER ? "D" : "A"; // Defender, Attacker
    }

    /*
     * todo comment
     * @param piece
     * @return
     */
    static ImageView getPieceImageView(final Piece piece) {
        ImageView imageView;

        imageView = null;

        if (piece != null) {
            if (piece.isKing()) {
                imageView = new ImageView(KING_IMAGE);
            } else if (piece.getOwner() == TablutSpinoff.Player.DEFENDER) {
                imageView = new ImageView(DEFENDER_IMAGE);
            } else if (piece.getOwner() == TablutSpinoff.Player.ATTACKER) {
                imageView = new ImageView(ATTACKER_IMAGE);
            }

            if (imageView != null) {
                imageView.setFitWidth(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);  // Slightly smaller than button
                imageView.setFitHeight(TablutSpinoff.BTN_SIZE_PX - IMAGE_PADDING_PX);
            }
        }

        return imageView;
    }
}
