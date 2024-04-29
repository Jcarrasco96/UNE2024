package com.jcarrasco96.une2024.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class GenericAdapter<T, AH : RecyclerView.ViewHolder> : RecyclerView.Adapter<AH>() {

    var items = ArrayList<T>()
    lateinit var context: Context

    override fun onBindViewHolder(holder: AH, position: Int) {
        bind(holder, items[position])
    }

    override fun getItemCount(): Int = items.size

    abstract fun bind(holder: AH, data: T)

    abstract fun refresh()

    open fun add(data: T) {
        items.add(data)
        notifyItemInserted(items.size)
    }

    open fun delete(data: T) {
        val position = items.indexOf(data)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun update(data: T) {
        val position = items.indexOf(data)
        notifyItemChanged(position)
    }

}