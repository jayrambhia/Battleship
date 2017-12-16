package com.fenchtose.battleship.models.moves

import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.models.Battleship
import com.fenchtose.battleship.models.Point

data class Move(val offense: BattleBoard, val defense: BattleBoard, val hit: Point) {

    data class Result(val hit: Point?, val defense: BattleBoard?, val events: ArrayList<Event>) {

        companion object {
            fun miss(hit: Point, defense: BattleBoard): Result {
                val events = ArrayList<Event>()
                events.add(Event.miss(hit, defense))
                return Result(hit, defense, events)
            }

            fun invalid(hit: Point, defense: BattleBoard): Result {
                val events = ArrayList<Event>()
                events.add(Event.invalid(hit, defense))
                return Result(hit, defense, events)
            }
        }

    }

    data class Event(val hit: Point?, val defense: BattleBoard?, val ship: Battleship?, val type: EventType) {
        companion object {
            fun invalid(hit: Point, defense: BattleBoard): Event {
                return Event(hit, defense, null, EventType.INVALID)
            }

            fun miss(hit: Point, defense: BattleBoard): Event {
                return Event(hit, defense, null, EventType.MISS)
            }

            fun hit(hit: Point, defense: BattleBoard, ship: Battleship): Event {
                return Event(hit, defense, ship, EventType.HIT)
            }

            fun destroyed(hit: Point, defense: BattleBoard, ship: Battleship): Event {
                return Event(hit, defense, ship, EventType.DESTROYED)
            }

            fun lost(hit: Point, defense: BattleBoard): Event {
                return Event(hit, defense, null, EventType.LOST)
            }
        }

    }
}