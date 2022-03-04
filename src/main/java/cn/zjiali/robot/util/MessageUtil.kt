package cn.zjiali.robot.util

import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import com.google.common.collect.Lists
import java.lang.StringBuilder
import java.lang.reflect.Field
import java.util.stream.Collectors

/**
 * 消息工具类
 *
 * @author zJiaLi
 * @since 2021-04-04 20:24
 */
object MessageUtil {
    @JvmStatic
    fun replaceMessage(message: String, o: Any): String? {
        if (ObjectUtil.isNullOrEmpty(message) || ObjectUtil.isNullOrEmpty(o)) {
            return null
        }
        val fields = Lists.newArrayList(*ReflectUtil.getFields(o.javaClass)).stream().map { obj: Field -> obj.name }
            .collect(Collectors.toList())
        val messageBuilder = StringBuilder()
        val messageArray = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((i, childMessage) in messageArray.withIndex()) {
            val left = childMessage.indexOf("{")
            val right = childMessage.indexOf("}")
            if (left > -1 && right > -1) {
                var fieldName = childMessage.substring(left + 1, right)
                if (fields.contains(fieldName)) {
                    val invokeVal = ReflectUtil.getFieldValue(o, fieldName)
                    if (invokeVal != null && "null" != invokeVal.toString()) {
                        fieldName = "{$fieldName}"
                        messageBuilder.append(childMessage.replace(fieldName, invokeVal.toString()))
                    }
                }
            } else {
                messageBuilder.append(childMessage)
            }
            if (i != messageArray.size - 1) messageBuilder.append("\n")
        }
        return messageBuilder.toString()
    }

    @JvmStatic
    fun replaceMessageByMap(message: String, fillMap: Map<String, String>): String? {
        if (ObjectUtil.isNullOrEmpty(message) || ObjectUtil.isNullOrEmpty(fillMap)) {
            return null
        }
        val messageBuilder = StringBuilder()
        val messageArray = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((i, childMessage) in messageArray.withIndex()) {
            val left = childMessage.indexOf("{")
            val right = childMessage.indexOf("}")
            if (left > -1 && right > -1) {
                val fieldName = childMessage.substring(left + 1, right)
                if (fillMap.containsKey(fieldName)) {
                    val fieldVal = fillMap[fieldName]
                    if (StrUtil.isNotBlank(fieldVal) && "null" != fieldVal)
                        messageBuilder.append(
                            childMessage.replace("{$fieldName}", fieldVal!!)
                        )
                }
            } else {
                messageBuilder.append(childMessage)
            }
            if (i != messageArray.size - 1) messageBuilder.append("\n")
        }
        return messageBuilder.toString()
    }
}