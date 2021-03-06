package com.fenchtose.battleship

import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.models.Board.Companion.testBoard
import org.junit.Assert.*

import org.junit.Test

class ModelTests {

    @Test
    fun isPointEqual() {
        assertEquals(Point(4, 2) == Point(4, 2), true)
        assertEquals(Point(4, 1), Point(4, 1))
    }

    @Test
    fun isPointEDifferent() {
        assertNotEquals(Point(3, 1), Point(4, 1))
    }

    @Test
    fun checkPointOperators() {
        assertEquals("direction times op", Direction.HORIZONTAL * 3, WeighedDirection(Direction.HORIZONTAL, 3))
        assertEquals("end point", Point(1, 5), Point(1,3)+ WeighedDirection(Direction.VERTICAL, 2))
    }

    @Test
    fun pointOnHorizontalShip() {
        val ship1 = Ship(1, 3, Point(2,1), Direction.HORIZONTAL)
        assertTrue(Point(2, 1) in ship1)
        assertTrue(Point(3, 1) in ship1)
        assertTrue(Point(4, 1) in ship1)
        assertTrue(Point(5, 1) !in ship1)
        assertTrue(Point(1, 1) !in ship1)
        assertTrue("point not on ship", Point(5, 1) !in ship1)
        assertTrue("point not on ship", Point(2, 2) !in ship1)
        assertTrue(Point(3, 2) !in ship1)
    }

    @Test
    fun pointOnVerticalShip() {
        val ship1 = Ship(1,3, Point(2,1), Direction.VERTICAL)
        assertTrue(Point(2, 1) in ship1)
        assertTrue(Point(2, 2) in ship1)
        assertTrue(Point(2, 3) in ship1)
        assertTrue(Point(2, 4) !in ship1)
        assertTrue(Point(1, 1) !in ship1)
        assertTrue("point not on ship", Point(1, 5) !in ship1)
        assertTrue("point not on ship", Point(1, 2) !in ship1)
        assertTrue(Point(3, 2) !in ship1)
    }

    @Test
    fun pointOnBoard() {
        val b = testBoard(8, 6)
        assertTrue("on board", Point(2, 3) in b)
        assertTrue("not on board", Point.None() !in b)
        assertTrue("not on board", Point(8,6) !in b)
        assertTrue("not on board", Point(7,6) !in b)
        assertTrue("not on board", Point(8,5) !in b)
        assertTrue("on board", Point(7,5) in b)
        assertTrue("on board", Point(0,0) in b)
    }

