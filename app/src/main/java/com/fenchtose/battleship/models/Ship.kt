package com.fenchtose.battleship.models

import com.fenchtose.battleship.redux.Action

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

fun List<Ship>.contains(p: Point): Boolean {
    forEach {
        if (p in it) {
            return true
        }
    }

    return false
}

fun List<Ship>.getShipDirection(p: Point): Direction? {
    forEach {
        if (p in it) {
            return it.direction
        }
    }

    return null
}

fun List<Ship>.update(ship: Ship): List<Ship> {
    var index = -1
    forEachIndexed { i, s ->
        if (s.id == ship.id) {
            index = i
            return@forEachIndexed
        }
    }

    if (index != -1) {
        val mutable = ArrayList(this)
        mutable.removeAt(index)
        mutable.add(index, ship)
        return mutable
    }

    return this
}

fun List<Ship>.getById(id: Int): Ship? {
    forEach {
        if (id == it.id) {
            return it
        }
    }

    return null
}

fun Ship.reduce(action: Action): Ship {
    return when(action) {
        is GeneratedAction.DefinitiveAction -> copy(hits = hits.plus(action.point))
        else -> this
    }
}