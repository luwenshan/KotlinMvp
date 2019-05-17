
### 抽象

项目中 Mvp 的 P 和 V 之间相互都依赖的具体实现，省去了抽象这一步，把 Mvc 中 Activity 中承担的业务逻辑搬到了 P 中,而 Activity 只承担了处理界面交互的能力，并且再也不用去写那些无用的接口。

项目中创建 M 实例是用的Java反射，省去在各业务的 P 中单独创建。

### 异步导致的内存泄漏
本项目中使用了 RxJava (异步利器)来处理异步问题，配合开源库 [RxLifecycle](https://github.com/trello/RxLifecycle) 解决异步导致的内存泄漏问题。

### 进度UI和页面加载状态的控制

进度UI和页面加载状态的控制使用了 RxJava 的自定义 ObservableTransformer 控制。

[参考 ProgressTransformer](./app/src/main/java/com/walkud/app/rx/transformer/ProgressTransformer.kt)

[参考 MultipleStatusViewTransformer](./app/src/main/java/com/walkud/app/rx/transformer/MultipleStatusViewTransformer.kt)

[参考 SmartRefreshTransformer](./app/src/main/java/com/walkud/app/rx/transformer/SmartRefreshTransformer.kt)

### 多状态View

[MultipleStatusView](./multiple-status-view/src/main/java/com/lws/multiplestatusview/MultipleStatusView.java) 封装了页面加载的多种状态，方便切换。

## 接入的第三方开源库

 - [RxJava](https://github.com/ReactiveX/RxJava)
 - [RxAndroid](https://github.com/ReactiveX/RxAndroid)
 - [RxLifecycle](https://github.com/trello/RxLifecycle)
 - [Retrofit](https://github.com/square/retrofit)
 - [Glide](https://github.com/bumptech/glide)
 - [Logger](https://github.com/orhanobut/logger)
 - [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
 - [Flexbox-layout](https://github.com/google/flexbox-layout)
 - [RealtimeBlurView](https://github.com/mmin18/RealtimeBlurView)
 - [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
 - [BGABanner-Android](https://github.com/bingoogolapple/BGABanner-Android)
 - [GSYVideoPlayer](https://github.com/CarGuo/GSYVideoPlayer)
 - [MPermissions](https://github.com/hongyangAndroid/MPermissions)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

**项目中的 API 均来自开眼视频，纯属学习交流使用！**