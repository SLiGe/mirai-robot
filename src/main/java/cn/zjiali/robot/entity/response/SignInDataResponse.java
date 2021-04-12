package cn.zjiali.robot.entity.response;


/**
 * 签到数据类
 *
 * @author zJiaLi
 * @since 2020-10-30 22:44
 */
public class SignInDataResponse {


    private String qq;

    private String integral;


    private String todayMsg;

    private int ranking;

    private String status;

    private int points;

    private String currentLevel;

    private String monthDay;

    private String totalDay;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

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

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
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
