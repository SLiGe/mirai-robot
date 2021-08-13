package cn.zjiali.robot.manager;

/**
 * @author zJiaLi
 * @since 2021-08-13 10:58
 */
public interface MessageManager {

    default String preProduce() {
        return "";
    }

    String produce(String doName, Object... params);

    default String afterProduce() {
        return "";
    }

}
