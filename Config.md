# 配置文件说明

## 1. application.profile

    应用读取某个环境配置文件,如dev,prod

## 2. application.config.file.local

    是否开启本地配置文件标志 true开始,false关闭.也就是application-{application.profile}.json ,
    配置文件放到本地,更方便修改,不用每次到jar包替换文件,当为true时,在java -jar mirai-robot.jar时,
    需要在命令后追加配置文件路径,如 :

`java -jar mirai-robot.jar C:\application-dev.json`

## 3. api.juhe.enable(暂时无用)

    是否开启聚合平台api调用,暂时无用

## 4. application.receive.data.send

    是否将回复内容(观音灵签,财神爷灵签这些,日常回复不会)发送到作者的服务端,方便收集数据,日后切换api使用

## 5. application.working.path(暂时无用)

    应用工作目录,默认为空,则运行目录为当前所在目录