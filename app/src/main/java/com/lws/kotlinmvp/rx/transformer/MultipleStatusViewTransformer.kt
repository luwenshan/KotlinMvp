package com.lws.kotlinmvp.rx.transformer

import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.lws.kotlinmvp.common.exception.ExceptionHandle
import com.lws.multiplestatusview.MultipleStatusView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 进度、错误、内容切换View事务，免去在接口回调中添加控制逻辑
 */
class MultipleStatusViewTransformer<T>(private var multipleStatusView: MultipleStatusView?) : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { multipleStatusView?.showLoading() }
            .doOnError { onError(it) }
            .doOnNext { onNext() }
            .doOnComplete { setViewNull() }
    }

    private fun onError(e: Throwable) {
        if (ExceptionHandle.isExceptionCode(e, ErrorStatus.NETWORK_ERROR)) {
            multipleStatusView?.showNoNetwork()
        } else {
            multipleStatusView?.showError()
        }
        setViewNull()
    }

    /**
     * 加载成功，显示内容布局
     */
    private fun onNext() {
        multipleStatusView?.showContent()
    }

    /**
     * 置空
     */
    private fun setViewNull() {
        multipleStatusView = null
    }
}