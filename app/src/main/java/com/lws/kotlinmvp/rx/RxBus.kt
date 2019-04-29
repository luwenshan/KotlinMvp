package com.lws.kotlinmvp.rx

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

object RxBus {
    private val bus: FlowableProcessor<Any> = PublishProcessor.create()

    /**
     * 发送事件
     */
    fun send(obj: Any) {
        bus.onNext(obj)
    }

    /**
     * 在调用线程中订阅
     */
    fun <T> toObservable(clazz: Class<T>): Flowable<T> {
        return bus.ofType(clazz)
    }

    /**
     * 在主线程中订阅
     */
    fun <T> toObservableMain(clazz: Class<T>): Flowable<T> {
        return bus.ofType(clazz).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 是否存在订阅者
     */
    fun hasSubscribers(): Boolean {
        return bus.hasSubscribers()
    }
}