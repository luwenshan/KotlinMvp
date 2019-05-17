package com.lws.kotlinmvp.mvp.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.common.ExtraKey
import com.lws.kotlinmvp.mvp.base.MvpFragment
import com.lws.kotlinmvp.mvp.presenter.MinePresenter
import com.lws.kotlinmvp.mvp.ui.activity.AboutActivity
import com.lws.kotlinmvp.mvp.ui.activity.WatchHistoryActivity
import com.lws.kotlinmvp.mvp.ui.adapter.MineAdapter
import com.lws.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * 我的 UI
 */
class MineFragment : MvpFragment<MinePresenter>() {

    private val items = arrayListOf(
        R.string.mine_message,
        R.string.mine_attention,
        R.string.mine_cache,
        R.string.view_history,
        R.string.feedback
    )

    private var mineAdapter = MineAdapter(items)

    companion object {
        fun getInstance(title: String) = MineFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getP() = MinePresenter().apply { view = this@MineFragment }

    override fun getLayoutId() = R.layout.fragment_mine

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {

        recyclerView.adapter = mineAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }
    }

    /**
     * 添加点击事件
     */
    override fun addListener() {
        super.addListener()

        //列表点击事件
        mineAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val resId = adapter.getItem(position) as Int
            when (resId) {
                R.string.mine_message -> showToast(resId)
                R.string.mine_attention -> showToast(resId)
                R.string.mine_cache -> showToast(resId)
                R.string.view_history -> forward(WatchHistoryActivity::class.java)
                R.string.feedback -> showToast(resId)
            }
        }

        //收藏点击事件
        collection_layout.setOnClickListener {
            showToast(R.string.collection)
        }

        //评论点击事件
        comment_layout.setOnClickListener {
            showToast(R.string.comment)
        }

        iv_about.setOnClickListener {
            forward(AboutActivity::class.java)
        }

        tv_view_homepage.setOnClickListener {
            showToast("查看个人主页 >")
        }

    }
}