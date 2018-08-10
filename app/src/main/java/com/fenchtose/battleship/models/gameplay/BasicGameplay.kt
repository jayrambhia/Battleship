package com.fenchtose.battleship.models.gameplay

import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.models.Battleship
import com.fenchtose.battleship.models.moves.Move
import com.fenchtose.battleship.models.Ship
import com.fenchtose.battleship.models.moves.Event
import com.fenchtose.battleship.models.moves.Result

class BasicGameplay(val gameUtils: GameUtils) : Gameplay {

    override fun setupBattle(board: BattleBoard, ship: Ship): Boolean {
        return gameUtils.putShip(board, Battleship(ship))
    }

    override fun play(move: Move): Result {
        var hit = false
        var destroyed = false
        var lost = false
        var hitShip: Battleship? = null

        if (move.offense.played.contains(move.hit)) {
            return Result.invalid(move.hit, move.defense)
        }

//        for (ship in move.defense.activeShips) {
//            hit = gameUtils.hit(ship, move.hit)
//            if (hit) {
//                hitShip = ship
//                if (ship.destroyed) {
//                    destroyed = true
//                    move.defense.destroyedShips.add(ship)
//                    move.defense.activeShips.remove(ship)
//                    if (move.defense.activeShips.size == 0) {
//                        lost = true
//                    }
//                }
//                break
//            }
//        }
//
//        move.offense.played.add(move.hit)
//
//        if (!hit || hitShip == null) {
//            move.offense.missed.add(move.hit)
//            move.defense.opponentMissed.add(move.hit)
//            return Result.miss(move.hit, move.defense)
//        }

        val events = ArrayList<Event>()
//        if (hit) {
//            move.offense.hit.add(move.hit)
//            move.defense.opponentHit.add(move.hit)
//            events.add(Event.hit(move.hit, move.defense, hitShip))
//        }
//
//        if (destroyed) {
//            events.add(Event.destroyed(move.hit, move.defense, hitShip))
//        }
//
//        if (lost) {
//            events.add(Event.lost(move.hit, move.defense))
//        }

        return Result(move.hit, move.defense, events)
    }

    override fun showBoards(board: BattleBoard, logger: Logger) {
        gameUtils.display(board, logger)
    }
}