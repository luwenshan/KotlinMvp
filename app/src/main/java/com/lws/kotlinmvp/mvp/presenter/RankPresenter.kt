package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.common.ExtraKey
import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.lws.kotlinmvp.mvp.ui.fragment.RankFragment

/**
 * 热门-排行Presenter
 */
class RankPresenter : BasePresenter<RankFragment, MainModel>() {


    private var apiUrl: String? = null//排行请求url

    /**
     * 初始化
     */
    fun init() {
        apiUrl = view.arguments?.getString(ExtraKey.API_URL)
        if (apiUrl == null) {
            view.showToast("")
            view.backward()
            return
        }

        queryRankList()
    }


    /**
     * 查询排行列表数据
     */
    fun queryRankList() {
        model.getHotRankData(apiUrl!!)
            .compose(NetTransformer())
            .compose(view.getMultipleStatusViewTransformer())
            .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                override fun call(result: HomeBean.Issue) {
                    view.updateListUi(result.itemList)
                }
            })
    }

}