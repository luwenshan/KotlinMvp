package com.lws.kotlinmvp.mvp.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.mvp.base.MvpFragment
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.mvp.presenter.FollowPresenter
import com.lws.kotlinmvp.mvp.ui.activity.VideoDetailActivity
import com.lws.kotlinmvp.mvp.ui.adapter.FollowAdapter
import com.lws.kotlinmvp.rx.transformer.MultipleStatusViewTransformer
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.layout_recyclerview.*

/**
 * 发现-关注 UI
 */
class FollowFragment : MvpFragment<FollowPresenter>() {

    private val followAdapter = FollowAdapter()

    private var loadingMore = false//是否正在加载更多

    companion object {
        fun getInstance() = FollowFragment()
    }

    override fun getP(): FollowPresenter = FollowPresenter().apply { view = this@FollowFragment }

    override fun getLayoutId(): Int = R.layout.layout_recyclerview

    /**
     * 获取加载进度切换事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = followAdapter
        //实现自动加载
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager?.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (itemCount != null) {
                    if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                        loadingMore = true
                        presenter.queryFollowList()
                    }
                }
            }
        })
    }

    /**
     * 添加点击事件
     */
    override fun addListener() {
        super.addListener()
        followAdapter.onItemClick = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val itemData = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(activity!!, view, itemData)
        }

        //异常布局，点击重新加载
        multipleStatusView.setOnClickListener {
            presenter.queryFollowList()
        }
    }

    /**
     * 懒加载数据
     */
    override fun onLazyLoadOnce() {
        super.onLazyLoadOnce()
        presenter.queryFollowList()
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(issue: HomeBean.Issue) {
        loadingMore = false
        followAdapter.setNewData(issue.itemList)
    }

}