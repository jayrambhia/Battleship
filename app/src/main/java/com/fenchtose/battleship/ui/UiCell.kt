package com.fenchtose.battleship.ui

import com.fenchtose.battleship.models.Direction

class UiCell(val cell: Cell) {
    var hasShip: Boolean = false
    var direction: Direction? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as UiCell

        if (cell != other.cell) return false
        if (hasShip != other.hasShip) return false
        if (direction != other.direction) return false

        return true
    }

    override fun hashCode(): Int {
        return cell.hashCode()
    }

    override fun toString(): String {
        return "UiCell(cell=$cell, hasShip=$hasShip, direction=$direction)"
    }


}