package com.zkatemor.movies.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zkatemor.movies.util.ConnectivityReceiver
import com.zkatemor.movies.util.SnackBar
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {
            val error_snackbar = SnackBar(main_layout)
            error_snackbar.show(this)
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }
}