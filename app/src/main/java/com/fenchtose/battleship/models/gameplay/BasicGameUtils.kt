package com.fenchtose.battleship.models.gameplay

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.*

class BasicGameUtils(val logger: Logger, val debug: Boolean) : GameUtils {

    val TAG = "BasicGameUtils";

    override fun putShip(board: BattleBoard, ship: Battleship): Boolean {
        if (!fitsOnBoard(board.board, ship.ship)) {
            if (debug) {
                logger.print(TAG, "$ship does not fit on the board")
            }
            return false
        }

        for (placeShip in board.board.ships) {
            if (isOverlap(placeShip.ship, ship.ship)) {
                return false
            }
        }

        board.board.ships.add(ship)
        board.activeShips.add(ship)
        return true
    }

    override fun hit(ship: Battleship, at: Point): Boolean {

        if (ship.hits.contains(at)) {
            return false
        }

        var hit = false
        when (ship.ship.direction) {
            Direction.HORIZONTAL -> hit = checkHitHorizontal(ship, at)
            Direction.VERTICAL -> hit = checkHitVertical(ship, at)
        }

        if (hit) {
            ship.hits.add(at)
            if (ship.hits.size == ship.ship.size) {
                ship.destroyed = true
            }
        }

        return hit
    }

    fun checkHitHorizontal(ship: Battleship, at: Point): Boolean {
        return at.row == ship.ship.start.row && at.col >= ship.ship.start.col && at.col < ship.ship.start.col + ship.ship.size
    }

    fun checkHitVertical(ship: Battleship, at: Point): Boolean {
        return at.col == ship.ship.start.col && at.row >= ship.ship.start.row && at.row < ship.ship.start.row + ship.ship.size
    }

    override fun isOverlap(exists: Ship, new: Ship): Boolean {
        val end1 = exists.start.getEndPoint(exists.size, exists.direction)
        val end2 = new.start.getEndPoint(new.size, new.direction)

        if (exists.direction == Direction.HORIZONTAL) {
            // check if intersects
            if (new.start.row <= exists.start.row && end2.row >= exists.start.row) {
                return !(end2.col < exists.start.col || new.start.col > end1.col)
            }

            return false

        } else {
            // check if intersects
            if (new.start.col <= exists.start.col && end2.col >= exists.start.col) {
                return !(end2.row < exists.start.row || new.start.row > end1.row)
            }

            return false
        }

    }

    fun fitsOnBoard(board: Board, ship: Ship): Boolean {
        val end = ship.start.getEndPoint(ship.size, ship.direction)
        return (ship.start.col >= 0 && ship.start.row >= 0 && end.col < board.width && end.row < board.height)
    }

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