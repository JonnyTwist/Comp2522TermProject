package ca.bcit.comp2522.termProject.myGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest
{

    private Piece p1;
    private Piece p2;
    private Piece p3;


    @BeforeEach
    void setUp()
    {
        p1 = new Pawn(Player.DEFENDER);
        p2 = new Pawn(Player.ATTACKER);
        p3 = King.getInstance();
    }

    @Test
    void getOwner()
    {
        assertEquals(Player.DEFENDER, p1.getOwner());
        assertEquals(Player.ATTACKER, p2.getOwner());
        assertEquals(Player.DEFENDER, p3.getOwner());
    }

    @Test
    void isKing()
    {
    }

    @Test
    void testToString()
    {
    }
}