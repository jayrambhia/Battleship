package com.fenchtose.battleship

import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.models.gameplay.BasicGameUtils
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
        assertEquals("point sum", Point(3, 1),Point(2, 1) + Direction.HORIZONTAL)
        assertEquals("end point", Point(1, 5), Point(1,3).getEndPoint(3, Direction.VERTICAL))
    }

    @Test
    fun pointOnHorizontalShip() {
        val ship1 = Ship(3, Point(2,1), Direction.HORIZONTAL)
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
        val ship1 = Ship(3, Point(2,1), Direction.VERTICAL)
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
    fun checkBasicGameUtils() {
        val b = Board(8, 6)
        val ships = arrayListOf(
                Ship(3, Point(3,3), Direction.VERTICAL),
                Ship(4, Point(3, 3), Direction.VERTICAL),
                Ship(4, Point(3, 4), Direction.HORIZONTAL),
                Ship(1, Point(1, 2), Direction.VERTICAL),
                Ship(1, Point(3, 2), Direction.HORIZONTAL),
                Ship(1, Point(1, 2), Direction.VERTICAL),
                Ship(2, Point(3, 2), Direction.HORIZONTAL),
                Ship(3, Point(1, 2), Direction.VERTICAL),
                Ship(4, Point(5, 6), Direction.HORIZONTAL),
                Ship(2, Point(2, 4), Direction.VERTICAL),
                Ship(3, Point(3, 4), Direction.HORIZONTAL),
                Ship(1, Point(6, 2), Direction.VERTICAL),
                Ship(2, Point(5, 1), Direction.HORIZONTAL),
                Ship(3, Point(5, 1), Direction.VERTICAL),
                Ship(4, Point(5, 2), Direction.HORIZONTAL)
                )

        for (i in 1..ships.size) {
            for (j in 1..ships.size) {
//                assertEquals("${i-1}, ${j-1}", ships[i-1] in ships[j-1], BasicGameUtils.checkOverlap(ships[i-1], ships[j-1]))
            }
        }
    }

    @Test
    fun pointOnBoard() {
        val b = Board(8, 6)
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
        val b = Board(8, 6)
        assertTrue("fits on board", b.fits(Ship(3, Point(2, 2), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(3, Point(2, 2), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(3, Point(0, 0), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(3, Point(0, 0), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(3, Point(2, 3), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(3, Point(5, 3), Direction.HORIZONTAL)))

        assertTrue("!fits on board", !b.fits(Ship(3, Point(-1, -2), Direction.VERTICAL)))
        assertTrue("!fits on board", !b.fits(Ship(3, Point(-2, -1), Direction.HORIZONTAL)))
        assertTrue("!fits on board", !b.fits(Ship(3, Point(2, 4), Direction.VERTICAL)))
        assertTrue("!fits on board", !b.fits(Ship(3, Point(6, 3), Direction.HORIZONTAL)))

        assertTrue("fits on board", b.fits(Ship(1, Point(0, 0), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(1, Point(0, 0), Direction.VERTICAL)))
        assertTrue("fits on board", b.fits(Ship(1, Point(7, 5), Direction.HORIZONTAL)))
        assertTrue("fits on board", b.fits(Ship(1, Point(7, 5), Direction.VERTICAL)))

        assertTrue("!fits on board", !b.fits(Ship(1, Point(8, 5), Direction.HORIZONTAL)))
        assertTrue("!fits on board", !b.fits(Ship(1, Point(7, 6), Direction.VERTICAL)))

    }

    @Test
    fun sameDirectionOverlapTest() {
        val h = Ship(3, Point(3, 1), Direction.HORIZONTAL)
        val h1 = Ship(3, Point(3, 1), Direction.HORIZONTAL)
        val h2 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        val h3 = Ship(3, Point(5, 1), Direction.HORIZONTAL)
        val h4 = Ship(2, Point(0, 1), Direction.HORIZONTAL)
        val h5 = Ship(2, Point(6, 1), Direction.HORIZONTAL)
        val h6 = Ship(3, Point(3, 2), Direction.HORIZONTAL)

        val v = Ship(3, Point(3, 3), Direction.VERTICAL)
        val v1 = Ship(3, Point(3, 3), Direction.VERTICAL)
        val v2 = Ship(3, Point(3, 1), Direction.VERTICAL)
        val v3 = Ship(3, Point(3, 5), Direction.VERTICAL)
        val v4 = Ship(3, Point(3, 0), Direction.VERTICAL)
        val v5 = Ship(3, Point(3, 6), Direction.VERTICAL)
        val v6 = Ship(3, Point(4, 3), Direction.VERTICAL)

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
        val h1 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v1 = Ship(3, Point(3,1), Direction.VERTICAL)
        assertTrue("overlaps", h1 in v1)
        assertTrue("overlaps", v1 in h1)

        // vertical just touching from top overlap
        val h2 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v2 = Ship(3, Point(3,0), Direction.VERTICAL)
        assertTrue("overlaps", h2 in v2)
        assertTrue("overlaps", v2 in h2)

        // vertical just touching from bottom overlap
        val h3 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v3 = Ship(3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h3 in v3)
        assertTrue("overlaps", v3 in h3)

        // vertical just missing from top overlap
        val h4 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v4 = Ship(2, Point(3,0), Direction.VERTICAL)
        assertTrue("!overlaps", h4 !in v4)
        assertTrue("!overlaps", v4 !in h4)

        // vertical just missing from bottom overlap
        val h5 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v5 = Ship(3, Point(3,3), Direction.VERTICAL)
        assertTrue("!overlaps", h5 !in v5)
        assertTrue("!overlaps", v5 !in h5)

    }

    @Test
    fun differentDirectionOverlapTest2() {
        // perfect !overlap
        val h1 = Ship(3, Point(2,2), Direction.HORIZONTAL)
        val v1 = Ship(3, Point(3,3), Direction.VERTICAL)
        assertTrue("!overlaps", h1 !in v1)
        assertTrue("!overlaps", v1 !in h1)

        // horizontal just touching from left overlap
        val h2 = Ship(3, Point(1,3), Direction.HORIZONTAL)
        val v2 = Ship(3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h2 in v2)
        assertTrue("overlaps", v2 in h2)

        // horizontal just touching from right overlap
        val h3 = Ship(3, Point(3,3), Direction.HORIZONTAL)
        val v3 = Ship(3, Point(3,2), Direction.VERTICAL)
        assertTrue("overlaps", h3 in v3)
        assertTrue("overlaps", v3 in h3)

        // horizontal just missing from left overlap
        val h4 = Ship(2, Point(1,3), Direction.HORIZONTAL)
        val v4 = Ship(3, Point(3,2), Direction.VERTICAL)
        assertTrue("!overlaps", h4 !in v4)
        assertTrue("!overlaps", v4 !in h4)

        // horizontal just missing from right overlap
        val h5 = Ship(3, Point(4,2), Direction.HORIZONTAL)
        val v5 = Ship(3, Point(3,2), Direction.VERTICAL)
        assertTrue("!overlaps", h5 !in v5)
        assertTrue("!overlaps", v5 !in h5)

    }
}
