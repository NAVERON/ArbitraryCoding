# 打卡记录软件思路及问题


## 软件整体设计思路

软件分为三个模块：

- `start`控制程序逻辑
- `user`用户相关信息的类
- `database`存储需要提交的打卡记录的数据结构以及数据库存储

下面简要说一下部分类的作用：
`LogerCard`为启动类，程序启动入口，`Main.fxlm`是主界面，`MainController`是控制主界面的控制器，`RecoderModel`是将控制和数据存储联系起来的中间模型，相当于桥接数据和控制的类；`user`包中设计了用户和验证用户的类，需要根据具体的用户存储信息来修改；`database`包中使用`derby`内嵌Java数据库存储打卡 数据；

## 现在的业务逻辑
### 打卡信息分为3块：草稿、本地提交、已经远程提交的
其中：
- `草稿`表示本地编辑的，但是既没有本地提交又没有远程提交的
- `本地提交`表示草稿已经提交到本地存储，但是没有提交到远程的
- `已经远程提交`表示已经远程提交的打卡记录

> 用户登陆之前不能显示任何信息，登陆之后，从远程加载10条历史提交信息，同时加载本地中存在的当前用户在本地存储但未提交的历史，还有当前用户的草稿
> 点击全部提交到远程，本地提交的结果将提交到远程，但是草稿不做处理
> 本地提交和草稿在提交到远程之前都可以删除

## 使用的主要技术

- [ ] `fxml`脚本编写界面，有利有弊
- [ ] `derby`纯Java写的数据库，`dbutils`操纵数据库的一个数据库工具包，方便数据库操作
- [ ] `httpclient`apache开发的可以模拟浏览器请求的包，处理网页，可以实现模拟登陆

## 还没有完成的功能

- 网页处理相关的内容，包括远程真正的验证和登陆，返回验证的用户信息
- 数据库存储有很多的问题，还在debug中，数据存储逻辑没有理清楚
- 远程提交到网页上功能，需要网页解析，比较有挑战性

## 现在为止出现的问题

### 数据库是一个比较大的问题，调试过程中有很多bug，初始化数据库出错等
- [ ] 数据库存储有很多问题，比如说登陆之后接通数据库，登出之后是否删除数据库表？如果删除，以前的信息就删除了；如果登出后不删除用户各种信息，如果换成另一个用户登录会不会有问题，数据库里存储了许多用户以前的数据，如果没有远程提交，那么无法同步
### 解决：能否使用文件存储打卡信息，这样的话不存在数据部署的问题

### 最小化到托盘
- [ ] SystemTray is not supported,在ubuntu系统上会出现不兼容的情况，以后可以查找替代方案  










