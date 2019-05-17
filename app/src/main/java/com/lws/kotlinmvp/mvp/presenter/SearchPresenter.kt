package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.common.exception.ExceptionHandle
import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.lws.kotlinmvp.rx.transformer.ProgressTransformer
import com.lws.kotlinmvp.mvp.ui.activity.SearchActivity

/**
 * 搜索Presenter
 */
class SearchPresenter : BasePresenter<SearchActivity, MainModel>() {

    private var keyWords: String? = null//关键字
    private var nextPageUrl: String? = null//下一页Url
    private var issue: HomeBean.Issue? = null//查询结果

    /**
     * 请求热门关键词的数据
     */
    fun queryHotWordData() {
        model.getHotWordData()
            .compose(NetTransformer())
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<ArrayList<String>>() {
                override fun call(result: ArrayList<String>) {
                    view.setHotWordData(result)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showToast(ExceptionHandle.handleExceptionMsg(e))
                }
            })
    }

    /**
     * 查询关键字
     */
    fun querySearchResult(keyStr: String) {
        keyWords = keyStr

        model.getSearchResult(keyWords!!)
            .compose(NetTransformer())
            .compose(ProgressTransformer(view))
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                override fun call(result: HomeBean.Issue) {
                    issue = result
                    if (result.count > 0 && result.itemList.size > 0) {
                        nextPageUrl = result.nextPageUrl
                        view.updateSearchResultUi(result, keyStr)
                    } else
                        view.setEmptyView()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showToast(ExceptionHandle.handleExceptionMsg(e))
                }
            })
    }

    /**
     * 加载更多
     */
    fun loadMoreData() {
        model.getSearchIssueData(nextPageUrl!!)
            .compose(NetTransformer())
            .compose(bindUntilOnDestroyEvent())
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                override fun call(result: HomeBean.Issue) {
                    issue!!.itemList.addAll(result.itemList)
                    nextPageUrl = result.nextPageUrl
                    view.updateSearchResultUi(issue!!, keyWords!!)
                }
            })
    }
}