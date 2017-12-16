package com.fenchtose.battleship.models

enum class Direction {
    HORIZONTAL, VERTICAL;

    operator fun times(n: Int): WeighedDirection {
        return WeighedDirection(this, n)
    }
}

data class WeighedDirection(val d: Direction, val len: Int) {

}