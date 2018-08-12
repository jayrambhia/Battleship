package com.fenchtose.battleship.converter

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.Board
import com.fenchtose.battleship.models.Direction
import com.fenchtose.battleship.models.Point
import com.fenchtose.battleship.ui.Cell
import com.fenchtose.battleship.ui.UiCell

class BasicBoardToUiConverter(val logger: Logger?) : BoardToUiConverter {

    override fun convert(board: Board): ArrayList<UiCell> {
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
                cells[index].hasShip = true
                cells[index].direction = ship.direction
                cells[index].cell.opponentHit = ship.hits.contains(cells[index].cell.point)
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
                cells[index].hasShip = true
                cells[index].direction = ship.direction
                cells[index].cell.opponentHit = true
                count++
            }
        }

        for (point in board.hits) {
            val index = point.row * board.width + point.col
            cells[index].cell.userHit = true
        }

        for (point in board.missed) {
            val index = point.row * board.width + point.col
            cells[index].cell.userMiss = true
        }

        for (point in board.opponentMissed) {
            val index = point.row * board.width + point.col
            cells[index].cell.opponentMiss = true
        }

        return cells
    }

    private fun generateCells(board: Board): ArrayList<UiCell> {
        val cells = ArrayList<UiCell>(board.width * board.height)
        for (i in 0 until board.height) {
            for (j in 0 until board.width) {
                cells.add(UiCell(Cell(Point(j, i))))
            }
        }

        return cells
    }
}