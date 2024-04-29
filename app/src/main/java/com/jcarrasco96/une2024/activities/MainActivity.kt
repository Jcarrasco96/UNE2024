package com.jcarrasco96.une2024.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.UNE2024
import com.jcarrasco96.une2024.databinding.ActivityMainBinding
import com.jcarrasco96.une2024.dialogs.CanPayBottomSheetDialog
import com.jcarrasco96.une2024.models.Registry
import com.jcarrasco96.une2024.utils.CalculateConsumption
import com.jcarrasco96.une2024.utils.Preferences
import com.jcarrasco96.une2024.utils.Utils
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private val nf: NumberFormat = DecimalFormat("#,##0.00")

    private var modeRead: Boolean = true

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Preferences.nightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.lastRead.setText(Preferences.read().toString())
        _binding.consumo.setText("0")

        _binding.btnCalcular.setOnClickListener {
            if (modeRead) {
                val lastRead = _binding.lastRead.text.toString().toLongOrNull()
                val read = _binding.read.text.toString().toLongOrNull()

                if (lastRead != null && read != null && lastRead < read) {
                    val registry = Registry(read = read, lastRead = lastRead)

                    UNE2024.db.registryDao().insert(registry)

                    Preferences.read(read)
                    calculate(read - lastRead)
                } else {
                    if (lastRead == null || read == null) {
                        Toast.makeText(this, getString(R.string.read_all), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, getString(R.string.read_gt), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val consume = _binding.consumo.text.toString().toLongOrNull()

                if (consume != null) {
                    calculate(_binding.consumo.text.toString().toLong())
                } else {
                    Toast.makeText(this, getString(R.string.read_all), Toast.LENGTH_SHORT).show()
                }
            }
        }

        _binding.btnFlipMode.setOnClickListener {
            Utils.flip(_binding.layoutLectura, _binding.layoutConsumo)
            modeRead = !modeRead
        }

        calculate(0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnu_registry -> startActivity(Intent("com.jcarrasco96.une2024.REGISTRY"))
            R.id.mnu_meter -> startActivity(Intent("com.jcarrasco96.une2024.METER"))
            R.id.mnu_spend -> showDialogSpend()
            R.id.mnu_dark_mode -> {
                val newNightModeB = AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(if (newNightModeB) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                Preferences.nightMode(newNightModeB)
                recreate()
            }
            R.id.mnu_settings -> startActivity(Intent("com.jcarrasco96.une2024.SETTINGS"))

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun showDialogSpend() {
        supportFragmentManager.let {
            CanPayBottomSheetDialog().show(it, CanPayBottomSheetDialog.TAG)
        }
    }

    private fun calculate(consumption: Long) {
        _binding.tableLayout.removeAllViews()

        val arrayListHeader = ArrayList<String>().apply {
            addAll(resources.getStringArray(R.array.header_table))
        }

        _binding.tableLayout.addHeader(arrayListHeader)

        val consumptionByStep = CalculateConsumption.kWhXSteps(consumption)
        val total = CalculateConsumption.calculateAmount(consumptionByStep)

        val ranges = resources.getStringArray(R.array.ranges)

        for (i in consumptionByStep.indices) {
            val elementos = arrayListOf(
                ranges[i],
                consumptionByStep[i].toString(),
                nf.format(CalculateConsumption.AMOUNT[i]),
                nf.format(consumptionByStep[i] * CalculateConsumption.AMOUNT[i])
            )
            _binding.tableLayout.addRow(elementos)
        }

        _binding.consKwh.text = getString(R.string.consume_kwh, consumption)
        _binding.importe.text = getString(R.string.amount_cup, nf.format(total))
    }

}