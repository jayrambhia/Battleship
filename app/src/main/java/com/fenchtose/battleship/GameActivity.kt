package com.fenchtose.battleship

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.fenchtose.battleship.converter.BasicBoardToUiConverter
import com.fenchtose.battleship.demo.RxGame
import com.fenchtose.battleship.demo.SuperBasicGame
import com.fenchtose.battleship.logger.AndroidLogger
import com.fenchtose.battleship.ui.UiCellAdapter

class GameActivity : AppCompatActivity() {

    var game: RxGame? = null
    var adapter: UiCellAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = UiCellAdapter(this)
        adapter.setHasStableIds(true)

        val logger = AndroidLogger()
        val converter = BasicBoardToUiConverter(logger)

//        val game = SuperBasicGame(Handler(), object: SuperBasicGame.MoveCallback {
//            override fun onMovePlayed(gmae: SuperBasicGame) {
//                adapter.cells.clear()
//                adapter.cells.addAll(converter.convert(gmae.board1))
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onGameEnd() {
//                Toast.makeText(applicationContext, "Game end", Toast.LENGTH_SHORT).show()
//            }
//        })
        val game = RxGame()
        adapter.cells.addAll(game.setupShips().cells)

        recyclerview.layoutManager = GridLayoutManager(this, game.width)
        recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

        this.game = game
        this.adapter = adapter

//        val game = DemoGame()
//        game.setupShips()
//        game.showBoards()
//        game.play()


    }

    override fun onResume() {
        super.onResume()
        game?.let {
            it.play()
                    .doOnNext {
                        if (it.over) {
                            Toast.makeText(baseContext, "Game over!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .subscribe {
                        val state = it
                        adapter?.let {
                            it.cells.clear()
                            it.cells.addAll(state.cells)
                            it.notifyDataSetChanged()
                        }
                    }
        }

    }

    override fun onPause() {
        super.onPause()
        game?.pause()
    }
}
