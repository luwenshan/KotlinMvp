package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.TabInfoBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.mvp.ui.fragment.HotFragment

/**
 * 热门 Presenter
 */
class HotPresenter : BasePresenter<HotFragment, MainModel>() {

    /**
     * 查询热门 Tab 数据
     */
    fun queryRankTabData() {
        model.getRankTabData()
            .compose(NetTransformer())
            .compose(view.getMultipleStatusViewTransformer())
            .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
            .subscribe(object : RxSubscribe<TabInfoBean>() {
                override fun call(result: TabInfoBean) {
                    view.updateTabUi(result)
                }
            })
    }


}