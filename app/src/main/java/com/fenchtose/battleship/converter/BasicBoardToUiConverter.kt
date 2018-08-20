package com.fenchtose.battleship.converter

import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.ui.Cell

class BasicBoardToUiConverter : BoardToUiConverter {

    override fun convert(board: Board): ArrayList<Cell> {
        return generateCells(board)
    }

    private fun generateCells(board: Board): ArrayList<Cell> {
        val cells = ArrayList<Cell>(board.width * board.height)
        for (i in 0 until board.height) {
            for (j in 0 until board.width) {
                val point = Point(j, i)
                cells.add(Cell(
                        point = point,
                        hasShip = board.ships.contains(point),
                        direction = board.ships.getShipDirection(point),
                        userHit = board.hits.contains(point),
                        userMissed = board.misses.contains(point),
                        opponentMissed = board.opponentMisses.contains(point),
                        opponentHit = board.opponentHits.contains(point)
                ))
            }
        }

        return cells
    }
}