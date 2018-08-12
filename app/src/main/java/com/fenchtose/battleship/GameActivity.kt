package com.fenchtose.battleship

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import com.fenchtose.battleship.converter.BasicBoardToUiConverter
import com.fenchtose.battleship.converter.BoardToUiConverter
import com.fenchtose.battleship.logger.AndroidLogger
import com.fenchtose.battleship.models.*
import com.fenchtose.battleship.redux.Dispatch
import com.fenchtose.battleship.redux.Unsubscribe
import com.fenchtose.battleship.ui.UiCellAdapter
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var adapter: UiCellAdapter
    private lateinit var gamestateInfo: TextView

    private lateinit var handler: Handler

    private lateinit var store: Gamestore
    private lateinit var myBoard: Board
    private lateinit var otherBoard: Board

    private lateinit var converter: BoardToUiConverter
    private var unsubscribe: Unsubscribe? = null
    private var dispatch: Dispatch? = null

    private var gameOver: Boolean = false

    private val width: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        handler = Handler()

        gamestateInfo = findViewById(R.id.gamestate_info)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = UiCellAdapter(this, {
            if (gameOver) {
                return@UiCellAdapter
            }
            dispatch?.invoke(GameAction.Move(myBoard.id, otherBoard.id, it.point))
        })

        adapter.setHasStableIds(true)

        val logger = AndroidLogger()
        converter = BasicBoardToUiConverter(logger)

        recyclerview.layoutManager = GridLayoutManager(this, width)
        recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()


        myBoard = Board(0, User("Player 1"), width, width)
        otherBoard = Board(1, User("Player 2"), width, width)

        val initState = GameState(
                board1 = myBoard,
                board2 = otherBoard,
                lastPlayed = otherBoard.id
        )

        store = Gamestore(initState)
        setupGame()
    }

    override fun onResume() {
        super.onResume()
        unsubscribe = store.subscribe({ state, dispatch ->
            this.dispatch = dispatch
            myBoard = state.board1
            otherBoard = state.board2

            val start = System.currentTimeMillis()
            val cells = converter.convert(myBoard)
            val end = System.currentTimeMillis()

            Log.d("Render", "time to convert to cells: ${end-start} ms")

            adapter.cells.clear()
            adapter.cells.addAll(cells)
            adapter.notifyDataSetChanged()

            gamestateInfo.text = if (state.lastPlayed == otherBoard.id) "Your turn" else "Player 2 turn"
            gameOver = state.gameOver

            if (state.gameOver) {
                gamestateInfo.text = "Game over!"
            }

            if(state.lastPlayed == myBoard.id && !state.gameOver) {
                handler.postDelayed({
                    playOther()
                }, 300)
            }

        })

    }

    override fun onPause() {
        super.onPause()
        unsubscribe?.invoke()
    }

    private fun setupGame() {
        for (i in 1..5) {
            store.dispatch(AddShip(myBoard.id, Ship(i, i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL ))))
            store.dispatch(AddShip(otherBoard.id, Ship(10 + i, i, Point(i, i), (if (i%2 == 0) Direction.HORIZONTAL else Direction.VERTICAL ))))
        }
    }

    private fun playOther() {
        val random =  Random()
        while(true) {
            val point = Point(random.nextInt(width), random.nextInt(width))
            if (!otherBoard.hits.contains(point) && !otherBoard.missed.contains(point)) {
                dispatch?.invoke(GameAction.Move(otherBoard.id, myBoard.id, point))
                break
            }
        }
    }
}
