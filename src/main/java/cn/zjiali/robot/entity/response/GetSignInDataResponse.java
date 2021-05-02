package cn.zjiali.robot.entity.response;


/**
 * 获取我的签到数据响应实体
 *
 * @author Gary
 * @since 2019-03-23 01:39
 */
public class GetSignInDataResponse {

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
     * @note 2020/10/31 修改
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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getTodayMsg() {
        return todayMsg;
    }

    public void setTodayMsg(String todayMsg) {
        this.todayMsg = todayMsg;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
