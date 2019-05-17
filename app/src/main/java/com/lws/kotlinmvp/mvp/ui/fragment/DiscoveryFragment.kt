package com.lws.kotlinmvp.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.common.ExtraKey
import com.lws.kotlinmvp.mvp.base.BaseFragmentAdapter
import com.lws.kotlinmvp.mvp.base.MvcFragment
import com.lws.kotlinmvp.utils.StatusBarUtil
import com.walkud.app.mvp.ui.fragment.CategoryFragment
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * 发现(和热门首页同样的布局）
 */
class DiscoveryFragment : MvcFragment() {

    private val tabList = listOf("关注", "分类")

    private val fragments = listOf(FollowFragment.getInstance(), CategoryFragment.getInstance())

    companion object {
        fun getInstance(title: String) = DiscoveryFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_hot

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        tv_header_title.text = arguments?.getString("title")

        /**
         * getSupportFragmentManager() 替换为getChildFragmentManager()
         */
        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, fragments, tabList)
        mTabLayout.setupWithViewPager(mViewPager)
//        TabLayoutHelper.setUpIndicatorWidth(mTabLayout)
    }
}