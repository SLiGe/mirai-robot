package cn.zjiali.robot.model.response;

/**
 * 历史上的今天
 *
 *  {
 *     "reason": "success",
 *     "result": [
 *         {
 *             "day": "1/1",
 *             "date": "前45年01月01日",
 *             "title": "罗马共和国开始使用儒略历",
 *             "e_id": "1"
 *         }
 *     ],
 *     "error_code": 0
 * }
 *
 * @author zJiaLi
 * @since 2021-03-21 11:28
 */
public class TodayOnHistoryResponse {

    /**
     * 日期
     */
    private String day;

    /**
     * 事件日期
     */
    private String date;

    /**
     * 事件标题
     */
    private String title;

    /**
     * 事件id,即下一接口中所用的e_id
     */
    private String e_id;

    public TodayOnHistoryResponse() {
    }

    public TodayOnHistoryResponse(String day, String date, String title, String e_id) {
        this.day = day;
        this.date = date;
        this.title = title;
        this.e_id = e_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }
}
