package cn.zjiali.robot.entity.response;

/**
 * 笑话大全返回实体
 *
 * @author zJiaLi
 * @since 2021-04-04 10:48
 */
public class JokeResponse {

    private String title;

    private String content;

    public JokeResponse() {
    }

    public JokeResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
