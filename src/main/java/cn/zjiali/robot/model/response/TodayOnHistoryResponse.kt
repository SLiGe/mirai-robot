package cn.zjiali.robot.model.response

/**
 * 历史上的今天
 *
 * {
 * "reason": "success",
 * "result": [
 * {
 * "day": "1/1",
 * "date": "前45年01月01日",
 * "title": "罗马共和国开始使用儒略历",
 * "e_id": "1"
 * }
 * ],
 * "error_code": 0
 * }
 *
 * @author zJiaLi
 * @since 2021-03-21 11:28
 */
class TodayOnHistoryResponse {
    /**
     * 日期
     */
     val day: String? = null

    /**
     * 事件日期
     */
     val happenDate: String? = null

    /**
     * 事件标题
     */
    val title: String? = null

    /**
     * 事件id,即下一接口中所用的e_id
     */
     val eid: String? = null
}