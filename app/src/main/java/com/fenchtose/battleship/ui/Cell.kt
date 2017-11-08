package com.fenchtose.battleship.ui

import com.fenchtose.battleship.models.Point

class Cell(val point: Point) {
    var userHit: Boolean = false
    var userMiss: Boolean = false
    var opponentHit: Boolean = false
    var opponentMiss: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Cell

        if (point != other.point) return false
        if (userHit != other.userHit) return false
        if (userMiss != other.userMiss) return false
        if (opponentHit != other.opponentHit) return false
        if (opponentMiss != other.opponentMiss) return false
        return true
    }

    override fun hashCode(): Int {
        return point.hashCode()
    }

    override fun toString(): String {
        return "Cell(point=$point, userHit=$userHit, opponentHit=$opponentHit)"
    }


}