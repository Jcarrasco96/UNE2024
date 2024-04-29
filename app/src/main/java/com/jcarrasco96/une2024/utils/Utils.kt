package com.jcarrasco96.une2024.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jcarrasco96.une2024.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Calendar

object Utils {

    fun flip(vararg layouts: ViewGroup) {
        layouts.forEach { layout ->
            layout.visibility = if (layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    fun dateTime(): String {
        val nf: NumberFormat = DecimalFormat("00")
        val calendar = Calendar.getInstance()

        val d = nf.format(calendar.get(Calendar.DAY_OF_MONTH))
        val m = nf.format(calendar.get(Calendar.MONTH) + 1)
        val y = calendar.get(Calendar.YEAR)

        val h = nf.format(calendar.get(Calendar.HOUR))
        val mm = nf.format(calendar.get(Calendar.MINUTE))
        val s = nf.format(calendar.get(Calendar.SECOND))
        val am = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

        val date = "${d}-${m}-${y}"
        val time = "${h}:${mm}:${s} $am"

        return "$date $time"
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Meter", text)

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(context, R.string.text_copied, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("HardwareIds")
    fun androidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun versionApp(packageManager: PackageManager, packageName: String): String {
        return packageManager.getPackageInfo(packageName, 0).versionName
    }

    fun sendFeedback(context: Context, versionName: String) {
        val body: String = context.getString(
            R.string.body_email,
            Build.VERSION.RELEASE,
            versionName,
            Build.BRAND,
            Build.MODEL,
            Build.MANUFACTURER
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("jcarrasco96joker@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Problema!")
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, "Seleccione"))
    }

}