package com.lws.kotlinmvp.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lws.kotlinmvp.R

/**
 * 我的Item Adapter
 */
class MineAdapter(data: ArrayList<Int>, layoutResId: Int = R.layout.item_mine) :
    BaseQuickAdapter<Int, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.setText(R.id.item_name, item)
    }
}