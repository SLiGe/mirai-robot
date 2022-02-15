package cn.zjiali.robot.model.response;


import lombok.Data;

/**
 * 签到数据类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:44
 */
@Data
public class SignInDataResponse {



    private Integer status;

    private String message;

    private DataResponse dataResponse;

    @Data
    public static class DataResponse {
        /**
         * QQ号
         */
        private String qq;

        /**
         * 积分数
         */
        private Integer points;

        /**
         * 月签到天数
         */
        private Integer monthDay;

        /**
         * 总签到天数
         */
        private Integer totalDay;

        /**
         * 当前等级
         *
         *  2020/10/31 修改
         */
        private String currentLevel;

        /**
         * 每日一句
         */
        private String todayMsg;

        /**
         * 排名
         */
        private int ranking;

        /**
         * 获得的积分
         */
        private Integer getPoints;
    }


}
