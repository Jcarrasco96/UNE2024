package com.jcarrasco96.une2024.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.adapters.MeterAdapter
import com.jcarrasco96.une2024.databinding.ActivityMeterBinding
import com.jcarrasco96.une2024.dialogs.AddMeterBottomSheetDialog
import com.jcarrasco96.une2024.interfaces.AddMeterInterface
import com.jcarrasco96.une2024.models.Meter
import com.jcarrasco96.une2024.utils.Preferences

class MeterActivity : AppCompatActivity(),
    AddMeterInterface {

    private lateinit var _binding: ActivityMeterBinding
    private lateinit var meterAdapter: MeterAdapter
    private var isFabHide: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Preferences.nightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        _binding = ActivityMeterBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.fabAdd.setOnClickListener {
            supportFragmentManager.let {
                AddMeterBottomSheetDialog(this).show(it, AddMeterBottomSheetDialog.TAG)
            }
        }

        meterAdapter = MeterAdapter()
        meterAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                verifyItems()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                verifyItems()
            }
        })

        _binding.recyclerview.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        _binding.recyclerview.setHasFixedSize(true)
        _binding.recyclerview.adapter = meterAdapter
        _binding.recyclerview.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY < oldScrollY) {
                animateFab(false)
            }
            if (scrollY > oldScrollY) {
                animateFab(true)
            }
        }

        _binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_meter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.mnu_auth_bandec -> makeCall(AUTH_B_NUMBER, REQUEST_AUTH_B_PERMISSION)
            R.id.mnu_auth_bm -> makeCall(AUTH_BM_NUMBER, REQUEST_AUTH_BM_PERMISSION)
            R.id.mnu_auth_bpa -> makeCall(AUTH_BPA_NUMBER, REQUEST_AUTH_BPA_PERMISSION)
            R.id.mnu_pay -> makeCall(PAY_NUMBER, REQUEST_PAY_PERMISSION)
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun loadData() {
        _binding.swipeRefresh.isRefreshing = true
        meterAdapter.refresh()
        _binding.swipeRefresh.isRefreshing = false
    }

    override fun onAdd(idMeter: String, name: String) {
        val meter = Meter(idMeter = idMeter, name = name)

        meterAdapter.add(meter)
    }

    private fun animateFab(hide: Boolean) {
        if (isFabHide && hide || !isFabHide && !hide) return

        isFabHide = hide

        val moveY = if (hide) 2 * _binding.fabAdd.height else 0

        _binding.fabAdd.animate()
            .translationY(moveY.toFloat())
            .setStartDelay(100)
            .setDuration(500)
            .start()
    }

    private fun makeCall(number: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE), requestCode
            )
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + Uri.encode(number))
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            return
        }

        when (requestCode) {
            REQUEST_AUTH_B_PERMISSION -> makeCall(AUTH_B_NUMBER, REQUEST_AUTH_B_PERMISSION)
            REQUEST_AUTH_BPA_PERMISSION -> makeCall(AUTH_BPA_NUMBER, REQUEST_AUTH_BPA_PERMISSION)
            REQUEST_AUTH_BM_PERMISSION -> makeCall(AUTH_BM_NUMBER, REQUEST_AUTH_BM_PERMISSION)
            REQUEST_PAY_PERMISSION -> makeCall(PAY_NUMBER, REQUEST_PAY_PERMISSION)
        }
    }

    companion object {
        private const val REQUEST_AUTH_B_PERMISSION = 1
        private const val REQUEST_AUTH_BPA_PERMISSION = 2
        private const val REQUEST_AUTH_BM_PERMISSION = 3
        private const val REQUEST_PAY_PERMISSION = 4

        private const val AUTH_B_NUMBER = "*444*40*02#"
        private const val AUTH_BPA_NUMBER = "*444*40*01#"
        private const val AUTH_BM_NUMBER = "*444*40*03#"
        private const val PAY_NUMBER = "*444*41#"
    }

    private fun verifyItems() {
        if (meterAdapter.itemCount > 0) {
            _binding.tvNoItems.visibility = View.GONE
            _binding.recyclerview.visibility = View.VISIBLE
        } else {
            _binding.tvNoItems.visibility = View.VISIBLE
            _binding.recyclerview.visibility = View.GONE
        }
    }

}