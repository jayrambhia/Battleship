package com.fenchtose.battleship.models

data class Ship(val size: Int, val start: Point, val direction: Direction) {
    val position = Position(start, direction, size)
    val end = position.end

    // check for overlaps
    operator fun contains(other: Ship): Boolean {
        return other.position in position
    }

    operator fun contains(p: Point): Boolean {
        return p in position
    }
}