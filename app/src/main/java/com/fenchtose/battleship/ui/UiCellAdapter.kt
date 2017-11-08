package com.fenchtose.battleship.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fenchtose.battleship.R

class UiCellAdapter(context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val cells = ArrayList<UiCell>()
    val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UiCellViewHolder(inflater.inflate(R.layout.square_cell_itemview, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as UiCellViewHolder
        holder.bind(cells[position])
    }

    override fun getItemId(position: Int): Long {
        return cells[position].hashCode().toLong()
    }

    class UiCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView as SquareCell

        fun bind(cell: UiCell) {
            view.hasShip = cell.hasShip
            view.isHit = cell.cell.opponentHit
            view.shipDirection = cell.direction
            view.userDidHit = cell.cell.userHit
            view.userDidMiss = cell.cell.userMiss
            view.opponentDidMiss = cell.cell.opponentMiss
            view.invalidate()
        }
    }
}