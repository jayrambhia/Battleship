package com.fenchtose.battleship.demo

import com.fenchtose.battleship.logger.AndroidLogger
import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.models.gameplay.BasicGameUtils
import com.fenchtose.battleship.models.gameplay.BasicGameplay
import com.fenchtose.battleship.models.gameplay.Gameplay
import com.fenchtose.battleship.models.moves.EventType
import com.fenchtose.battleship.models.moves.Move
import java.util.*

class DemoGame {
    val board1: BattleBoard
    val board2: BattleBoard

    val gameplay: Gameplay
    val logger: Logger

    init {
        board1 = BattleBoard(User("player 1"), Board(10, 10))
        board2 = BattleBoard(User("player 2"), Board(10, 10))
        logger = AndroidLogger()
        gameplay = BasicGameplay(BasicGameUtils(logger, true))
    }

    fun setupShips() {
        for (i in 1..5) {
            gameplay.setupBattle(board1, Ship(i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL )))
            gameplay.setupBattle(board2, Ship(i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL )))
        }
    }

    fun showBoards() {
        gameplay.showBoards(board1, logger)
        logger.print("DEMO", "--------------\n\n")
        gameplay.showBoards(board2, logger)
    }

    fun play() {
        var offense = board1
        var defense = board2

        val random = Random()
        var moveCount = 0

        while (!defense.didUserLose && moveCount < 200) {
            val result = gameplay.play(Move(offense, defense, Point(random.nextInt(10), random.nextInt(10))))

            if (result.events.size == 1) {
                if (result.events[0].type == EventType.INVALID) {
                    continue
                }
            }

            moveCount++

            for (event in result.events) {
                if (event.type == EventType.LOST) {
                    defense.didUserLose = true
                    break
                }
            }

            defense = offense.also { offense = defense }

            logger.print("move result $moveCount", result.toString())

            gameplay.showBoards(board1, logger)
            gameplay.showBoards(board2, logger)
        }

        if (defense.didUserLose) {
            logger.print("game", "winner is ${offense.user.name}")
        }
    }
}