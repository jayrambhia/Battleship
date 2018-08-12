package com.fenchtose.battleship.models

data class Ship(
        val id: Int,
        val size: Int, val start: Point, val direction: Direction,
        val hits: Set<Point> = setOf()) {

    val end = start + direction*(size-1)
    val destroyed = hits.size == size

    // check for overlaps
    operator fun contains(other: Ship): Boolean {
        if (other.direction == direction) {
            return other.start in this || other.end in this
        }

        val vertical = if (other.direction == Direction.VERTICAL) other else this
        val horizontal = if (other.direction == Direction.HORIZONTAL) other else this

        return horizontal.start.row in vertical.start.row..vertical.end.row
                && vertical.start.col in horizontal.start.col..horizontal.end.col
    }

    operator fun contains(p: Point): Boolean {
        return when(direction) {
            Direction.HORIZONTAL -> start.row == p.row && start.col <= p.col && end.col >= p.col
            Direction.VERTICAL -> start.col == p.col && start.row <= p.row && end.row >= p.row
        }
    }

}