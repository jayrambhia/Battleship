package com.fenchtose.battleship.models

data class Board(val width: Int, val height: Int) {
    val ships = ArrayList<Battleship>()

    fun fits(ship: Ship) = ship.start in this && ship.end in this

    fun isOverlap(ship: Ship): Boolean {
        return ships.any { ship in it.ship }
    }

    fun isOverlap(ship: Battleship): Boolean {
        return isOverlap(ship.ship)
    }

    operator fun contains(p: Point) = p.isValid() && p.col < width && p.row < height

}