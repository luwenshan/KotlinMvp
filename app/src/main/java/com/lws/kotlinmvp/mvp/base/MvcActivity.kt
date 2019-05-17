package com.lws.kotlinmvp.mvp.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.common.extensions.closeKeyBoard
import com.lws.kotlinmvp.rx.transformer.EmptyTransformer
import com.lws.kotlinmvp.rx.transformer.ProgressTransformer
import com.lws.kotlinmvp.utils.CleanLeakUtil
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.zhy.m.permission.MPermissions
import io.reactivex.ObservableTransformer

abstract class MvcActivity : RxAppCompatActivity() {
    //权限设置跳转记录标记
    var permissionFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initView(savedInstanceState)
        addListener()
        processLogic(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (permissionFlag) {
            permissionFlag = false
            recheckPermissions()
        }
    }

    /**
     * 获取RootLayout布局Id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化 View 控件
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 给 View 控件添加事件监听器
     */
    open fun addListener() {}

    /**
     * 处理业务逻辑，状态恢复等操作
     */
    open fun processLogic(savedInstanceState: Bundle?) {}

    fun showToast(redId: Int) {
        showToast(getString(redId))
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 跳转
     */
    fun forward(cls: Class<*>) {
        forward(Intent(this, cls))
    }

    /**
     * 跳转
     */
    fun forward(intent: Intent) {
        closeKeyBoard()
        startActivity(intent)
    }

    /**
     * 跳转并关闭当前页面
     */
    fun forwardAndFinish(cls: Class<*>) {
        forward(cls)
        finish()
    }

    /**
     * 跳转并关闭当前页面
     */
    fun forwardAndFinish(intent: Intent) {
        forward(intent)
        finish()
    }

    /**
     * 回退
     */
    fun backward() {
        closeKeyBoard()
        finish()
    }

    /**
     * MPermissions 请求权限回调处理
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 显示权限申请说明
     * @param permission 说明文本
     */
    fun showPermissionDialog(permission: String) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(permission)
            .setTitle(getString(R.string.string_help_text, permission))
            .setPositiveButton("设置") { _, _ ->
                permissionFlag = true
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                forward(intent)
            }
            .create()
            .show()
    }

    /**
     * 重新检查权限，子类复写
     * 当跳转至设置页面后，回到当前页面时会调用此方法，子类可重新检查权限是否设置
     */
    open fun recheckPermissions() {}

    /**
     * 获取异步进度加载事务，子类复写
     * 默认返回 默认进度框事务
     */
    open fun <VT> getProgressTransformer(): ObservableTransformer<VT, VT> = ProgressTransformer(this)

    /**
     * 获取异步进度下拉或上拉加载事务，子类复写
     * 默认返回 空事务
     */
    open fun <VT> getSmartRefreshTransformer(): ObservableTransformer<VT, VT> = EmptyTransformer()

    /**
     * 获取进度、错误、内容切换View事务，子类复写
     */
    open fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> = EmptyTransformer()

    override fun onDestroy() {
        //修复华为手机内存泄漏Bug
        CleanLeakUtil.fixInputMethodManagerLeak(this)
        super.onDestroy()
    }
}