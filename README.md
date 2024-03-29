# Mirai-Robot

    mirai-robot机器人,基于mirai机器人协议,依赖`mirai-core-jvm`
# 服务接口
接口文档地址: [ApiDoc]<https://robot.zjiali.cn/> , 可自行开发对应插件

# 如何使用

- 安装[OpenJDK17]<https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot>,使用其他版本jdk可能会出现未知异常
- 在application.properties修改环境参数``application.profile``为dev或其他自定义环境
- 参照 **Config.md** 配置application.properties
- 修改application-{dev}.json

```json
{
  "appEnable": 1,//是否加载插件 0否 1是
  "qq": "xxx",//QQ账号
  "password": "xxx",//QQ密码
  "plugins": [
    {
      "name": "一言",//插件名称
      "enable": 1,//是否加载 0否 1是
      "handler": "cn.zjiali.robot.handler.SenMessageEventHandler",//处理器地址,请勿修改
      "command": "一言",//插件命令
      "template": "",//发送消息模板
      "templateFlag":"0",//是否使用消息模板  0=无模板 1=单一模板 2=多个模板(多个模板写到properties属性内)
      "code": "oneSen",//插件代码,请勿修改
      "ignoreKeyWords": "",//忽略触发关键字
      "properties": { //插件配置
        "url": "https://robot.zjiali.cn/api/getSen"
      }
    },
    {
      "name": "签到",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.SignInMessageEventHandler",
      "command": "签到,积分查询",
      "template": "",
      "templateFlag":"2",
      "code": "sign",
      "ignoreKeyWords": "",
      "properties": {
        "signTemplate": "签到成功!\n\uD83D\uDCB8获得积分: {getPoints}点\n⭐本月积累签到: {monthDay}天\n\uD83D\uDCB3当前积分: {points}点\n⭐当前等级: {currentLevel}\n⭐每日一句: {todayMsg}",
        "signUrl": "https://robot.zjiali.cn/api/signIn",
        "querySignTemplate": "\uD83D\uDCB8总积分: {points}点\n⭐连续签到: {monthDay}天\n\uD83D\uDCB3总签到天数: {totalDay}天\n⭐当前等级: {currentLevel}\n⭐每日一句: {todayMsg}",
        "querySignUrl": "https://robot.zjiali.cn/api/querySignInData"
      }
    },
    {
      "name": "今日运势",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.FortuneMessageEventHandler",
      "command": "运势",
      "template": "\uD83C\uDF13您的今日运势为: {fortuneSummary}\n\uD83C\uDF1F星指数: {luckyStar}\n\uD83D\uDCD7签文: {signText}\n\uD83D\uDCDD解签: {unSignText}",
      "templateFlag":"1",
      "code": "fortune",
      "ignoreKeyWords": "",
      "properties": {
        "day_one": "0",
        "point": "0",
        "url": "https://robot.zjiali.cn/api/getFortuneOfToday"
      }
    },
    {
      "name": "老黄历",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.YellowCalendarMessageEventHandler",
      "command": "老黄历",
      "template": "今日老黄历:\n阳历:{yangli}\n阴历:{yinli}\n五行:{wuxing}\n冲煞:{chongsha}\n彭祖百忌:{baiji}\n吉神宜趋:{jishen}\n宜:{yi}\n凶神宜忌:{xiongshen}\n忌:{ji}",
      "templateFlag":"1",
      "code": "yellowCalendar",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/yellowCalendar"
      }
    },
    {
      "name": "万年历",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.CalendarMessageEventHandler",
      "command": "万年历",
      "template": "今日万年历:\n假日:{holiday}\n忌:{avoid}\n属相:{animalsYear}\n假日描述:{desc}\n周几:{weekday}\n宜:{suit}\n纪年:{lunarYear}\n农历:{lunar}\n具体日期:{date}",
      "templateFlag":"1",
      "code": "calendar",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/perpetualCalendar"
      }
    },
    {
      "name": "历史上的今天",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.TodayOfHistoryMessageEventHandler",
      "command": "历史上的今天",
      "template": "",
      "templateFlag":"0",
      "code": "todayHistory",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/perpetualCalendar"
      }
    },
    {
      "name": "笑话",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.JokeMessageEventHandler",
      "command": "笑话",
      "template": "",
      "templateFlag":"0",
      "code": "joke",
      "ignoreKeyWords": "",
      "properties": {
        "url": "http://127.0.0.1:8999/api/queryJoke"
      }
    },
    {
      "name": "灵签",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.SpiritSignMessageEventHandler",
      "command": "观音灵签,月老灵签,财神灵签",
      "template": "",
      "templateFlag":"2",
      "code": "lq",
      "ignoreKeyWords": "",
      "properties": {
        "url": "http://127.0.0.1:8999/lq/oneSignPerDay",
        "gyTemplate": "{title}\n诗曰:{shi_yue}\n诗意:{shi_yi}\n解曰:{jie_yue}\n本签精髓:{bqjs}\n详情请点击:{viewUrl}",
        "ylTemplate": "{title}\n签诗:{qian_shi}\n解签:{jie_qian}\n详情请点击:{viewUrl}",
        "csTemplate": "{title}\n诗曰:{shi_yue}\n吉凶:{ji_xiong}\n详情请点击:{viewUrl}"
      }
    },
    {
      "name": "观音灵签",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.GyLqMessageEventHandler",
      "command": "观音灵签",
      "template": "签号:{number1}\n好坏:{haohua}\n签语:{qianyu}\n诗意解签:{shiyi}\n白话解签:{jieqian}",
      "templateFlag":"1",
      "code": "gylq",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/queryLq"
      }
    },
    {
      "name": "月老灵签",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.YlLqMessageEventHandler",
      "command": "月老灵签",
      "template": "签号:{number1}\n好坏:{haohua}\n诗意解签:{shiyi}\n解签:{jieqian}\n注释:{zhushi}\n白话浅释:{baihua}",
      "templateFlag":"1",
      "code": "yllq",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/queryLq"
      }
    },
    {
      "name": "财神爷灵签",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.CsyLqMessageEventHandler",
      "command": "财神爷灵签",
      "template": "签号:{number1}\n签语:{qianyu}\n注释:{zhushi}\n解签:{jieqian}\n解说:{jieshuo}\n结果:{jieguo}\n婚姻:{hunyin}\n交易:{jiaoyi}\n事业:{shiye}",
      "templateFlag":"1",
      "code": "csylq",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/queryLq"
      }
    },
    {
      "name": "茉莉聊天",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.MoLiMessageEventHandler",
      "templateFlag":"0",
      "code": "MOLI",
      "ignoreKeyWords": "天气,ip,@qq,@lol,@sfz,@sjh,@cy,笑话,观音灵签,月老灵签,财神爷灵签",
      "properties": {
        "chatGroupAt": "1",
        "limit": "5",
        "api_key": "",
        "api_secret": "",
        "type": "",
        "url": "http://i.itpk.cn/api.php",
        "zUrlChat": "https://robot.zjiali.cn/api/getChatReply",
        "zUrlLq": "https://robot.zjiali.cn/api/queryLq",
        "isMoLiServer": "0"
      }
    }
  ]
}
```

