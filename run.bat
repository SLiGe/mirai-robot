java -jar -Xdebug -agentlib:jdwp=transport=dt_socket,address=*:6666,server=y,suspend=n -Dmirai.slider.captcha.supported
 -Drobot.protocol=3 -Drobot.qq=2364051402
 -Drobot.workdir=C:\robot\2364051402
 -Dlog4j.configurationFile=classpath:/log4j2.xml
 -Dapplication.config.file=C:\robot\2364051402\application-dev-2364051402.json  C:\robot\2364051402\mirai-robot.jar