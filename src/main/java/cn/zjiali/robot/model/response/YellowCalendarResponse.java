package cn.zjiali.robot.model.response;

/**
 * 老黄历响应实体
 *
 * @author zJiaLi
 * @since 2021-04-04 20:33
 */
public class YellowCalendarResponse {

    private String id;
    private String yangli;
    private String yinli;
    private String wuxing;
    private String chongsha;
    private String baiji;
    private String jishen;
    private String yi;
    private String xiongshen;
    private String ji;

    public YellowCalendarResponse() {
    }

    public YellowCalendarResponse(String id, String yangli, String yinli, String wuxing, String chongsha, String baiji, String jishen, String yi, String xiongshen, String ji) {
        this.id = id;
        this.yangli = yangli;
        this.yinli = yinli;
        this.wuxing = wuxing;
        this.chongsha = chongsha;
        this.baiji = baiji;
        this.jishen = jishen;
        this.yi = yi;
        this.xiongshen = xiongshen;
        this.ji = ji;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYangli() {
        return yangli;
    }

    public void setYangli(String yangli) {
        this.yangli = yangli;
    }

    public String getYinli() {
        return yinli;
    }

    public void setYinli(String yinli) {
        this.yinli = yinli;
    }

    public String getWuxing() {
        return wuxing;
    }

    public void setWuxing(String wuxing) {
        this.wuxing = wuxing;
    }

    public String getChongsha() {
        return chongsha;
    }

    public void setChongsha(String chongsha) {
        this.chongsha = chongsha;
    }

    public String getBaiji() {
        return baiji;
    }

    public void setBaiji(String baiji) {
        this.baiji = baiji;
    }

    public String getJishen() {
        return jishen;
    }

    public void setJishen(String jishen) {
        this.jishen = jishen;
    }

    public String getYi() {
        return yi;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }

    public String getXiongshen() {
        return xiongshen;
    }

    public void setXiongshen(String xiongshen) {
        this.xiongshen = xiongshen;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }
}
