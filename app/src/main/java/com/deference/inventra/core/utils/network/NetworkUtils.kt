package com.deference.inventra.core.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.deference.inventra.Inventra

fun isInternetAvailable(): Boolean {
    val connectivityManager = Inventra.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

fun getUserAgent(context: Context): String {
    val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName ?: "unknown"
    val osVersion = "Android ${Build.VERSION.RELEASE}"
    val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
    return "$appName/$versionName ($osVersion; $deviceModel)"
}