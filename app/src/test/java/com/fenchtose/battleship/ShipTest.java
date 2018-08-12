package com.fenchtose.battleship;

import com.fenchtose.battleship.models.Direction;
import com.fenchtose.battleship.models.Point;
import com.fenchtose.battleship.models.Ship;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTest {

	private Ship ship;

	/*@Before
	public void setup() {
		ship = new Ship(3, new Point(1, 1), Direction.HORIZONTAL);
		battleship = new Battleship(ship);
		game = new BasicGameUtils(null, false);
	}

	@Test
	public void hitShip() {
		boolean hit = game.hit(battleship, new Point(2, 1));
		assertEquals(true, hit);
		assertEquals(1, battleship.getHits().size());
		assertEquals(new Point(2, 1), battleship.getHits().get(0));
		assertEquals(false, battleship.getDestroyed());
	}

	@Test
	public void misShip() {
		boolean hit = game.hit(battleship, new Point(1, 2));
		assertEquals(false, hit);
		assertEquals(0, battleship.getHits().size());
		assertEquals(false, battleship.getDestroyed());
	}

	@Test
	public void destroyShip() {
		game.hit(battleship, new Point(1, 1));
		game.hit(battleship, new Point(2, 1));
		game.hit(battleship, new Point(3, 1));
		assertEquals(3, battleship.getHits().size());
		assertEquals(true, battleship.getDestroyed());
	}*/
}
