package com.fenchtose.battleship.converter

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.models.Direction
import com.fenchtose.battleship.models.Point
import com.fenchtose.battleship.ui.Cell
import com.fenchtose.battleship.ui.UiCell

class BasicBoardToUiConverter(val logger: Logger?) : BoardToUiConverter {

    override fun convert(board: BattleBoard): ArrayList<UiCell> {
        val cells = generateCells(board)
        for (ship in board.activeShips) {
            val start = ship.ship.start
            var count = 0
            while (count < ship.ship.size) {
                val index = start.row * board.board.width + start.col + (if (ship.ship.direction == Direction.HORIZONTAL) count else count * board.board.width)
                if (index < 0) {
                    logger?.print("converter - a", "index: $index")
                    logger?.print("converter - a", "start: $start")
                }
                cells[index].hasShip = true
                cells[index].direction = ship.ship.direction
                cells[index].cell.opponentHit = ship.hits.contains(cells[index].cell.point)
                count++
            }
        }

        for (ship in board.destroyedShips) {
            val start = ship.ship.start
            var count = 0
            while (count < ship.ship.size) {
                val index = start.row * board.board.width + start.col + (if (ship.ship.direction == Direction.HORIZONTAL) count else count * board.board.width)
                if (index < 0) {
                    logger?.print("converter - d", "index: $index")
                    logger?.print("converter - d", "start: $start")
                }
                cells[index].hasShip = true
                cells[index].direction = ship.ship.direction
                cells[index].cell.opponentHit = true
                count++
            }
        }

        for (point in board.hit) {
            val index = point.row * board.board.width + point.col
            cells[index].cell.userHit = true
        }

        for (point in board.missed) {
            val index = point.row * board.board.width + point.col
            cells[index].cell.userMiss = true
        }

        for (point in board.opponentMissed) {
            val index = point.row * board.board.width + point.col
            cells[index].cell.opponentMiss = true
        }

        return cells
    }

    private fun generateCells(board: BattleBoard): ArrayList<UiCell> {
        val cells = ArrayList<UiCell>(board.board.width * board.board.height)
        for (i in 0..board.board.height-1) {
            for (j in 0..board.board.width-1) {
                cells.add(UiCell(Cell(Point(j, i))))
            }
        }

        return cells
    }
}