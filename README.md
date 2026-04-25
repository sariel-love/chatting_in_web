<h1>请注意本项目目前还处在被本笨鸟练手阶段，暂时不具备任何参考性</h1>

<h1>本项目为多人在线网络聊天web项目</h1>

<h2>目前使用到的技术栈：</h2>

<h3>
    &emsp;  Spring MVC  <br><br>
    &emsp;  Spring Framework  <br><br>
    &emsp;  MyBatis  <br><br>
    &emsp;  MySQL  <br><br>
    &emsp;  Maven  <br><br>
    &emsp;  WebSocket  <br><br>
    &emsp;  Thymeleaf  <br><br>
</h3>
<h3>功能：</h3>

<h4>&emsp;  私聊<br>

&emsp;  与ai聊天<br>

&emsp;  群聊:<br>

&emsp;  &emsp;  【目前没测试人数限制】<br>
&emsp;  &emsp;  【消息峰值可能导致系统崩溃】<br>
&emsp;  &emsp;  【聊天记录无法保存】<br>




&emsp;  基础登录：<br>

&emsp;  &emsp;  使用手机号登录（唯一）<br>
&emsp;  &emsp;  注册功能：<br>
&emsp;  &emsp;  规避同一手机号多次注册<br>
&emsp;  &emsp;  密码非空<br>
&emsp;  &emsp;  邮箱矫正<br>
&emsp;  &emsp;  手机号位数矫正<br>
</h4>

<h3>现阶段工作；</h4>
    1.聊天记录从简单的浏览器转向数据库√<br>
    插:登录后，第一次聊天记录加载，从数据库中获得，之后将新的消息逐条加进数据库，前端从浏览器缓存中获得？<br>
        用户A发送消息<br>
                ↓<br>
        后端存入数据库<br>
            ↓<br>
        后端通过 WebSocket 推送给 你 和 用户A<br>
                ↓<br>
        前端直接显示消息 + 更新本地缓存<br>
        登录成功<br>
        后端返回 token → 存 localStorage<br>
        聊天页面<br>
        判断有 token → 允许进入 → 加载缓存<br>
        退出登录<br>
        删除 token + 清除聊天缓存 → 跳登录页<br>
        下次打开<br>
        无 token → 判断未登录 → 必须重新登录<br>
    2.集成redis到springboot<br>
    3.改变在线用户存储，从本地map到redis hash<br>
    4.配置线程池，异步处理websocket消息<br>
    5.实现历史消息缓存，先redis再mysql<br>
    6.实现用户信息缓存和群聊信息缓存<br>
    7.测试缓存命中率，mysql压力下降情况<br>
    8.实现分布式锁，防止重复登录<br>
    9.测试模拟1000用户并发，验证稳定性<br>
    10.完善日志
        

