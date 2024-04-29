package com.jcarrasco96.une2024.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.jcarrasco96.une2024.R

class CustomTable: TableLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val layoutRowParams: TableRow.LayoutParams = TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f
    )

    fun addRow(data: ArrayList<String>) {
        addData(data)
    }

    fun addHeader(data: ArrayList<String>) {
        addData(data, true)
    }

    private fun addData(data: ArrayList<String>, isHeader: Boolean = false) {
        val row = TableRow(context)
        row.layoutParams = layoutRowParams

        for (i in data.indices) {
            val textView = TextView(context)
            textView.text = data[i]
            textView.gravity = Gravity.CENTER

            if (isHeader) {
                textView.setTextAppearance(R.style.tableHeader)
                textView.setBackgroundResource(R.drawable.table_header)
            } else {
                textView.setTextAppearance(R.style.tableRow)
                textView.setBackgroundResource(R.drawable.table_row)
            }

            textView.layoutParams = layoutRowParams
            row.addView(textView)
        }

        addView(row)
    }

    fun removeRow(index: Int) {
        removeViewAt(index)
    }

}