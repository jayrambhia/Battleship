package com.fenchtose.battleship.models

import android.util.Log
import com.fenchtose.battleship.redux.Action
import com.fenchtose.battleship.redux.Dispatch
import com.fenchtose.battleship.redux.Next

data class GameState(
        val board1: Board,
        val board2: Board,
        val lastPlayed: Int,
        val gameOver: Boolean = false) {

    fun boardById(id: Int): Board? {
        return when (id) {
            board1.id -> board1
            board2.id -> board2
            else -> null
        }
    }

    fun updateBoard(board: Board?): GameState {
        return when(board?.id) {
            board1.id -> copy(board1 = board)
            board2.id -> copy(board2 = board)
            else -> this
        }
    }
}

sealed class GameAction(val offense: Int, val defense: Int): Action {
    class Move(offense: Int, defense: Int, val point: Point): GameAction(offense, defense)
}

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

fun LoggerMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    Log.d("Store-Middleware", "action dispatched -> $action")
    val retVal = next(state, action, dispatch)
    Log.d("Store-Middleware", "action updated to -> $action")
    return retVal
}

fun GameSetupMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is AddShip) {
        val board = state.boardById(action.offense)
        board?.let {
            if (!it.fits(action.ship) || it.isOverlap(action.ship)) {
                return next(state, AddShipInvalid(action.offense, action.ship), dispatch)
            }
        }
    }

    return next(state, action, dispatch)
}

fun StateValidityMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    when(action) {
        is GameAction -> {
            if (state.boardById(action.offense) == null || state.boardById(action.defense) == null) {
                return InvalidState
            }
        }

        is GeneratedAction -> {
            if (state.boardById(action.offense) == null || state.boardById(action.defense) == null) {
                return InvalidState
            }
        }

        is AddShip -> {
            if (state.boardById(action.offense) == null) {
                return InvalidState
            }
        }
    }

    return next(state, action, dispatch)
}

fun MoveMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GameAction) {
        val offense = state.boardById(action.offense)
        offense?.let {
            when(action) {
                is GameAction.Move -> {
                    if (it.hits.contains(action.point) || it.missed.contains(action.point)) {
                        return next(state, GeneratedAction.InvalidMove(action.offense, action.defense, action.point), dispatch)
                    }

                    return next(state, GeneratedAction.PlayMove(action.offense, action.defense, action.point), dispatch)
                }
            }
        }

    }

    return next(state, action, dispatch)
}

fun HitMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.PlayMove) {
        val defense = state.boardById(action.defense)

        defense?.let {
            for (id in it.activeShips) {
                it.ships.getById(id)?.let {
                    if (action.point in it) {
                        return next(state, GeneratedAction.DefinitiveAction.HitMove(action.offense, action.defense, action.point, it), dispatch)
                    }
                }
            }

            return next(state, GeneratedAction.MissedMove(action.offense, action.defense, action.point), dispatch)
        }

    }

    return next(state, action, dispatch)
}

fun DestroyMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.DefinitiveAction.HitMove && state.boardById(action.offense) != null) {
        if (action.ship.hits.size + 1 == action.ship.size) {
            return next(state, GeneratedAction.DefinitiveAction.DestroyShip(action.offense, action.defense, action.point, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun LostMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.DefinitiveAction.DestroyShip) {
        if (state.boardById(action.defense)?.activeShips?.size == 1) {
            return next(state, GeneratedAction.DefinitiveAction.LostGame(action.offense, action.defense, action.point, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun SwitcherMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction && action !is GeneratedAction.InvalidMove) {
        return next(state, SwitchAction(action.offense, action.defense, action), dispatch)
    }

    return next(state, action, dispatch)
}



/*
fun GameState.gameplayMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GameAction) {
        if (action.offense == state.offense.id) {
            when (action) {
                is GameAction.Move -> {
                    if (state.offense.played.contains(action.point)) {
                        return GeneratedAction.InvalidMove(action.defense, action.point)
                    }

                    // check for hits
                    for (ship in state.defense.activeShips) {
                        if (action.point in ship.ship) {
                            return GeneratedAction.HitMove(action.defense, action.point, ship)
                        }
                    }

                    return GeneratedAction.MissedMove(action.defense, action.point)

                }
            }
        }
    }

    if (action is GeneratedAction) {
        if (action.defense == state.defense.id) {
            when(action) {
                is GeneratedAction.HitMove -> {
                    if (action.ship.hits.size + 1 == action.ship.ship.size) {

                    }
                }
            }
        }
    }

}*/
