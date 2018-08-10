package com.fenchtose.battleship.models

// 0 index
data class Point(val col: Int, val row: Int) {

    companion object {
        fun None(): Point {
            return Point(-1, -1)
        }
    }

    fun isValid() = col >= 0 && row >= 0

    fun is2DIn(p: Point) = col < p.col && row < p.row
    fun is2DOut(p: Point) = col > p.col && row > p.row

    @Deprecated("Trying to remove this method", ReplaceWith("this + direction * size"))
    fun getEndPoint(size: Int, direction: Direction) = this + direction * (size-1)

    operator fun plus(wd: WeighedDirection) = when (wd.d) {
        Direction.HORIZONTAL -> Point(col + wd.len, row)
        Direction.VERTICAL -> Point(col, row + wd.len)
    }

    operator fun plus(d: Direction) = plus(d * 1)

}

data class Position(val start: Point, val d: Direction, val len: Int) {
    val end = start + d*(len-1)

    operator fun contains(p: Point) = when(d) {
        Direction.HORIZONTAL -> start.row == p.row && start.col <= p.col && end.col >= p.col
        Direction.VERTICAL -> start.col == p.col && start.row <= p.row && end.row >= p.row
    }

    operator fun contains(p: Position): Boolean {
        if (p.d == d) {
            return p.start in this || p.end in this
        }

        val hp = if (p.d == Direction.HORIZONTAL) p else this
        val vp = if (p.d == Direction.VERTICAL) p else this

        // check hp row intersection
        return hp.start.row in vp.start.row..vp.end.row && vp.start.col in hp.start.col..hp.end.col
    }

}

enum class Direction {
    HORIZONTAL, VERTICAL;

    operator fun times(n: Int): WeighedDirection {
        return WeighedDirection(this, n)
    }
}

data class WeighedDirection(val d: Direction, val len: Int)