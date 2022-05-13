# 前言
高仿京东商城项目具有完整的结构，代码整洁规范，结构清晰，集成React-Native热更功能，功能还在持续更新中...如果对你有帮助，给个star
1. kotlin 、 retrofit2 + okhttp3 网络请求 、多模块
2. [leakcanary 内存泄漏检测](https://github.com/square/leakcanary)
3. [基于MVI架构airbnb的Mavericks](https://airbnb.io/mavericks/#/README)
4. [本地mock](https://github.com/mirrajabi/okhttp-json-mock)
5. [alibaba ARouter](https://github.com/alibaba/ARouter/tree/master)
6. 集成RN热更功能， 用户设置为rn写的页面，[rn工程请见](https://github.com/liuaries/rn_mall.git)


# MVI架构
<img src="images/framework.png" title="" alt="image" width="800">

MVI即Model-View-Intent，它受前端框架的启发，提倡一种单向数据流的设计思想，非常适合数据驱动型的UI展示项目：

* Model: 与其他MVVM中的Model不同的是，MVI的Model主要指UI状态（State）。当前界面展示的内容无非就是UI状态的一个快照：例如数据加载过程、控件位置等都是一种UI状态
* View: 与其他MVX中的View一致，可能是一个Activity、Fragment或者任意UI承载单元。MVI中的View通过订阅Intent的变化实现界面刷新（不是Activity的Intent、后面介绍）
* Intent: 此Intent不是Activity的Intent，用户的任何操作都被包装成Intent后发送给Model进行数据请求

# 下载

##Apk下载链接： [Apk下载链接](https://www.pgyer.com/FYfa)

##Apk二维码

![](./images/download.png)

# 首页
<img src="images/home.gif" title="" alt="image" width="351">

# 分类
<img src="images/category.gif" title="" alt="image" width="351">

# 购物车
<img src="images/cart.gif" title="" alt="image" width="351">

# 我的
<img src="images/mine.gif" title="" alt="image" width="351">

# 商品详情
<img src="images/detail.gif" title="" alt="image" width="351">

# 我的-设置(rn页面-拉取远程的bundle)
<img src="images/setting.gif" title="" alt="image" width="351">

# 第三方库
| 库                       | 功能                      |
| ----------------------- | ----------------------    |
| **retrofit2**           | **网络**                   |
| **okHttp3**             | **网络**                   |
| **mavericks**           | **MVI框架**                |
| **BaseRecyclerViewAdapterHelper**  | **万能适配器**   |
| **PhotoView**           | **图片预览**                |
| **ARouter**             | **组件化路由**              |
| **coil**                | **图片加载**                |
| **XPopup**              | **弹窗组件**                |
| **banner**              | **滚动图**                  |
| **SmartRefreshLayout**  | **智能下拉刷新框架**         |
| **gson**                | **json解析**                |
| **leakcanary**          | **内存泄漏检测库**           |

# 免责声明
⚠️本APP仅限于学习交流使用，项目中使用的图片及字体等资源如有侵权请联系作者删除

⚠️本APP接口数据均为mock，请勿用于其它商业目的

⚠️如使用本项目代码造成侵权与作者无关