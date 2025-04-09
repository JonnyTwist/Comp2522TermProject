package ca.bcit.comp2522.termproject.tablut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

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
    void testConstructorValidation()
    {
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class, () -> new Pawn(null));

        assertEquals("Pieces must have owners", exception1.getMessage());
    }

    @Test
    void testGetOwner()
    {
        assertEquals(Player.DEFENDER, p1.getOwner());
        assertEquals(Player.ATTACKER, p2.getOwner());
        assertEquals(Player.DEFENDER, p3.getOwner());

        assertNotSame(Player.ATTACKER, p1.getOwner());
        assertNotSame(Player.DEFENDER, p2.getOwner());
        assertNotSame(Player.ATTACKER, p3.getOwner());
    }

    @Test
    void testIsKing()
    {
        assertFalse(p1.isKing());
        assertFalse(p2.isKing());
        assertTrue(p3.isKing());
    }

    @Test
    void testToString()
    {
        assertEquals("D", p1.toString());
        assertEquals("A", p2.toString());
        assertEquals("DK", p3.toString());
    }
}