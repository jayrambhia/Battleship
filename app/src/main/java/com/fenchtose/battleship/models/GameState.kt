package com.fenchtose.battleship.models

import android.util.Log
import com.fenchtose.battleship.redux.Action
import com.fenchtose.battleship.redux.Dispatch
import com.fenchtose.battleship.redux.Next
import com.fenchtose.battleship.redux.reduceChildState

data class GameState(
        val board1: Board,
        val board2: Board,
        val lastPlayed: Int,
        val gameOver: Boolean = false) {

    fun hasBoardById(id: Int): Boolean {
        return when(id) {
            board1.id, board2.id -> true
            else -> false
        }
    }

    fun boardById(id: Int): Board {
        return when (id) {
            board1.id -> board1
            board2.id -> board2
            else -> throw RuntimeException("No such board with id: $id")
        }
    }

    fun updateBoard(board: Board): GameState {
        return when(board.id) {
            board1.id -> copy(board1 = board)
            board2.id -> copy(board2 = board)
            else -> this
        }
    }
}

private fun whichBoard(board: Board, offense: Board, defense: Board): Board {
    return when(board.id) {
        offense.id -> offense
        defense.id -> defense
        else -> board
    }
}

fun GameState.reduceGameplay(action: Action): GameState {
    return when(action) {
        is GeneratedAction -> {
            val offense = boardById(action.offense).reduceOffense(action)
            val defense = boardById(action.defense).reduceDefense(action)
            copy(board1 = whichBoard(board1, offense, defense),
                    board2 = whichBoard(board2, offense, defense),
                    gameOver = action is GeneratedAction.DefinitiveAction.LostGame)

        }
        is SwitchAction -> {
            reduceGameplay(action.last).copy(lastPlayed = action.offense)
        }
        else -> this
    }
}

fun GameState.reduceSetup(action: Action): GameState {
    return when(action) {
        is AddShip -> reduceChildState(this, boardById(action.offense), action, Board::reduceSetup, { state, board -> state.updateBoard(board) })
        else -> this
    }
}

data class Move(val offense: Int, val defense: Int, val point: Point): Action

sealed class GeneratedAction(val offense: Int, val defense: Int, val point: Point): Action {
    class InvalidMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)
    class PlayMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)
    class MissedMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)

    sealed class DefinitiveAction(offense: Int, defense: Int, point: Point, val ship: Ship): GeneratedAction(offense, defense, point) {
        class HitMove(offense: Int, defense: Int, point: Point, ship: Ship) : DefinitiveAction(offense, defense, point, ship)
        class DestroyShip(offense: Int, defense: Int, point: Point, ship: Ship) : DefinitiveAction(offense, defense, point, ship)
        class LostGame(offense: Int, defense: Int, point: Point, ship: Ship) : DefinitiveAction(offense, defense, point, ship)
    }
}

object InvalidState: Action

data class SwitchAction(val offense: Int, val defense: Int, val last: GeneratedAction): Action

fun loggerMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    Log.d("Store-Middleware", "action dispatched -> $action")
    val retVal = next(state, action, dispatch)
    Log.d("Store-Middleware", "action updated to -> $action")
    return retVal
}

fun gameSetupMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is AddShip) {
        val board = state.boardById(action.offense)
        if (!board.fits(action.ship) || board.isOverlap(action.ship)) {
            return next(state, AddShipInvalid(action.offense, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun stateValidityMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    when(action) {
        is Move -> {
            if (!state.hasBoardById(action.offense) || !state.hasBoardById(action.defense)) {
                return InvalidState
            }
        }

        is GeneratedAction -> {
            if (!state.hasBoardById(action.offense) || !state.hasBoardById(action.defense)) {
                return InvalidState
            }
        }

        is AddShip -> {
            if (!state.hasBoardById(action.offense)) {
                return InvalidState
            }
        }
    }

    return next(state, action, dispatch)
}

fun moveMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is Move) {
        val offense = state.boardById(action.offense)
        if (offense.hits.contains(action.point) || offense.misses.contains(action.point)) {
            return next(state, GeneratedAction.InvalidMove(action.offense, action.defense, action.point), dispatch)
        }

        return next(state, GeneratedAction.PlayMove(action.offense, action.defense, action.point), dispatch)

    }

    return next(state, action, dispatch)
}

fun hitMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.PlayMove) {
        val defense = state.boardById(action.defense)

        for (id in defense.activeShips) {
            defense.ships.getById(id)?.let {
                if (action.point in it) {
                    return next(state, GeneratedAction.DefinitiveAction.HitMove(action.offense, action.defense, action.point, it), dispatch)
                }
            }
        }

        return next(state, GeneratedAction.MissedMove(action.offense, action.defense, action.point), dispatch)
    }

    return next(state, action, dispatch)
}

fun destroyMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.DefinitiveAction.HitMove) {
        if (action.ship.hits.size + 1 == action.ship.size) {
            return next(state, GeneratedAction.DefinitiveAction.DestroyShip(action.offense, action.defense, action.point, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun lostMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.DefinitiveAction.DestroyShip) {
        if (state.boardById(action.defense).activeShips.size == 1) {
            return next(state, GeneratedAction.DefinitiveAction.LostGame(action.offense, action.defense, action.point, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun switcherMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction && action !is GeneratedAction.InvalidMove) {
        return next(state, SwitchAction(action.offense, action.defense, action), dispatch)
    }

    return next(state, action, dispatch)
}