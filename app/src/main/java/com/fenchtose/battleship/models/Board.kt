package com.fenchtose.battleship.models

import com.fenchtose.battleship.add
import com.fenchtose.battleship.redux.Action
import com.fenchtose.battleship.redux.reduceChildState

data class Board(
        val width: Int,
        val height: Int,
        val ships: List<Battleship> = listOf()) {

    fun fits(ship: Ship) = ship.start in this && ship.end in this

    fun isOverlap(ship: Ship): Boolean {
        return ships.any { ship in it.ship }
    }

    fun isOverlap(ship: Battleship): Boolean {
        return isOverlap(ship.ship)
    }

    operator fun contains(p: Point) = p.isValid() && p.col < width && p.row < height

}

data class BattleBoard(
        val id: Int,
        val user: User,
        val board: Board,
        val didUserLose: Boolean = false,
        val activeShips: List<Battleship> = listOf(),
        val destroyedShips: List<Battleship> = listOf(),
        val played: Set<Point> = setOf(),
        val hits: Set<Point> = setOf(),
        val missed: Set<Point> = setOf(),
        val opponentHits: Set<Point> = setOf(),
        val opponentMissed: Set<Point> = setOf()
)

data class AddShip(val offense: Int, val ship: Battleship): Action

fun GameState.reduceShip(action: Action): GameState {
    return when(action) {
        is AddShip ->  reduceChildState(this, offense(), action, BattleBoard::reduce, {state, board -> state.copy(of)})
    }
}

fun Board.reduce(action: Action): Board {
    return when(action) {
        is AddShip -> copy(ships = ships.add(action.ship))
        else -> this
    }
}

fun BattleBoard.reduce(action: Action): BattleBoard {
    return when(action) {
        is AddShip -> reduceChildState(this, board, action, Board::reduce,
                { state, child -> state.copy(board = child, activeShips = state.activeShips.add(action.ship))} )
        else -> this
    }
}
