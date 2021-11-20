package cn.zjiali.robot.model.response;

/**
 * @author zJiaLi
 * @since 2021-05-08 10:02
 */
public class RobotBaseResponse <T>{

    private int status;

    private String message;

    private T data;

    public RobotBaseResponse() {
    }

    public RobotBaseResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
