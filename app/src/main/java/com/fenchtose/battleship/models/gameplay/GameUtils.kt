package com.fenchtose.battleship.models.gameplay

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.models.Battleship
import com.fenchtose.battleship.models.Point
import com.fenchtose.battleship.models.Ship

interface GameUtils {
    fun hit(ship: Battleship, at: Point): Boolean
    fun putShip(board: BattleBoard, ship: Battleship): Boolean
    fun isOverlap(exists: Ship, new: Ship): Boolean

    fun display(board: BattleBoard, logger: Logger)
}