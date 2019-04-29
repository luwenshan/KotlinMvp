package com.lws.kotlinmvp.rx

import android.os.Looper
import com.lws.kotlinmvp.common.exception.SubscribeException
import com.lws.kotlinmvp.utils.MLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class RxSubscribe<T> : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        try {
            call(t)
        } catch (e: Exception) {
            //这里统一捕获业务逻辑及UI更新异常，防止由于后端数据返回异常，导致app崩溃的问题
            onError(SubscribeException(e))
        }
    }

    override fun onError(e: Throwable) {
        MLog.e("isUiThread:${Looper.getMainLooper() == Looper.myLooper()}", e)
    }

    abstract fun call(result: T)
}