package com.fenchtose.battleship.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fenchtose.battleship.R

class UiCellAdapter(context: Context, private val onClick: ((Cell) -> Unit)): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val cells = ArrayList<Cell>()
    private val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UiCellViewHolder(inflater.inflate(R.layout.square_cell_itemview, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as UiCellViewHolder
        holder.bind(cells[position])
    }

    override fun getItemId(position: Int): Long {
        return cells[position].hashCode().toLong()
    }

    class UiCellViewHolder(itemView: View, onClick: (Cell) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val view = itemView as SquareCell
        private var cell: Cell? = null

        init {
            view.setOnClickListener {
                cell?.let(onClick)
            }
        }

        fun bind(cell: Cell) {
            this.cell = cell
            view.bind(cell)
        }
    }
}