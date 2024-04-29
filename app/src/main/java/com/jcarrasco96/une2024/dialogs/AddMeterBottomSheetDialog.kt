package com.jcarrasco96.une2024.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.UNE2024
import com.jcarrasco96.une2024.databinding.DialogAddMeterBinding
import com.jcarrasco96.une2024.interfaces.AddMeterInterface

class AddMeterBottomSheetDialog(
    private val iAddMeter: AddMeterInterface
) : AbstractBottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddMeterBottomSheetDialog"
    }

    private lateinit var _binding: DialogAddMeterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddMeterBinding.inflate(inflater, container, false)

        _binding.btnSubmit.setOnClickListener {
            val idMeter = _binding.itemMeterId.text.toString()
            val name = _binding.itemMeterName.text.toString()

            when {
                idMeter.isNotEmpty() && name.isNotEmpty() -> {
                    if (!UNE2024.db.meterDao().exists(idMeter)) {
                        iAddMeter.onAdd(idMeter, name)
                        dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(), "Ya existe este contador", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                else -> Toast.makeText(requireContext(), R.string.read_all, Toast.LENGTH_SHORT).show()
            }
        }

        _binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }

}