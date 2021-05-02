package cn.zjiali.robot.entity.response;


/**
 * 签到数据类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:44
 */
public class SignInDataResponse {


    private String qq;

    /**
     * 获得的积分
     */
    private Integer getPoints;


    private String todayMsg;

    private int ranking;

    private String status;

    private Integer points;

    private String currentLevel;

    private String monthDay;

    private String totalDay;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getGetPoints() {
        return getPoints;
    }

    public void setGetPoints(Integer getPoints) {
        this.getPoints = getPoints;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    public String getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(String totalDay) {
        this.totalDay = totalDay;
    }
}
