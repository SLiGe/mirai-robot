# mirai-robot
mirai-robot机器人插件
# 如何使用
- 在application.properties修改环境参数``application.profile``为dev或其他自定义环境
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
    }
  ]
}

```

- 进入到项目目录,执行``mvn clean package``

# 联系方式
- QQ群 809647649
