package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.common.ExtraKey
import com.lws.kotlinmvp.common.exception.ExceptionHandle
import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.CategoryBean
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.lws.kotlinmvp.mvp.ui.activity.CategoryDetailActivity

/**
 * 分类详情列表Presenter
 */
class CategoryDetailPresenter : BasePresenter<CategoryDetailActivity, MainModel>() {

    private lateinit var categoryData: CategoryBean
    private var issue: HomeBean.Issue? = null
    private var nextPageUrl: String? = null

    /**
     * 初始化
     */
    fun init() {
        if (!view.intent.hasExtra(ExtraKey.CATEGORY_DATA)) {
            //判断参数
            view.showToast("参数错误")
            view.backward()
            return
        }

        categoryData = view.intent.getSerializableExtra(ExtraKey.CATEGORY_DATA) as CategoryBean
        view.updateTopUi(categoryData)
        queryCategoryDetailList()
    }

    /**
     * 获取详情列表第一页数据
     */
    fun queryCategoryDetailList() {
        model.getCategoryDetailList(categoryData.id)
            .compose(NetTransformer())
            .compose(view.getMultipleStatusViewTransformer())
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                override fun call(result: HomeBean.Issue) {
                    issue = result
                    nextPageUrl = result.nextPageUrl
                    view.updateListUi(issue!!)
                }
            })
    }


    /**
     * 获取详情列表更多数据
     */
    fun queryMoreCategoryData() {
        model.getMoreCategoryData(nextPageUrl!!)
            .compose(NetTransformer())
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {

                override fun call(result: HomeBean.Issue) {
                    issue!!.itemList.addAll(result.itemList)
                    view.updateListUi(issue!!)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showToast(ExceptionHandle.handleExceptionMsg(e))
                }
            })
    }

}