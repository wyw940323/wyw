package com.example.administrator.kotlinexample.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.wyw.search.historicalsearch.MyApplication

/**
 * Created by Administrator on 2018/9/10.
 */

class NetWorkUtil {
    companion object {
        @SuppressLint("MissingPermission")
        fun isNetWorkConnected(): Boolean {
            val cm =  MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val currentNet = cm.activeNetworkInfo ?: return false
            return currentNet.isAvailable
        }
    }

}