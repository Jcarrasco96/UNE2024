package com.jcarrasco96.une2024.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.databinding.DialogCanPayBinding
import com.jcarrasco96.une2024.utils.CalculateConsumption

class CanPayBottomSheetDialog : AbstractBottomSheetDialogFragment() {

    companion object {
        const val TAG = "CanPayBottomSheetDialog"
    }

    private lateinit var _binding: DialogCanPayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogCanPayBinding.inflate(inflater, container, false)

        _binding.btnCalculate.setOnClickListener {
            val text = _binding.money.text.toString()

            if (text.isNotEmpty()) {
                val money = text.toDouble()
                val spend = CalculateConsumption.canPay(money)

                if (spend == -1) {
                    _binding.tvSpend.setText(R.string.more_5000)
                } else {
                    _binding.tvSpend.text = getString(R.string.minus_5000, spend)
                }
            } else {
                _binding.tvSpend.setText(R.string.read_all)
            }
        }

        return _binding.root
    }

}