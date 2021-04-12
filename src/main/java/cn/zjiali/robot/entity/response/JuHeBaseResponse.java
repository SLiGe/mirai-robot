package cn.zjiali.robot.entity.response;

/**
 * 聚合数据响应基类
 *
 * @author zJiaLi
 * @since 2021-03-21 10:39
 */
public class JuHeBaseResponse<T> {

    /**
     * 返回说明
     */
    private String reason;

    /**
     * 返回结果集
     */
    private T result;

    /**
     * 返回码
     */
    private Integer error_code;

    public JuHeBaseResponse() {
    }

    public JuHeBaseResponse(String reason, T result, Integer error_code) {
        this.reason = reason;
        this.result = result;
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }
}
