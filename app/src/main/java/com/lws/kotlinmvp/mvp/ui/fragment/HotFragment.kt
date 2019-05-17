package com.walkud.app.mvp.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.common.ExtraKey
import com.lws.kotlinmvp.mvp.base.BaseFragmentAdapter
import com.lws.kotlinmvp.mvp.base.MvpFragment
import com.lws.kotlinmvp.mvp.model.bean.TabInfoBean
import com.lws.kotlinmvp.mvp.presenter.HotPresenter
import com.lws.kotlinmvp.mvp.ui.fragment.RankFragment
import com.lws.kotlinmvp.rx.transformer.MultipleStatusViewTransformer
import com.lws.kotlinmvp.utils.StatusBarUtil
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * 热门 UI
 * Created by Zhuliya on 2018/12/4
 */
class HotFragment : MvpFragment<HotPresenter>() {

    private val mTabTitleList = ArrayList<String>()//存放 tab 标题
    private val mFragmentList = ArrayList<Fragment>()

    companion object {
        fun getInstance(title: String) = HotFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getP() = HotPresenter().apply { view = this@HotFragment }

    override fun getLayoutId() = R.layout.fragment_hot

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

        tv_header_title.text = arguments?.getString("title")

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        presenter.queryRankTabData()
    }

    /**
     * 添加点击事件
     */
    override fun addListener() {
        super.addListener()
        //异常布局，点击重新加载
        multipleStatusView.setOnClickListener {
            presenter.queryRankTabData()
        }
    }

    /**
     * 更新 Tab UI
     */
    fun updateTabUi(tabInfoBean: TabInfoBean) {

        tabInfoBean.tabInfo.tabList.mapTo(mTabTitleList) { it.name }
        tabInfoBean.tabInfo.tabList.mapTo(mFragmentList) { RankFragment.getInstance(it.apiUrl) }

        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, mFragmentList, mTabTitleList)
        mTabLayout.setupWithViewPager(mViewPager)
    }

}