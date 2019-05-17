package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.CategoryBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.mvp.ui.fragment.CategoryFragment

/**
 * 发现-分类Presenter
 */
class CategoryPresenter : BasePresenter<CategoryFragment, MainModel>() {

    /**
     * 获取分类列表
     */
    fun queryCategoryData() {
        model.getCategoryData()
            .compose(NetTransformer())
            .compose(view.getMultipleStatusViewTransformer())
            .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
            .subscribe(object : RxSubscribe<ArrayList<CategoryBean>>() {
                override fun call(result: ArrayList<CategoryBean>) {
                    //更新UI列表
                    view.updateListUi(result)
                }
            })
    }


}