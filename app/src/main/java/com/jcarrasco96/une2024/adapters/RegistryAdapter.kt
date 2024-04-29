package com.jcarrasco96.une2024.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.UNE2024
import com.jcarrasco96.une2024.databinding.ItemRegistryBinding
import com.jcarrasco96.une2024.models.Registry
import com.jcarrasco96.une2024.utils.CalculateConsumption
import java.text.DecimalFormat
import java.text.NumberFormat

class RegistryAdapter : GenericAdapter<Registry, RegistryAdapter.ViewHolder>() {

    private val registryDao = UNE2024.db.registryDao()
    private val nf: NumberFormat = DecimalFormat("#,##0.00")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRegistryBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val viewLayout = ItemRegistryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).root

        return ViewHolder(viewLayout)
    }

    override fun bind(holder: ViewHolder, data: Registry) {
        val drawableRes = if (!data.official) R.drawable.ic_star_off else R.drawable.ic_star_on

        holder.binding.btnMarkAsOfficial.setImageDrawable(
            ContextCompat.getDrawable(context, drawableRes)
        )
        holder.binding.itemRead.text =
            context.getString(R.string.registry_interval, data.lastRead, data.read)
        holder.binding.itemConsumo.text =
            context.getString(R.string.consume_interval, data.consume())
        holder.binding.itemImporte.text =
            context.getString(R.string.amount_interval, nf.format(CalculateConsumption.amount(data.consume())))
        holder.binding.itemDate.text = data.date

        holder.binding.btnMarkAsOfficial.setOnClickListener {
            data.official = !data.official
            update(data)
        }

        holder.binding.btnDelete.setOnClickListener {
            when (data.official) {
                true -> dialogDelete(data)
                false -> delete(data)
            }
        }
    }

    override fun refresh() {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
        items = registryDao.all() as ArrayList<Registry>
        notifyItemRangeInserted(0, items.size)
    }


    override fun add(data: Registry) {
        super.add(data)
        registryDao.insert(data)
    }

    override fun delete(data: Registry) {
        super.delete(data)
        registryDao.delete(data)
    }

    override fun update(data: Registry) {
        super.update(data)
        registryDao.update(data)
    }

    private fun dialogDelete(registry: Registry) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(R.string.delete_reg)
        builder.setMessage(R.string.delete_question)
        builder.setPositiveButton(R.string.delete) { _: DialogInterface, _: Int ->
            delete(registry)
        }
        builder.setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int -> }
        builder.show()
    }

}