- 消息模板中{}里的字段可在`src\main\java\cn\zjiali\robot\entity\response` 中查看各插件对应的实体
- 开发新插件需要实现`AbstractMessageEventHandler`,可参照现有插件
- 进入到项目目录,执行``mvn clean kotlin:compile package -f pom.xml``

- 启动参数:
```shell
-Dmirai.slider.captcha.supported #支持验证码
-Drobot.protocol=1  #协议选择(0 - Android 手机, 1 - Android 平板, 2 - Android 手表, 3 - IPAD, 4 - MACOS)
-Drobot.qq=2364051402  #机器人QQ
-Dapplication.config.file=/home/2364051402/application-dev.json #本地配置文件地址
-Drobot.workdir=/home/2364051402 #工作文件夹
```

- 执行 `java -jar -Drobot.protocol=0 -Dapplication.config.file=G:\application-dev-3333.json mirai-robot.jar`


# 鸣谢

> [IntelliJ IDEA](https://zh.wikipedia.org/zh-hans/IntelliJ_IDEA) 是一个在各个方面都最大程度地提高开发人员的生产力的 IDE，适用于 JVM 平台语言。

特别感谢 [JetBrains](https://www.jetbrains.com/?from=mirai-robot)
为开源项目提供免费的 [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=mirai-robot) 等 IDE 的授权  
[<img src=".github/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=mirai-robot)