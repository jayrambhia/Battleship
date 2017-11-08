package com.fenchtose.battleship;

import com.fenchtose.battleship.models.gameplay.BasicGameUtils;
import com.fenchtose.battleship.models.Direction;
import com.fenchtose.battleship.models.gameplay.GameUtils;
import com.fenchtose.battleship.models.Point;
import com.fenchtose.battleship.models.Ship;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class gamesetupTest {
	private GameUtils gameUtils;

	@Before
	public void setup() {
		gameUtils = new BasicGameUtils(null);
	}

	@Test
	public void overlap1() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		Ship s2 = new Ship(3, new Point(1, 0), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap2() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(0, 1), Direction.HORIZONTAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap3() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap4() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		Ship s2 = new Ship(3, new Point(3, 1), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap5() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		Ship s2 = new Ship(3, new Point(3, 1), Direction.HORIZONTAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap6() {
		Ship s1 = new Ship(3, new Point(3, 1), Direction.HORIZONTAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}
	// -------------------
	// -------------------
	@Test
	public void overlap11() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap12() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap13() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap14() {
		Ship s1 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(1, 3), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}

	@Test
	public void overlap15() {
		Ship s1 = new Ship(3, new Point(1, 3), Direction.VERTICAL);
		Ship s2 = new Ship(3, new Point(1, 1), Direction.VERTICAL);
		assertEquals(true, gameUtils.isOverlap(s1, s2));
	}
}
