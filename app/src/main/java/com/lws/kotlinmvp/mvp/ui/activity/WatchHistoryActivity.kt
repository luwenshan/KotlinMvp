package com.lws.kotlinmvp.mvp.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.mvp.base.MvpActivity
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.mvp.presenter.WatchHistoryPresenter
import com.lws.kotlinmvp.mvp.ui.adapter.WatchHistoryAdapter
import com.lws.kotlinmvp.rx.transformer.MultipleStatusViewTransformer
import com.lws.kotlinmvp.utils.StatusBarUtil
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.layout_watch_history.*
import java.util.*

/**
 * 观看记录 UI
 */
class WatchHistoryActivity : MvpActivity<WatchHistoryPresenter>() {

    private val watchHistoryAdapter = WatchHistoryAdapter()

    override fun getP() = WatchHistoryPresenter().apply { view = this@WatchHistoryActivity }

    override fun getLayoutId() = R.layout.layout_watch_history

    /**
     * 获取进度、错误、内容切换View事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = watchHistoryAdapter

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)

        presenter.queryWatchHistory()
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()
        //返回
        toolbar.setNavigationOnClickListener { backward() }

        //记录Item点击事件
        watchHistoryAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val itemData = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(this@WatchHistoryActivity, view, itemData)
        }
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(itemDatas: ArrayList<HomeBean.Issue.Item>) {
        if (itemDatas.isEmpty()) {
            multipleStatusView.showEmpty()
        } else {
            watchHistoryAdapter.setNewData(itemDatas)
        }
    }

}