package com.fenchtose.battleship.models

import com.fenchtose.battleship.add
import com.fenchtose.battleship.redux.Action
import com.fenchtose.battleship.redux.reduceChildStateNullable
import com.fenchtose.battleship.remove

data class Board(
        val id: Int,
        val user: User,
        val width: Int,
        val height: Int,
        val ships: List<Ship> = listOf(),
        val didUserLose: Boolean = false,
        val activeShips: List<Int> = listOf(),
        val destroyedShips: List<Int> = listOf(),
        val hits: Set<Point> = setOf(),
        val missed: Set<Point> = setOf(),
        val opponentHits: Set<Point> = setOf(),
        val opponentMissed: Set<Point> = setOf()
) {
    fun fits(ship: Ship) = ship.start in this && ship.end in this

    fun isOverlap(ship: Ship): Boolean {
        return ships.any { ship in it }
    }

    operator fun contains(p: Point) = p.isValid() && p.col < width && p.row < height

    fun activeShips(): List<Ship> {
        return ships.filter { it.id in activeShips }
    }

    fun destroyedShips(): List<Ship> {
        return ships.filter { it.id in destroyedShips }
    }
}

data class AddShip(val offense: Int, val ship: Ship): Action
data class AddShipInvalid(val offense: Int, val ship: Ship): Action

fun GameState.reduceShip(action: Action): GameState {
    return when(action) {
        is AddShip -> reduceChildStateNullable(this, boardById(action.offense), action, Board?::reduceOffense, { state, board -> state.updateBoard(board) })
        is GeneratedAction -> {
            var updated = reduceChildStateNullable(this, boardById(action.offense), action, Board?::reduceOffense, { state, board -> state.updateBoard(board) })
            updated = reduceChildStateNullable(updated, updated.boardById(action.defense), action, Board?::reduceDefense, { state, board -> state.updateBoard(board)})
            if (action is GeneratedAction.DefinitiveAction.LostGame) {
                updated = updated.copy(gameOver = true)
            }

            updated
        }
        is SwitchAction -> {
            val updated = reduceShip(action.last)
            updated.copy(lastPlayed = action.offense)
        }
        else -> this
    }
}

fun Board?.reduceOffense(action: Action): Board? {
    if (this == null) {
        return this
    }

    return when(action) {
        is AddShip -> copy(ships = ships.add(action.ship), activeShips = activeShips.add(action.ship.id))
        is GeneratedAction.MissedMove -> copy(missed = missed.plus(action.point))
        is GeneratedAction.DefinitiveAction -> copy(hits = hits.plus(action.point))
        else -> this
    }
}

fun Board?.reduceDefense(action: Action): Board? {
    if (this == null) {
        return this
    }

    return when(action) {
//        is AddShip -> copy(ships = ships.add(action.ship), activeShips = activeShips.add(action.ship.id))
        is GeneratedAction.MissedMove -> copy(opponentMissed = opponentMissed.plus(action.point))
        is GeneratedAction.DefinitiveAction -> {
            val ship = ships.getById(action.ship.id)
            if (ship != null) {
                val updated = ship.reduce(action)
                copy(opponentHits = opponentHits.plus(action.point),
                        ships = ships.update(updated),
                        activeShips = if (updated.destroyed) activeShips.remove(updated.id) else activeShips,
                        destroyedShips = if (updated.destroyed) destroyedShips.add(updated.id) else destroyedShips
                )
            } else {
                this
            }
        }
        else -> this
    }
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
        is GeneratedAction.DefinitiveAction.HitMove -> copy(hits = hits.add(action.point))
        is GeneratedAction.DefinitiveAction.DestroyShip -> copy(hits = hits.add(action.point), destroyed = true)
        is GeneratedAction.DefinitiveAction.LostGame -> copy(hits = hits.add(action.point), destroyed = true)
        else -> this
    }
}
