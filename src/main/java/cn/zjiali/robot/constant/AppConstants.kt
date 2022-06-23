package cn.zjiali.robot.constant

/**
 * @author zJiaLi
 * @since 2021-09-07 10:20
 */
object AppConstants {

    /**
     * 使用map填充消息内容标志
     */
    const val FILL_OUT_MESSAGE_MAP_FLAG = 1

    /**
     * 使用对象实例填充消息内容标志
     */
    const val FILL_OUT_MESSAGE_OBJECT_FLAG = 2

    /**
     * 消息由总处理器生产
     */
    const val MESSAGE_TYPE_HANDLER = 1

    /**
     * 消息由插件生产
     */
    const val MESSAGE_TYPE_PLUGIN = 2

}