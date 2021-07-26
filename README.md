# Mirai-Robot
    mirai-robot机器人插件,基于mirai机器人协议,依赖`mirai-core-jvm`
# 如何使用
- 安装[OpenJDK8]<https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot>,使用其他版本jdk可能会出现未知异常
- 在application.properties修改环境参数``application.profile``为dev或其他自定义环境
- 参照 **Config.md** 配置application.properties
- 修改application-{dev}.json
```json
{
  "appEnable": 1, //是否加载插件 0否 1是
  "qq": "2696864316", //QQ账号
  "password": "..", //QQ密码
  "plugins": [
    {
      "name": "一言",  //插件名称
      "enable": 1, //是否加载 0否 1是
      "handler": "cn.zjiali.robot.handler.SenHandler", //处理器地址,请勿修改
      "configClass": "cn.zjiali.robot.config.plugin.SignInConfig",//插件配置项地址,请勿修改
      "properties": {
        "command": "一言" //插件命令
      }
    },
    {
      "name": "签到",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.SignInHandler",
      "configClass": "cn.zjiali.robot.config.plugin.SignInConfig",
      "properties": {
        "command": "签到,积分查询",
        "cur_level": "1", //是否显示当前等级
        "day_sen": "1" //是否显示每日一言
      }
    },
    {
      "name": "今日运势",
      "enable": 1,
      "handler": "cn.zjiali.robot.handler.FortuneHandler",
      "configClass": "cn.zjiali.robot.config.plugin.SignInConfig",
      "properties": {
        "command": "运势",
        "sign_text": "1", //是否显示签文
        "star_num": "1", //是否显示星指数
        "un_sign": "1", //是否显示解签
        "day_one": "0", //是否每日一次(群内生效)
        "point": "0" //是否积分制
      }
    },
     {
       "name": "老黄历", //待完成
       "enable": 0,
       "handler": "cn.zjiali.robot.handler.YellowCalendarHandler",
       "configClass": "cn.zjiali.robot.config.plugin.YellowCalendarConfig",
       "properties": {
         "command": "老黄历",
         "key": "1", //聚合平台密钥
         "url": "1",  //调用URL
         "template": "1" //发送消息模板
       }
     },
     {
       "name": "万年历", //待完成
       "enable": 0,
       "handler": "cn.zjiali.robot.handler.CalendarHandler",
       "configClass": "cn.zjiali.robot.config.plugin.CalendarConfig",
       "properties": {
         "command": "万年历",
         "key": "1",
         "url": "1",
         "template": "1"
       }
     },
     {
       "name": "茉莉聊天",
       "enable": 1,
       "handler": "cn.zjiali.robot.handler.MoLiHandler",
       "configClass": "cn.zjiali.robot.config.plugin.MoLiConfig",
       "properties": {
         "chatGroupAt": "1", //群里是否@机器人才发言
         "jokeCommand": "笑话", //笑话命令
         "jokeTemplate": "", //笑话模板,默认为空,直接发送一段话
         "jokeEnable": "1", //是否启用笑话功能
         "gylqCommand": "观音灵签", //观音灵签命令
         "gylqTemplate": "签号:{number1}\n好坏:{haohua}\n签语:{qianyu}\n诗意解签:{shiyi}\n白话解签:{jieqian}",
         "gylqEnable": "1",
         "yllqCommand": "月老灵签",
         "yllqTemplate": "签号:{number1}\n好坏:{haohua}\n诗意解签:{shiyi}\n解签:{jieqian}\n注释:{zhushi}\n白话浅释:{baihua}",
         "yllqEnable": "1",
         "csylqCommand": "财神爷灵签",
         "csylqTemplate": "签号:{number1}\n签语:{qianyu}\n注释:{zhushi}\n解签:{jieqian}\n解说:{jieshuo}\n结果:{jieguo}\n婚姻:{hunyin}\n交易:{jiaoyi}\n白话浅释:{baihua}",
         "csylqEnable": "1",
         "limit": "5", //茉莉平台需要的参数 2-8,数值越大回复越准确,但是速度会下降
         "api_key": "", //茉莉平台key,可空
         "api_secret": "", //茉莉平台密钥,可空
         "type": "", //茉莉平台返回消息类型,可空
         "url": "http://i.itpk.cn/api.php" //茉莉平台URL
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

特别感谢 [JetBrains](https://www.jetbrains.com/?from=mirai-robot) 为开源项目提供免费的 [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=mirai-robot) 等 IDE 的授权  
[<img src=".github/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=mirai-robot)