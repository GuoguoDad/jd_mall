# 前言

android_template 是本人学习android的练手项目，基于MVI架构(airbnb的Mavericks)，项目具有完整的结构。代码整洁规范，结构清晰。

MVI即Model-View-Intent。MVI架构首前端框架的启发，提倡一种单向数据流的设计思想。非常适合数据驱动型的UI展示项目

1. Model: 与其他MVVM中的Model不同的是，MVI的Model主要指UI状态（State）。当前界面展示的内容无非就是UI状态的一个快照：例如数据加载过程、控件位置等都是一种UI状态

2. View: 与其他MVX中的View一致，可能是一个Activity、Fragment或者任意UI承载单元。MVI中的View通过订阅Intent的变化实现界面刷新

3. Intent: 此Intent不是Activity的Intent，用户的任何操作都被包装成Intent后发送给Model进行数据请求

# 项目展示

<img src="images/screen1.png" title="" alt="image" width="351">
<img src="images/screen2.png" title="" alt="image" width="353">
<img src="images/screen3.png" title="" alt="image" width="355">
<img src="images/screen4.png" title="" alt="image" width="361">
