package com.fenchtose.battleship

import com.fenchtose.battleship.models.gameplay.BasicGameUtils
import com.fenchtose.battleship.models.Direction
import com.fenchtose.battleship.models.gameplay.GameUtils
import com.fenchtose.battleship.models.Point
import com.fenchtose.battleship.models.Ship

import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals

class gamesetupTest {
    private lateinit var gameUtils: GameUtils

    @Before
    fun setup() {
        gameUtils = BasicGameUtils(TestLogger(), false)
    }

    @Test
    fun overlap1() {
        val s1 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        val s2 = Ship(3, Point(1, 0), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap2() {
        val s1 = Ship(3, Point(1, 1), Direction.VERTICAL)
        val s2 = Ship(3, Point(0, 1), Direction.HORIZONTAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap3() {
        val s1 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        val s2 = Ship(3, Point(1, 1), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap4() {
        val s1 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        val s2 = Ship(3, Point(3, 1), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap5() {
        val s1 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        val s2 = Ship(3, Point(3, 1), Direction.HORIZONTAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap6() {
        val s1 = Ship(3, Point(3, 1), Direction.HORIZONTAL)
        val s2 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    // -------------------
    // -------------------
    @Test
    fun overlap11() {
        val s1 = Ship(3, Point(1, 1), Direction.VERTICAL)
        val s2 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap12() {
        val s1 = Ship(3, Point(1, 1), Direction.VERTICAL)
        val s2 = Ship(3, Point(1, 1), Direction.HORIZONTAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap13() {
        val s1 = Ship(3, Point(1, 1), Direction.VERTICAL)
        val s2 = Ship(3, Point(1, 1), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap14() {
        val s1 = Ship(3, Point(1, 1), Direction.VERTICAL)
        val s2 = Ship(3, Point(1, 3), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }

    @Test
    fun overlap15() {
        val s1 = Ship(3, Point(1, 3), Direction.VERTICAL)
        val s2 = Ship(3, Point(1, 1), Direction.VERTICAL)
        assertEquals(true, gameUtils.isOverlap(s1, s2))
    }
}
