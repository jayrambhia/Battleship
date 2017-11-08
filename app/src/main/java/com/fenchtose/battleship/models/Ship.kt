package com.fenchtose.battleship.models

class Ship(val size: Int, val start: Point, val direction: Direction) {
    val end = start.getEndPoint(size, direction)
    override fun toString(): String {
        return "Ship(size=$size, start=$start, direction=$direction)"
    }
}