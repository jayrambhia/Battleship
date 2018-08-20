package com.fenchtose.battleship

import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.redux.SimpleStore

class Gamestore(initialSate: GameState): SimpleStore<GameState>(
        initialState = initialSate,
        middlewares = listOf(
                ::loggerMiddleware,
                ::stateValidityMiddleware,
                ::gameSetupMiddleware,
                ::moveMiddleware,
                ::hitMiddleware,
                ::destroyMiddleware,
                ::lostMiddleware,
                ::switcherMiddleware
        ),
        reducers = listOf(
                GameState::reduceSetup,
                GameState::reduceGameplay
        )
)