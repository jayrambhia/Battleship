package com.fenchtose.battleship.models

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