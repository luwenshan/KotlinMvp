package com.lws.kotlinmvp.mvp.presenter

import com.lws.kotlinmvp.common.exception.ExceptionHandle
import com.lws.kotlinmvp.mvp.base.BasePresenter
import com.lws.kotlinmvp.mvp.model.MainModel
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.rx.RxSubscribe
import com.lws.kotlinmvp.rx.transformer.EmptyTransformer
import com.lws.kotlinmvp.rx.transformer.NetTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.lws.kotlinmvp.mvp.ui.fragment.FollowFragment
import io.reactivex.ObservableTransformer

/**
 * 发现-关注Presenter
 */
class FollowPresenter : BasePresenter<FollowFragment, MainModel>() {

    private var issue: HomeBean.Issue? = null
    var nextPageUrl: String? = null

    /**
     * 是否为首页
     */
    private fun isFirstPage() = (issue == null)

    /**
     *  获取关注数据
     */
    fun queryFollowList() {

        val observable = if (nextPageUrl == null) {
            model.getFollowInfo()
        } else {
            model.getFollowIssueData(nextPageUrl!!)
        }

        val transformer: ObservableTransformer<HomeBean.Issue, HomeBean.Issue> =
            if (isFirstPage()) view.getMultipleStatusViewTransformer() else EmptyTransformer()

        observable
//                .flatMap { result ->
//                    //如果出现返回的数据类型不匹配，可以取消注释,也可以封装为事务
//                    //由于接口不能保证返回的类型，所以在这里对源数据进行处理，当然，也可以将该代码放在Model中进行处理
//                    val iterator = result.itemList.iterator()
//                    while (iterator.hasNext()) {
//                        if (iterator.next().type != "videoCollectionWithBrief") {
//                            iterator.remove()
//                        }
//                    }
//
//                    Observable.just(result)
//                }
            .compose(NetTransformer())
            .compose(transformer)
            .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
            .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                override fun call(result: HomeBean.Issue) {

                    if (issue == null) {//首页
                        issue = result
                    } else {//加载更多
                        issue!!.itemList.addAll(result.itemList)
                    }

                    nextPageUrl = result.nextPageUrl

                    view.updateListUi(issue!!)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showToast(ExceptionHandle.handleExceptionMsg(e))
                }
            })
    }

}