package com.fenchtose.battleship

import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.redux.SimpleStore

class Gamestore(initialSate: GameState): SimpleStore<GameState>(
        initialState = initialSate,
        middlewares = listOf(
                ::LoggerMiddleware,
                ::StateValidityMiddleware,
                ::GameSetupMiddleware,
                ::MoveMiddleware,
                ::HitMiddleware,
                ::DestroyMiddleware,
                ::LostMiddleware,
                ::SwitcherMiddleware
        ),
        reducers = listOf(
            GameState::reduceShip
        )
)