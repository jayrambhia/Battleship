package com.fenchtose.battleship.models

class Board(val width: Int, val height: Int) {
    val ships = ArrayList<Battleship>()

    override fun toString(): String {
        return "Board(width=$width, height=$height)"
    }

}