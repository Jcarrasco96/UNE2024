package com.jcarrasco96.une2024.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jcarrasco96.une2024.adapters.RegistryAdapter
import com.jcarrasco96.une2024.databinding.ActivityRegistryBinding
import com.jcarrasco96.une2024.utils.Preferences

class RegistryActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRegistryBinding
    private lateinit var registryAdapter: RegistryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Preferences.nightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        _binding = ActivityRegistryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        registryAdapter = RegistryAdapter()
        registryAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
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
        _binding.recyclerview.adapter = registryAdapter

        _binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun loadData() {
        _binding.swipeRefresh.isRefreshing = true
        registryAdapter.refresh()
        _binding.swipeRefresh.isRefreshing = false
    }

    private fun verifyItems() {
        if (registryAdapter.itemCount > 0) {
            _binding.tvNoItems.visibility = View.GONE
            _binding.recyclerview.visibility = View.VISIBLE
        } else {
            _binding.tvNoItems.visibility = View.VISIBLE
            _binding.recyclerview.visibility = View.GONE
        }
    }

}