package com.jcarrasco96.une2024.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceClickListener
import androidx.preference.PreferenceFragmentCompat
import com.jcarrasco96.une2024.R
import com.jcarrasco96.une2024.UNE2024
import com.jcarrasco96.une2024.models.ApiDataJson
import com.jcarrasco96.une2024.models.Meter
import com.jcarrasco96.une2024.models.Registry
import com.jcarrasco96.une2024.services.API
import com.jcarrasco96.une2024.utils.NetworkUtils
import com.jcarrasco96.une2024.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var versionName: String = ""

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            versionName = Utils.versionApp(requireActivity().packageManager, requireActivity().packageName)

            findPreference<Preference>("key_export")?.onPreferenceClickListener = OnPreferenceClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (!NetworkUtils.isHostReachable()) {
                        return@launch
                    }

                    val registry = UNE2024.db.registryDao().all() as ArrayList<Registry>
                    val meter = UNE2024.db.meterDao().all() as ArrayList<Meter>
                    val uuid = Utils.androidId(requireContext())

                    try {
                        val response = API.save(ApiDataJson(uuid, registry, meter))

                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), response.message, Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("EXCEPTION", e.message.toString())

                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), e.message, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                true
            }

            findPreference<Preference>("key_import")?.onPreferenceClickListener = OnPreferenceClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (!NetworkUtils.isHostReachable()) {
                        return@launch
                    }

                    try {
                        val response = API.view(requireContext())

                        UNE2024.db.registryDao().deleteAll()
                        UNE2024.db.meterDao().deleteAll()

                        if (response.registry != null) {
                            response.registry.forEach {
                                UNE2024.db.registryDao().insert(it)
                            }
                        }
                        if (response.meter != null) {
                            response.meter.forEach {
                                UNE2024.db.meterDao().insert(it)
                            }
                        }

                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), R.string.backup_complete, Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("EXCEPTION", e.message.toString())

                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), e.message, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                true
            }

            findPreference<Preference>("key_app_name")?.summary = getString(R.string.version, versionName)

            findPreference<Preference>("key_send_feedback")?.onPreferenceClickListener = OnPreferenceClickListener {
                Utils.sendFeedback(requireContext(), versionName)
                true
            }

            findPreference<Preference>("key_portfolio")?.onPreferenceClickListener = OnPreferenceClickListener {
                startActivity(Intent(
                    "android.intent.action.VIEW", Uri.parse("https://jcarrasco96.com/")
                ))
                true
            }

            findPreference<Preference>("key_telegram")?.onPreferenceClickListener = OnPreferenceClickListener {
                val intent = Intent("android.intent.action.VIEW", Uri.parse("https://t.me/une2024"))
                try {
                    if (requireActivity().packageManager.getPackageInfo("org.telegram.messenger.web", 0) != null) {
                        intent.setPackage("org.telegram.messenger.web")
                    }
                } catch (_: PackageManager.NameNotFoundException) {

                }

                try {
                    if (requireActivity().packageManager.getPackageInfo("org.telegram.messenger", 0) != null) {
                        intent.setPackage("org.telegram.messenger")
                    }
                } catch (_: PackageManager.NameNotFoundException) {

                }
                startActivity(intent)

                true
            }
        }

    }

}