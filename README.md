# Mirai-Robot

    mirai-robot机器人插件,基于mirai机器人协议,依赖`mirai-core-jvm`

# 如何使用

- 安装[OpenJDK8]<https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot>,使用其他版本jdk可能会出现未知异常
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
      "handler": "cn.zjiali.robot.handler.SenMessageEventHandler", //处理器地址,请勿修改
      "command": "一言",//插件命令
      "template": "",//发送消息模板
      "templateFlag": "0",//是否使用消息模板 0否1是
      "code": "oneSen", //插件代码,请勿修改
      "ignoreKeyWords": "", //忽略触发关键字
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
      "templateFlag": "2",
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
      "templateFlag": "1",
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
      "enable": 0,
      "handler": "cn.zjiali.robot.handler.YellowCalendarMessageEventHandler",
      "command": "老黄历",
      "template": "",
      "templateFlag": "1",
      "code": "yellowCalendar",
      "ignoreKeyWords": "",
      "properties": {
        "key": "1",
        "url": "http://v.juhe.cn/laohuangli/d"
      }
    },
    {
      "name": "万年历",
      "enable": 0,
      "handler": "cn.zjiali.robot.handler.CalendarMessageEventHandler",
      "command": "万年历",
      "template": "",
      "templateFlag": "1",
      "code": "calendar",
      "ignoreKeyWords": "",
      "properties": {
        "key": "1",
        "url": "http://v.juhe.cn/calendar/day"
      }
    },
    {
      "name": "笑话",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.JokeMessageEventHandler",
      "command": "笑话",
      "template": "",
      "templateFlag": "0",
      "code": "joke",
      "ignoreKeyWords": "",
      "properties": {
        "url": "https://robot.zjiali.cn/api/queryJoke"
      }
    },
    {
      "name": "观音灵签",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.GyLqMessageEventHandler",
      "command": "观音灵签",
      "template": "签号:{number1}\n好坏:{haohua}\n签语:{qianyu}\n诗意解签:{shiyi}\n白话解签:{jieqian}",
      "templateFlag": "1",
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
      "templateFlag": "1",
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
      "templateFlag": "1",
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
      "templateFlag": "0",
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

- 进入到项目目录,执行``mvn clean package``

- 启动参数 ,在启动命令后追加即可,如: `java -jar -Drobot.protocol=0`
    - application.config.file 配置文件地址
    - robot.protocol 协议选择使用协议(0 - Android 手机, 1 - Android 平板, 2 - Android 手表)

- 执行 java -jar mirai-robot.jar

# 鸣谢

> [IntelliJ IDEA](https://zh.wikipedia.org/zh-hans/IntelliJ_IDEA) 是一个在各个方面都最大程度地提高开发人员的生产力的 IDE，适用于 JVM 平台语言。

特别感谢 [JetBrains](https://www.jetbrains.com/?from=mirai-robot)
为开源项目提供免费的 [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=mirai-robot) 等 IDE 的授权  
[<img src=".github/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=mirai-robot)