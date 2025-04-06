package ca.bcit.comp2522.termProject.myGame;

final class King extends Piece
{
    //todo check if singletonable

    private static King instance;

    private King()
    {
        super(Player.DEFENDER, true);
    }

    public static King getInstance()
    {
        if (instance == null)
        {
            instance = new King();
        }
        return instance;
    }
}
