package com.fenchtose.battleship.converter

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.Board
import com.fenchtose.battleship.models.Direction
import com.fenchtose.battleship.models.Point
import com.fenchtose.battleship.ui.Cell

class BasicBoardToUiConverter(val logger: Logger?) : BoardToUiConverter {

    override fun convert(board: Board): ArrayList<Cell> {
        val cells = generateCells(board)
        for (ship in board.activeShips()) {
            val start = ship.start
            var count = 0
            while (count < ship.size) {
                val index = start.row * board.width + start.col + (if (ship.direction == Direction.HORIZONTAL) count else count * board.width)
                if (index < 0) {
                    logger?.print("converter - a", "index: $index")
                    logger?.print("converter - a", "start: $start")
                }

                cells[index] = cells[index].copy(hasShip = true, direction = ship.direction)
                count++
            }
        }

        for (ship in board.destroyedShips()) {
            val start = ship.start
            var count = 0
            while (count < ship.size) {
                val index = start.row * board.width + start.col + (if (ship.direction == Direction.HORIZONTAL) count else count * board.width)
                if (index < 0) {
                    logger?.print("converter - d", "index: $index")
                    logger?.print("converter - d", "start: $start")
                }

                cells[index] = cells[index].copy(hasShip = true, direction = ship.direction)
                count++
            }
        }

        return cells
    }

    private fun generateCells(board: Board): ArrayList<Cell> {
        val cells = ArrayList<Cell>(board.width * board.height)
        for (i in 0 until board.height) {
            for (j in 0 until board.width) {
                val point = Point(j, i)
                cells.add(Cell(
                        point = point,
                        userHit = board.hits.contains(point),
                        userMiss = board.missed.contains(point),
                        opponentMiss = board.opponentMissed.contains(point),
                        opponentHit = board.opponentHits.contains(point)
                ))
            }
        }

        return cells
    }
}