    @Test
    fun shipOnBoard() {
        val b = testBoard(8, 6)
        assertTrue("fits on board", b.fits(Ship(1,3, Point(2, 2), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(2,3, Point(2, 2), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(3,3, Point(0, 0), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(4,3, Point(0, 0), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(5,3, Point(2, 3), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(6,3, Point(5, 3), Direction.HORIZONTAL)))

        assertTrue("!fits on board", !b.fits(Ship(7,3, Point(-1, -2), Direction.VERTICAL)))
        assertTrue("!fits on board", !b.fits(Ship(8,3, Point(-2, -1), Direction.HORIZONTAL)))
        assertTrue("!fits on board", !b.fits(Ship(9,3, Point(2, 4), Direction.VERTICAL)))
        assertTrue("!fits on board", !b.fits(Ship(10,3, Point(6, 3), Direction.HORIZONTAL)))

        assertTrue("fits on board", b.fits(Ship(11,1, Point(0, 0), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(12,1, Point(0, 0), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(13,1, Point(7, 5), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(14,1, Point(7, 5), Direction.VERTICAL)))

        assertTrue("!fits on board", !b.fits(Ship(15,1, Point(8, 5), Direction.HORIZONTAL)))
        assertTrue("!fits on board", !b.fits(Ship(16,1, Point(7, 6), Direction.VERTICAL)))

    }

    @Test
    fun sameDirectionOverlapTest() {
        val h = Ship(1,3, Point(3, 1), Direction.HORIZONTAL)
        val h1 = Ship(2,3, Point(3, 1), Direction.HORIZONTAL)
        val h2 = Ship(3,3, Point(1, 1), Direction.HORIZONTAL)
        val h3 = Ship(4,3, Point(5, 1), Direction.HORIZONTAL)
        val h4 = Ship(5,2, Point(0, 1), Direction.HORIZONTAL)
        val h5 = Ship(6,2, Point(6, 1), Direction.HORIZONTAL)
        val h6 = Ship(7,3, Point(3, 2), Direction.HORIZONTAL)

        val v = Ship(8,3, Point(3, 3), Direction.VERTICAL)
        val v1 = Ship(9,3, Point(3, 3), Direction.VERTICAL)
        val v2 = Ship(10,3, Point(3, 1), Direction.VERTICAL)
        val v3 = Ship(11,3, Point(3, 5), Direction.VERTICAL)
        val v4 = Ship(12,3, Point(3, 0), Direction.VERTICAL)
        val v5 = Ship(13,3, Point(3, 6), Direction.VERTICAL)
        val v6 = Ship(14,3, Point(4, 3), Direction.VERTICAL)

        assertTrue("overlaps", h1 in h)
        assertTrue("overlaps", h2 in h)
        assertTrue("overlaps", h3 in h)
        assertTrue("overlaps", h4 !in h)
        assertTrue("!overlaps", h5 !in h)
        assertTrue("!overlaps", h6 !in h)

        assertTrue("overlaps", v1 in v)
        assertTrue("overlaps", v2 in v)
        assertTrue("overlaps", v3 in v)
        assertTrue("overlaps", v4 !in v)
        assertTrue("!overlaps", v5 !in v)
        assertTrue("!overlaps", v6 !in v)
    }

    @Test
    fun differentDirectionOverlapTest() {
        // perfect overlap
        val h1 = Ship(1,3, Point(2,2), Direction.HORIZONTAL)
        val v1 = Ship(2,3, Point(3,1), Direction.VERTICAL)
        assertTrue("overlaps", h1 in v1)
        assertTrue("overlaps", v1 in h1)

        // vertical just touching from top overlap
        val h2 = Ship(3,3, Point(2,2), Direction.HORIZONTAL)
        val v2 = Ship(4,3, Point(3,0), Direction.VERTICAL)
        assertTrue("overlaps", h2 in v2)
        assertTrue("overlaps", v2 in h2)

        // vertical just touching from bottom overlap
        val h3 = Ship(5,3, Point(2,2), Direction.HORIZONTAL)
        val v3 = Ship(6,3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h3 in v3)
        assertTrue("overlaps", v3 in h3)

        // vertical just missing from top overlap
        val h4 = Ship(7,3, Point(2,2), Direction.HORIZONTAL)
        val v4 = Ship(8,2, Point(3,0), Direction.VERTICAL)
        assertTrue("!overlaps", h4 !in v4)
        assertTrue("!overlaps", v4 !in h4)

        // vertical just missing from bottom overlap
        val h5 = Ship(9,3, Point(2,2), Direction.HORIZONTAL)
        val v5 = Ship(10,3, Point(3,3), Direction.VERTICAL)
        assertTrue("!overlaps", h5 !in v5)
        assertTrue("!overlaps", v5 !in h5)

    }

    @Test
    fun differentDirectionOverlapTest2() {
        // perfect !overlap
        val h1 = Ship(1,3, Point(2,2), Direction.HORIZONTAL)
        val v1 = Ship(2,3, Point(3,3), Direction.VERTICAL)
        assertTrue("!overlaps", h1 !in v1)
        assertTrue("!overlaps", v1 !in h1)

        // horizontal just touching from left overlap
        val h2 = Ship(3,3, Point(1,3), Direction.HORIZONTAL)
        val v2 = Ship(4,3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h2 in v2)
        assertTrue("overlaps", v2 in h2)

        // horizontal just touching from right overlap
        val h3 = Ship(5,3, Point(3,3), Direction.HORIZONTAL)
        val v3 = Ship(6,3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h3 in v3)
        assertTrue("overlaps", v3 in h3)

        // horizontal just missing from left overlap
        val h4 = Ship(7,2, Point(1,3), Direction.HORIZONTAL)
        val v4 = Ship(8,3, Point(3,2), Direction.VERTICAL)
        assertTrue("!overlaps", h4 !in v4)
        assertTrue("!overlaps", v4 !in h4)

        // horizontal just missing from right overlap
        val h5 = Ship(9,3, Point(4,2), Direction.HORIZONTAL)
        val v5 = Ship(10,3, Point(3,2), Direction.VERTICAL)
        assertTrue("!overlaps", h5 !in v5)
        assertTrue("!overlaps", v5 !in h5)

    }
}
