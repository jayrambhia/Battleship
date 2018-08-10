package com.fenchtose.battleship.models

import com.fenchtose.battleship.redux.Action
import com.fenchtose.battleship.redux.Dispatch
import com.fenchtose.battleship.redux.Next

data class GameState(
        val board1: BattleBoard,
        val board2: BattleBoard,
        val offense: Int
) {
    fun offense(): BattleBoard {
        return if (board1.id == offense) board1 else board2
    }

    fun defense(): BattleBoard {
        return if (board1.id == offense) board2 else board1
    }
}

sealed class GameAction(val offense: Int, val defense: Int): Action {
    class Move(offense: Int, defense: Int, val point: Point): GameAction(offense, defense)
}

sealed class GeneratedAction(val offense: Int, val defense: Int, val point: Point): Action {
    class InvalidMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)
    class PlayMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)
    class HitMove(offense: Int, defense: Int, point: Point, val ship: Battleship): GeneratedAction(offense, defense, point)
    class MissedMove(offense: Int, defense: Int, point: Point): GeneratedAction(offense, defense, point)
    class DestroyShip(offense: Int, defense: Int, point: Point, val ship: Battleship): GeneratedAction(offense, defense, point)
    class LostGame(offense: Int, defense: Int, point: Point, val ship: Battleship): GeneratedAction(offense, defense, point)
}

data class SwitchAction(val offense: Int, val defense: Int, val last: GeneratedAction): Action

fun MoveMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GameAction && action.offense == state.offense) {
        when(action) {
            is GameAction.Move -> {
                if (state.offense().played.contains(action.point)) {
                    return next(state, GeneratedAction.InvalidMove(action.offense, action.defense, action.point), dispatch)
                }

                return next(state, GeneratedAction.PlayMove(action.offense, action.defense, action.point), dispatch)
            }
        }
    }

    return next(state, action, dispatch)
}

fun HitMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.PlayMove && action.offense == state.offense) {
        for (ship in state.defense().activeShips) {
            if (action.point in ship.ship) {
                return next(state, GeneratedAction.HitMove(action.offense, action.defense, action.point, ship), dispatch)
            }
        }

        return next(state, GeneratedAction.MissedMove(action.offense, action.defense, action.point), dispatch)
    }

    return next(state, action, dispatch)
}

fun DestroyMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.HitMove && action.offense == state.offense) {
        if (action.ship.hits.size + 1 == action.ship.ship.size) {
            return next(state, GeneratedAction.DestroyShip(action.offense, action.defense, action.point, action.ship), dispatch)
        }
    }

    return next(state, action, dispatch)
}

fun LostMiddleware(state: GameState, action: Action, dispatch: Dispatch, next: Next<GameState>): Action {
    if (action is GeneratedAction.DestroyShip && action.offense == state.offense) {
        if (state.defense().activeShips.size == 1) {
            return next(state, GeneratedAction.LostGame(action.offense, action.defense, action.point, action.ship), dispatch)
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
