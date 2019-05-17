package com.lws.kotlinmvp.mvp.ui.adapter

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lws.kotlinmvp.R
import com.lws.kotlinmvp.common.extensions.durationFormat
import com.lws.kotlinmvp.common.glide.GlideApp
import com.lws.kotlinmvp.mvp.model.bean.HomeBean
import com.lws.kotlinmvp.utils.MLog

/**
 * 关注Item 水平列表Adapter
 */
class FollowHorizontalAdapter(
    layoutResId: Int = R.layout.item_follow_horizontal,
    data: MutableList<HomeBean.Issue.Item> = ArrayList()
) :
    BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {

        val data = item.data

        // 加载封页图
        GlideApp.with(mContext)
            .load(item.data?.cover?.feed!!)
            .placeholder(R.drawable.placeholder_banner)
            .transition(DrawableTransitionOptions().crossFade())
            .into(helper.getView(R.id.iv_cover_feed))

        //横向 RecyclerView 封页图下面标题
        helper.setText(R.id.tv_title, data?.title ?: "")

        // 格式化时间
        val timeFormat = durationFormat(data?.duration)

        //标签
        MLog.d("horizontalItemData===title:${data?.title}tag:${data?.tags?.size}")
        if (data?.tags != null && data.tags.size > 0) {
            helper.setText(R.id.tv_tag, "#${data.tags[0].name} / $timeFormat")
        } else {
            helper.setText(R.id.tv_tag, "#$timeFormat")
        }
    }
}