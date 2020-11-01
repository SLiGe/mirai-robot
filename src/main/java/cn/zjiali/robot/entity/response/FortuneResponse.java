package cn.zjiali.robot.entity.response;

/**
 * 运势实体
 *
 * @author zJiaLi
 * @since 2020-10-31 22:54
 */
public class FortuneResponse {
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

    public String getFortuneSummary() {
        return fortuneSummary;
    }

    public void setFortuneSummary(String fortuneSummary) {
        this.fortuneSummary = fortuneSummary;
    }

    public String getLuckyStar() {
        return luckyStar;
    }

    public void setLuckyStar(String luckyStar) {
        this.luckyStar = luckyStar;
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
    }

    public String getUnSignText() {
        return unSignText;
    }

    public void setUnSignText(String unSignText) {
        this.unSignText = unSignText;
    }
}
