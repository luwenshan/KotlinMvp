package com.lws.kotlinmvp.rx

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerCompat private constructor() {
    companion object {
        fun <T> applyComputationSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyIoSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}