package com.zkatemor.movies.util

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormatSymbols

class Tools {
    companion object {
        fun convertDate(sDate: String?): String {
            var result = ""

            var month = ""
            var day = ""
            var year = ""


            if (sDate != null) {
                month += sDate!!.substring(5, 7)
                day += sDate!!.substring(8)
                year += sDate!!.substring(0, 4)
            }

            if (sDate != null) {
                result += day.toInt().toString()
                result += " " + DateFormatSymbols().getMonths()[month.toInt() - 1]
                result += " " + year.toInt().toString()
            }

            return result
        }

        private fun isConnectNetwork(context: Context): Boolean {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }

        fun isInternetAccess(context: Context): Boolean {
            return (isConnectNetwork(context) && isConnectGoogle())
        }

        private fun isConnectGoogle(): Boolean {
            try {
                val urlc = URL("http://www.google.com").openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Test")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 10000
                urlc.connect()
                return urlc.responseCode === 200
            } catch (e: IOException) {
                //log("IOException in connectGoogle())")
                return false
            }

        }
    }
}