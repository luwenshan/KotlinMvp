package com.lws.kotlinmvp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL

class NetworkUtil {
    companion object {
        var NET_CNNT_BAIDU_OK = 1 // NetworkAvailable
        var NET_CNNT_BAIDU_TIMEOUT = 2 // no NetworkAvailable
        var NET_NOT_PREPARE = 3 // Net no ready
        var NET_ERROR = 4 //net error
        private val TIMEOUT = 3000 // TIMEOUT

        @JvmStatic
        fun isNetworkAvailable(context: Context): Boolean {
            val manager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return info != null && info.isAvailable
        }

        /**
         * 获取IP地址
         */
        @JvmStatic
        fun getLocalIpAddress(): String {
            var ret = ""
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val enumIpAddress = en.nextElement().inetAddresses
                    while (enumIpAddress.hasMoreElements()) {
                        val netAddress = enumIpAddress.nextElement()
                        if (!netAddress.isLoopbackAddress) {
                            ret = netAddress.hostAddress.toString()
                        }
                    }
                }
            } catch (ex: SocketException) {
                ex.printStackTrace()
            }
            return ret
        }

        /**
         * ping "http://www.baidu.com"
         */
        @JvmStatic
        private fun pingNetwork(): Boolean {
            var result = false
            var httpUrl: HttpURLConnection? = null
            try {
                httpUrl = URL("http://www.baidu.com").openConnection() as HttpURLConnection
                httpUrl.connectTimeout = TIMEOUT
                httpUrl.connect()
                result = true
            } catch (e: IOException) {
            } finally {
                httpUrl?.disconnect()
            }
            return result
        }

        /**
         * 是否移动网络
         */
        @JvmStatic
        fun isMobileNetwork(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
        }

        @JvmStatic
        fun isWifi(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * is2G
         */
        @JvmStatic
        fun is2G(context: Context): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE
                    || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .subtype == TelephonyManager.NETWORK_TYPE_CDMA)
        }

        /**
         * is wifi on
         */
        @JvmStatic
        fun isWifiEnabled(context: Context): Boolean {
            val mgrConn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mgrTel = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return mgrConn.activeNetworkInfo != null && mgrConn
                .activeNetworkInfo.state == NetworkInfo.State.CONNECTED || mgrTel
                .networkType == TelephonyManager.NETWORK_TYPE_UMTS
        }
    }
}