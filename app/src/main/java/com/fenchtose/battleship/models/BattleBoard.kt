package com.fenchtose.battleship.models

data class BattleBoard(val user: User, val board: Board) {
    var didUserLose: Boolean = false
    val activeShips = ArrayList<Battleship>()
    val destroyedShips = ArrayList<Battleship>()
    val played = HashSet<Point>()
    val hit = HashSet<Point>()
    val missed = HashSet<Point>()
    val opponentHit = HashSet<Point>()
    val opponentMissed = HashSet<Point>()

    init {
        activeShips.addAll(board.ships)
    }

}