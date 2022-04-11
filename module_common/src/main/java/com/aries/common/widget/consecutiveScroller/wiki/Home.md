## 引入依赖

在Project的build.gradle在添加以下代码

```groovy
allprojects {
      repositories {
         ...
         maven { url 'https://jitpack.io' }
      }
   }
```
在Module的build.gradle在添加以下代码
```groovy

implementation 'com.github.donkingliang:ConsecutiveScroller:4.6.1'

```

## 文档

**注意：** 如果你准备使用这个库，请务必认真阅读下面的文档。它能让你了解ConsecutiveScrollerLayout可以实现的功能，以及避免不必要的错误。

### [基本功能](https://github.com/donkingliang/ConsecutiveScroller/wiki/1.%E5%9F%BA%E6%9C%AC%E5%8A%9F%E8%83%BD)

>[基本使用](https://github.com/donkingliang/ConsecutiveScroller/wiki/1.%E5%9F%BA%E6%9C%AC%E5%8A%9F%E8%83%BD#%E5%9F%BA%E6%9C%AC%E4%BD%BF%E7%94%A8)

>[关于margin](https://github.com/donkingliang/ConsecutiveScroller/wiki/1.%E5%9F%BA%E6%9C%AC%E5%8A%9F%E8%83%BD#%E5%85%B3%E4%BA%8Emargin)

>[布局对齐方式](https://github.com/donkingliang/ConsecutiveScroller/wiki/1.%E5%9F%BA%E6%9C%AC%E5%8A%9F%E8%83%BD#%E5%B8%83%E5%B1%80%E5%AF%B9%E9%BD%90%E6%96%B9%E5%BC%8F)

>[嵌套Fragment](https://github.com/donkingliang/ConsecutiveScroller/wiki/1.%E5%9F%BA%E6%9C%AC%E5%8A%9F%E8%83%BD#%E5%B5%8C%E5%A5%97fragment)

### [布局吸顶](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6)

>[普通吸顶](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#%E6%99%AE%E9%80%9A%E5%90%B8%E9%A1%B6)

>[常驻吸顶](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#%E5%B8%B8%E9%A9%BB%E5%90%B8%E9%A1%B6)

>[吸顶下沉模式](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#%E5%90%B8%E9%A1%B6%E4%B8%8B%E6%B2%89%E6%A8%A1%E5%BC%8F)

>[stickyOffset属性](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#stickyoffset%E5%B1%9E%E6%80%A7)

>[autoAdjustHeightAtBottomView属性和adjustHeightOffset属性](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#autoadjustheightatbottomview%E5%B1%9E%E6%80%A7%E5%92%8Cadjustheightoffset%E5%B1%9E%E6%80%A7)

>[关于吸顶功能的其他方法](https://github.com/donkingliang/ConsecutiveScroller/wiki/2.%E5%B8%83%E5%B1%80%E5%90%B8%E9%A1%B6#%E5%85%B3%E4%BA%8E%E5%90%B8%E9%A1%B6%E5%8A%9F%E8%83%BD%E7%9A%84%E5%85%B6%E4%BB%96%E6%96%B9%E6%B3%95)

### [局部滑动](https://github.com/donkingliang/ConsecutiveScroller/wiki/3.%E5%B1%80%E9%83%A8%E6%BB%91%E5%8A%A8)

### [滑动子view的下级view](https://github.com/donkingliang/ConsecutiveScroller/wiki/4.%E6%BB%91%E5%8A%A8%E5%AD%90view%E7%9A%84%E4%B8%8B%E7%BA%A7view)

>[layout_scrollChild属性](https://github.com/donkingliang/ConsecutiveScroller/wiki/4.%E6%BB%91%E5%8A%A8%E5%AD%90view%E7%9A%84%E4%B8%8B%E7%BA%A7view#layout_scrollchild%E5%B1%9E%E6%80%A7)

>[IConsecutiveScroller接口](https://github.com/donkingliang/ConsecutiveScroller/wiki/4.%E6%BB%91%E5%8A%A8%E5%AD%90view%E7%9A%84%E4%B8%8B%E7%BA%A7view#iconsecutivescroller%E6%8E%A5%E5%8F%A3)

>[对ViewPager的支持](https://github.com/donkingliang/ConsecutiveScroller/wiki/4.%E6%BB%91%E5%8A%A8%E5%AD%90view%E7%9A%84%E4%B8%8B%E7%BA%A7view#%E5%AF%B9viewpager%E7%9A%84%E6%94%AF%E6%8C%81)

>[对ViewPager2的支持](https://github.com/donkingliang/ConsecutiveScroller/wiki/4.%E6%BB%91%E5%8A%A8%E5%AD%90view%E7%9A%84%E4%B8%8B%E7%BA%A7view#%E5%AF%B9viewpager2%E7%9A%84%E6%94%AF%E6%8C%81)

### [其他](https://github.com/donkingliang/ConsecutiveScroller/wiki/5.%E5%85%B6%E4%BB%96)

>[使用腾讯x5的WebView](https://github.com/donkingliang/ConsecutiveScroller/wiki/5.%E5%85%B6%E4%BB%96#%E4%BD%BF%E7%94%A8%E8%85%BE%E8%AE%AFx5%E7%9A%84webview)

>[使用SmartRefreshLayout](https://github.com/donkingliang/ConsecutiveScroller/wiki/5.%E5%85%B6%E4%BB%96#%E4%BD%BF%E7%94%A8smartrefreshlayout)

>[其他常用方法](https://github.com/donkingliang/ConsecutiveScroller/wiki/5.%E5%85%B6%E4%BB%96#%E5%85%B6%E4%BB%96%E5%B8%B8%E7%94%A8%E6%96%B9%E6%B3%95)

>[其他注意事项](https://github.com/donkingliang/ConsecutiveScroller/wiki/5.%E5%85%B6%E4%BB%96#%E5%85%B6%E4%BB%96%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)


**建议：** 在开始使用ConsecutiveScroller前，我建议你先拉取github上的代码，运行体验一下demo的效果。demo的实现虽然简单，但是覆盖了大部分的功能效果，你在阅读文档或者使用的过程中，遇到不理解的地方，也可以通过修改和体验demo来帮助理解。
