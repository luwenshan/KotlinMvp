package com.lws.kotlinmvp

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.lws.kotlinmvp.utils.ContextUtil
import com.lws.kotlinmvp.utils.DisplayManager
import com.lws.kotlinmvp.utils.MLog
import com.squareup.leakcanary.LeakCanary

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        ContextUtil.setContext(this)

        MLog.init()//初始化日志
        DisplayManager.init(this)//初始化UI显示工具类
    }
}