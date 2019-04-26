package com.lws.kotlinmvp.utils

import android.content.Context

class ContextUtil private constructor() {
    companion object {
        private var context: Context? = null

        fun setContext(cxt: Context) {
            context = cxt.applicationContext
        }

        fun getContext(): Context {
            return context!!
        }
    }
}