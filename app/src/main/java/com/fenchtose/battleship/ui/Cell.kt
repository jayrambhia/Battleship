package com.fenchtose.battleship.ui

import com.fenchtose.battleship.models.Direction
import com.fenchtose.battleship.models.Point

data class Cell(val point: Point,
                val hasShip: Boolean = false, val direction: Direction? = null,
                val userHit: Boolean = false, val userMissed: Boolean = false,
                val opponentHit: Boolean = false, val opponentMissed: Boolean = false)