package com.fenchtose.battleship.models

data class Battleship(val ship: Ship) {
    val hits: ArrayList<Point> = ArrayList(ship.size)
    var destroyed: Boolean = false
}