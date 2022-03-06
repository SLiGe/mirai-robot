# 配置文件说明
## 1. application.profile
    应用读取某个环境配置文件,如dev,prod
## 2. application.config.file.local
    是否开启本地配置文件标志 true开始,false关闭.也就是application-{application.profile}.json ,
    配置文件放到本地,更方便修改,不用每次到jar包替换文件,当为true时,在java -jar mirai-robot.jar时,
    需要在命令后追加配置文件路径,如 :
`java -jar -Dapplication.config.file=C:\application-dev.json  mirai-robot.jar`
## 3. api.juhe.enable(暂时无用)
    是否开启聚合平台api调用,暂时无用
## 4. application.receive.data.send
    是否将回复内容(观音灵签,财神爷灵签这些,日常回复不会)发送到作者的服务端,方便收集数据,日后切换api使用
## 5. application.working.path(暂时无用)
    应用工作目录,默认为空,则运行目录为当前所在目录
## 6. robot.protocol
    机器人协议选择,0-手机(默认),1-平板,2-watch手表
`java -jar  -Drobot.protocol=0 mirai-robot.jar`
## 7. robot.reply.blacklist
    消息回复黑名单,里面包含的QQ则不回复,默认带的是QQ官方机器人的QQ号
## 8. robot.reply.blacklist.url (无需修改)
    消息回复服务端黑名单,该黑名单不定期更新,不会涉及真实用户
## 9. robot.dict.query.url(无需修改)
    服务器字典查询服务URL
## 10.robot.websocket.flag
    websocket开启标识,0关闭 1开启, 开启后则接收服务端推送消息
## 11.robot.websocket.url(无需修改)
    websocket服务端地址