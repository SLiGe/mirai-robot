package cn.zjiali.robot.entity.response;

import lombok.Data;

/**
 * 运势实体
 *
 * @author zJiaLi
 * @since 2020-10-31 22:54
 */
@Data
public class FortuneResponse {

    private int status;

    private String message;

    private DataResponse dataResponse;

    @Data
    public static class DataResponse {

        private Long id;

        /**
         * 运情总结
         */
        private String fortuneSummary;

        /**
         * 幸运星
         */
        private String luckyStar;

        /**
         * 签文
         */
        private String signText;

        /**
         * 解签
         */
        private String unSignText;
    }


}
