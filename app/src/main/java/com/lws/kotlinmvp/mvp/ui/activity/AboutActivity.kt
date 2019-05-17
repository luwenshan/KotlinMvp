package com.lws.kotlinmvp.mvp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.mvp.base.MvcActivity
import com.lws.kotlinmvp.utils.AppUtils
import com.lws.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_about.*

/**
 * 关于我们
 */
class AboutActivity : MvcActivity() {

    override fun getLayoutId() = R.layout.activity_about

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {

        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

        val ver = AppUtils.getVerName(applicationContext)
        tv_version_name.text = "v$ver"
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()

        //返回
        toolbar.setNavigationOnClickListener { backward() }

        //点击访问 GitHub
        relayout_gitHub.setOnClickListener {
            val uri = Uri.parse("https://github.com/Walkud/JudyKotlinMvp")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        feedback_layout.setOnClickListener {
            showToast("意见反馈")
        }
    }
}