package com.jcarrasco96.une2024.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jcarrasco96.une2024.UNE2024
import com.jcarrasco96.une2024.databinding.ItemMeterBinding
import com.jcarrasco96.une2024.models.Meter
import com.jcarrasco96.une2024.utils.Utils

class MeterAdapter : GenericAdapter<Meter, MeterAdapter.ViewHolder>() {

    private val meterDao = UNE2024.db.meterDao()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMeterBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val viewLayout = ItemMeterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).root

        return ViewHolder(viewLayout)
    }

    override fun bind(holder: ViewHolder, data: Meter) {
        holder.binding.itemRead.text = data.idMeter
        holder.binding.itemConsume.text = data.name

        holder.binding.btnCopy.setOnClickListener {
            Utils.copyToClipboard(holder.binding.root.context, data.idMeter)
        }
        holder.binding.btnDelete.setOnClickListener {
            delete(data)
        }
    }

    override fun refresh() {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
        items = meterDao.all() as ArrayList<Meter>
        notifyItemRangeInserted(0, items.size)
    }

    override fun add(data: Meter) {
        super.add(data)
        meterDao.insert(data)
    }

    override fun delete(data: Meter) {
        super.delete(data)
        meterDao.delete(data)
    }

    override fun update(data: Meter) {
        super.update(data)
        meterDao.update(data)
    }

}