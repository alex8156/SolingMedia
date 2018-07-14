#知豆D2S多媒体交接文档
>知豆D2S多媒体模块使用Kotlin编写，框架基于谷歌官方推荐框架Android Architecture Components，以及databinding库进行数据和视图绑定。功能目前还在完善中，bug也不少，代码也有很多不规范的地方，望大神提出建议改进问题。

## 环境搭建
###本地安装Android Studio
安装Android Studio 3.1.2版本以及Shadowsocks，安装文件位于公司虚拟桌面共享目录`软件一科/曾翼/D2S`，拷贝目录文件到本地windows系统，安装`android-studio-ide-173.4720617-windows.exe`。
###Gradle环境变量配置
解压`gradle-4.7-all.zip`，将其bin路径加入Path环境变量中，用于打包apk，如`D:\gradle-4.7\bin`

###设置gradle全局代理
 因为项目中依赖了google仓库，因此需设置gradle代理，解决依赖问题。拷贝`gradle.properties`至C盘用户目录`.gradle`下（如`C:\Users\yizeng\.gradle`）
###设置Chrome代理
由于网络限制，简书和安卓官网等网站上的参考资料无法浏览，需设置Chrome浏览器代理。安装`ChromeSetup.exe`谷歌浏览器后，点击`更多工具-扩展程序`，将`SwitchyOmega_Chromium.crx`拖入其中。安装`Shadowsocks.exe`，设置代理。

`代理配置私聊我，因为是个人付费账号`
###编译apk
因为是本地开发，远程依赖太多，并且有些代码是在编译时期生成的，需要用gradle打包生成apk后，以预编译应用程序的形式进行Android make编译。具体操作：
Android Studio Terminal终端输入`cd APP_Music`，然后输入`gradle build`，打包成功后会在相应工程目录下生成apk ，将apk传输入虚拟桌面中；Gitlab项目分支上创建`Android.mk`([写法参考](https://github.com/alex8156/SolingMedia/blob/master/APP_Music/Android.mk))，拷贝apk至同级目录下,`mm -B`编译

###发布开源库到JCenter

[media-library](https://bintray.com/alex8156/SolingMedia/media-library)已上传至JCenter，发布爬坑参照这篇[文章](https://android.jlelse.eu/publishing-your-android-kotlin-or-java-library-to-jcenter-from-android-studio-1b24977fe450)


## 知识点
###Kotlin语法
>Kotlin是一种在Java虚拟机上运行的静态类型编程语言，它也可以被编译成为JavaScript源代码。虽然与Java语法并不兼容，但Kotlin被设计成可以和Java代码相互运作，并可以重复使用如Java集合框架等的现有Java类库。—— [维基百科](https://zh.wikipedia.org/wiki/Kotlin)

 [官方文档](http://kotlinlang.org/docs/reference/)
 [中文pdf文档](https://legacy.gitbook.com/book/hltj/kotlin-reference-chinese/details)
 [什么是函数式编程思维](https://www.zhihu.com/question/28292740)
 [Lambda 表达式有何用处？如何使用？](https://www.zhihu.com/question/20125256)
[Kotlin 协程异步操作库](https://www.jianshu.com/p/d4a8358e843e)
[『译』Coroutines 与 RxJava 异部机制对比之异步编程](https://www.jianshu.com/p/c9a3c32943fc)
[Kotlin扩展](https://blog.csdn.net/qq_26122557/article/details/79385640)
[官方新出的 Kotlin 扩展库 KTX，到底帮你干了什么？](https://www.jianshu.com/p/d2b3fdef90d8)
[玩转 Kotlin 委托属性](https://www.jianshu.com/p/306bdc2bac3f)



###Android官方组件框架[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
	ViewModel:用于为UI组件提供数据，并且能够在旋转屏幕等Configuration Change发生时，仍能保持里面的数据。当UI组件恢复时，可以立刻向UI提供数据.
	Lifecycle:能够根据Activity或者Fragment的生命周期自行调整类的行为.
	LiveData:LiveData是有生命周期感知能力的，这意味着它可以在activities, fragments, 或者 services生命周期是活跃状态时更新这些组件.可以在生命周期结束的时候立刻解除对数据的订阅，从而避免内存泄漏等问题.
	Room:数据库解决方案,Room在SQLite上提供了一个方便访问的抽象层

[Architecture Component的官方例子](https://github.com/googlesamples/android-architecture-components)
[理解Android Architecture Components系列](https://www.jianshu.com/p/42eb71ec4a19)
[使用Room的7个专业小建议](https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1)

###数据绑定库[DataBinding](https://developer.android.com/topic/libraries/data-binding/)
[Android DataBinding 从入门到进阶](https://blog.csdn.net/c10wtiybq1ye3/article/details/80491063)
[当RecycleView遇上DataBinding](https://www.jianshu.com/p/fd57c53df244)
[Android Data Binding: RecyclerView](https://medium.com/google-developers/android-data-binding-recyclerview-db7c40d9f0e4)
[Databinding与LiveData的合作](https://juejin.im/post/5a4b89e2f265da430e4f896f)
###约束布局[ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout)
[ConstraintLayout在项目中实践与总结](https://www.jianshu.com/p/f110b4fcfe93)
[（译）理解ConstraintLayout性能上的好处](https://www.jianshu.com/p/fae1d533597b)
