package com.lws.kotlinmvp.rx.transformer

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 下拉刷新或上拉加载结束状态切换事务，免去在接口回调中添加控制逻辑
 */
class SmartRefreshTransformer<T>(private var smartRefreshLayout: SmartRefreshLayout?) : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.observeOn(AndroidSchedulers.mainThread())
            .doOnError { refreshComplete() }
            .doOnComplete { refreshComplete() }
    }

    /**
     * 加载完成，结束下拉刷新或上拉加载
     */
    private fun refreshComplete() {
        smartRefreshLayout?.finishRefresh()
        smartRefreshLayout?.finishLoadmore()
        smartRefreshLayout = null
    }
}