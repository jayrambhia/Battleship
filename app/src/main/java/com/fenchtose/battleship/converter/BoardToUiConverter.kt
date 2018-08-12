package com.fenchtose.battleship.converter

import com.fenchtose.battleship.models.Board
import com.fenchtose.battleship.ui.UiCell

interface BoardToUiConverter {
    fun convert(board: Board): ArrayList<UiCell>
}