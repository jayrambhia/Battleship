package com.fenchtose.battleship.models

// 0 index
data class Point(val col: Int, val row: Int) {

    companion object {
        fun None(): Point {
            return Point(-1, -1)
        }
    }

    fun isValid() = col >= 0 && row >= 0

    operator fun plus(wd: WeighedDirection) = when (wd.d) {
        Direction.HORIZONTAL -> Point(col + wd.len, row)
        Direction.VERTICAL -> Point(col, row + wd.len)
    }

}

enum class Direction {
    HORIZONTAL, VERTICAL;

    operator fun times(n: Int): WeighedDirection {
        return WeighedDirection(this, n)
    }
}

data class WeighedDirection(val d: Direction, val len: Int)