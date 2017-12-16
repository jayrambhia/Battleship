package com.fenchtose.battleship.models.gameplay

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.*

class BasicGameUtils(val logger: Logger, val debug: Boolean) : GameUtils {

    val TAG = "BasicGameUtils"

    override fun putShip(board: BattleBoard, ship: Battleship): Boolean {
        if (!board.board.fits(ship.ship)) {
            if (debug) {
                logger.print(TAG, "$ship does not fit on the board")
            }
            return false
        }

        if (board.board.isOverlap(ship)) {
            return false
        }

        board.board.ships.add(ship)
        board.activeShips.add(ship)
        return true
    }

    override fun hit(ship: Battleship, at: Point): Boolean {

        if (ship.hits.contains(at)) {
            return false
        }

        if (at in ship.ship) {
            ship.hits.add(at)
            if (ship.hits.size == ship.ship.size) {
                ship.destroyed = true
            }

            return true
        }

        return false
    }

    override fun isOverlap(exists: Ship, new: Ship) = new in exists

    override fun display(board: BattleBoard, logger: Logger) {

        val parr = Array(board.board.height, {Array(board.board.width, { _ -> '-'})})

        for (ship in board.activeShips) {
            var i = ship.ship.start.col
            var j = ship.ship.start.row
            var count = ship.ship.size

            while(count > 0) {
                val c = if (ship.hits.contains(Point(i, j))) 'o' else '0'

                parr[j][i] = c
                count--

                if (ship.ship.direction == Direction.HORIZONTAL) {
                    i++
                } else {
                    j++
                }
            }

        }

        for (ship in board.destroyedShips) {
            var i = ship.ship.start.col
            var j = ship.ship.start.row
            var count = ship.ship.size

            while(count > 0) {

                val c = 'x'

                parr[j][i] = c
                count--

                if (ship.ship.direction == Direction.HORIZONTAL) {
                    i++
                } else {
                    j++
                }
            }

        }

        logger.print("board", "" + board.user + "'s board: --->")
        val sb = StringBuilder()
        for (row in parr) {
            for (col in row) {
                sb.append(col)
                sb.append(" ")
            }

            sb.append("\n")
        }

        logger.print("board", sb.toString())
    }
}