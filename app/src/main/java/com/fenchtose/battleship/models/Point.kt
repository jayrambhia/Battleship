package com.fenchtose.battleship.models

// 0 index
class Point(val col: Int, val row: Int) {
    companion object {
        fun None(): Point {
            return Point(-1, -1)
        }
    }

    fun getEndPoint(size: Int, direction: Direction): Point {
        when (direction) {
            Direction.HORIZONTAL -> return Point(col + size - 1, row)
            Direction.VERTICAL -> return Point(col, row + size - 1)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Point

        if (row != other.row) return false
        if (col != other.col) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }

    override fun toString(): String {
        return "Point(col=$col, row=$row)"
    }


}