package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.lws.kotlinmvp.mvp.ui.activity.WatchHistoryActivity
import java.util.*

/**
 * 观看记录 Presneter
 */
class WatchHistoryPresenter : BasePresenter<WatchHistoryActivity, MainModel>() {

    /**
     * 查询观看记录列表数据
     */
    fun queryWatchHistory() {
        model.getWatchHistory()
            .compose(NetTransformer())
            .compose(view.getMultipleStatusViewTransformer())
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<ArrayList<HomeBean.Issue.Item>>() {
                override fun call(result: ArrayList<HomeBean.Issue.Item>) {
                    view.updateListUi(result)
                }
            })
    }

}