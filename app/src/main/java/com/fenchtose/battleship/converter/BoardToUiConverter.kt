package com.fenchtose.battleship.converter

import com.fenchtose.battleship.models.BattleBoard
import com.fenchtose.battleship.ui.UiCell

interface BoardToUiConverter {
    fun convert(board: BattleBoard): ArrayList<UiCell>
}