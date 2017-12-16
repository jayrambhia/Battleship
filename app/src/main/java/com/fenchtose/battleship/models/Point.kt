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

    /*
    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }

    override fun toString(): String {
        return "Point(col=$col, row=$row)"
    }*/


}