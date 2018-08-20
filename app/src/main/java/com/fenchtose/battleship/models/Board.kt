package com.fenchtose.battleship.models

import com.fenchtose.battleship.add
import com.fenchtose.battleship.redux.Action

data class Board(
        val id: Int,
        val user: User,
        val width: Int,
        val height: Int,
        val ships: List<Ship> = listOf(),
        val hits: Set<Point> = setOf(),
        val misses: Set<Point> = setOf(),
        val opponentHits: Set<Point> = setOf(),
        val opponentMisses: Set<Point> = setOf()
) {

    val activeShips = ships.filter { !it.destroyed }.map { it.id }
    val didUserLose = ships.isNotEmpty() && activeShips.isEmpty()

    fun fits(ship: Ship) = ship.start in this && ship.end in this

    fun isOverlap(ship: Ship): Boolean {
        return ships.any { ship in it }
    }

    operator fun contains(p: Point) = p.isValid() && p.col < width && p.row < height

    companion object {
        fun testBoard(width: Int, height: Int) = Board(0, User("test"), width, height)
    }
}

data class AddShip(val offense: Int, val ship: Ship): Action
data class AddShipInvalid(val offense: Int, val ship: Ship): Action

fun Board.reduceOffense(action: Action): Board {
    return when(action) {
        is GeneratedAction.MissedMove -> copy(misses = misses.plus(action.point))
        is GeneratedAction.DefinitiveAction -> copy(hits = hits.plus(action.point))
        else -> this
    }
}

fun Board.reduceDefense(action: Action): Board {
    return when(action) {
        is GeneratedAction.MissedMove -> copy(opponentMisses = opponentMisses.plus(action.point))
        is GeneratedAction.DefinitiveAction -> {
            val ship = ships.getById(action.ship.id)
            if (ship != null) {
                val updated = ship.reduce(action)
                copy(opponentHits = opponentHits.plus(action.point), ships = ships.update(updated))
            } else {
                this
            }
        }
        else -> this
    }
}

fun Board.reduceSetup(action: Action): Board {
    return when(action) {
        is AddShip -> copy(ships = ships.add(action.ship))
        else -> this
    }
}

