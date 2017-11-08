package com.fenchtose.battleship.models

class Battleship(val ship: Ship) {
    val hits: ArrayList<Point> = ArrayList(ship.size)
    var destroyed: Boolean = false

    override fun toString(): String {
        return "Battleship(ship=$ship, destroyed=$destroyed)"
    }

}