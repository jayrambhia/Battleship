package com.fenchtose.battleship.demo

import com.fenchtose.battleship.converter.BasicBoardToUiConverter
import com.fenchtose.battleship.logger.AndroidLogger
import com.fenchtose.battleship.logger.Logger
import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.models.gameplay.BasicGameUtils
import com.fenchtose.battleship.models.gameplay.BasicGameplay
import com.fenchtose.battleship.models.gameplay.Gameplay
import com.fenchtose.battleship.models.moves.EventType
import com.fenchtose.battleship.models.moves.Move
import com.fenchtose.battleship.ui.UiCell
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

class RxGame() {
    private val publisher: PublishSubject<GameState> = PublishSubject.create()

    private var d: Disposable? = null

    private val gameplay: Gameplay
    private val logger: Logger

    private val board1: BattleBoard
    private val board2: BattleBoard
    private val random = Random()
    private val conveter: BasicBoardToUiConverter

    val width: Int

    private var paused: Boolean = true

    init {
        board1 = BattleBoard(User("player 1"), Board(10, 10))
        board2 = BattleBoard(User("player 2"), Board(10, 10))
        logger = AndroidLogger()
        gameplay = BasicGameplay(BasicGameUtils(logger, true))
        conveter = BasicBoardToUiConverter(logger)
        width = board1.board.width
    }

    fun setupShips(): GameState {
        for (i in 1..5) {
            gameplay.setupBattle(board1, Ship(i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL )))
            gameplay.setupBattle(board2, Ship(i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL )))
        }

        return GameState(false, conveter.convert(board1))
    }

    fun play(): Observable<GameState> {
        pause()
        paused = false
        _play(board1, board2)
        return publisher
    }

    fun pause() {
        paused = true
        d?.dispose()
    }

    private fun _play(offense: BattleBoard, defense: BattleBoard) {
        d = Observable.just(1)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { playMove(offense, defense) }
                .map { getGameState() }
                .doOnNext({
                    publisher.onNext(it)
                })
                .subscribe {
                    logger.print("game", "over : ${it.over}")
                    if (!it.over && !paused) {
                        _play(defense, offense)
                    }
                }
    }

    private fun getGameState(): GameState {
        return GameState(board1.didUserLose || board2.didUserLose, conveter.convert(board1))
    }

    private fun playMove(offense: BattleBoard, defense: BattleBoard): Boolean {
        val result = gameplay.play(Move(offense, defense, Point(random.nextInt(10), random.nextInt(10))))

        if (result.events.size == 1) {
            if (result.events[0].type == EventType.INVALID) {
                return playMove(offense, defense)
            }
        }

        for (event in result.events) {
            if (event.type == EventType.LOST) {
                defense.didUserLose = true
                break
            }
        }

        logger.print("move result", result.toString())

        gameplay.showBoards(board1, logger)
        gameplay.showBoards(board2, logger)

        return true
    }

    class GameState(val over: Boolean, val cells: ArrayList<UiCell>)
}