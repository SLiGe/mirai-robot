# 配置文件说明
## 1. application.profile
    应用读取某个环境配置文件,如dev,prod
## 2. application.config.file.local
    是否开启本地配置文件标志 true开始,false关闭.也就是application-{application.profile}.json ,
    配置文件放到本地,更方便修改,不用每次到jar包替换文件,当为true时,在java -jar mirai-robot.jar时,
    需要在命令后追加配置文件路径,如 :
`java -jar mirai-robot.jar C:\application-dev.json`
## 3. api.juhe.enable
    是否开启聚合平台api调用,暂时无用