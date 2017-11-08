package com.fenchtose.battleship.models.gameplay

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.models.moves.Move
import com.fenchtose.battleship.models.Ship

interface Gameplay {
    fun setupBattle(board: BattleBoard, ship: Ship): Boolean
    fun play(move: Move): Move.Result
    fun showBoards(board: BattleBoard, logger: Logger)
}