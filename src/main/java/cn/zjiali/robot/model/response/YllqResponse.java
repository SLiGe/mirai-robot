package cn.zjiali.robot.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author zJiaLi
 * @since 2021-04-08 18:44
 */
@Deprecated
public class YllqResponse {

    @SerializedName("number1")
    private String number1;
    @SerializedName("number2")
    private String number2;
    @SerializedName("haohua")
    private String haohua;
    @SerializedName("shiyi")
    private String shiyi;
    @SerializedName("jieqian")
    private String jieqian;
    @SerializedName("zhushi")
    private String zhushi;
    @SerializedName("baihua")
    private String baihua;
    @SerializedName("type")
    private String type;

    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getHaohua() {
        return haohua;
    }

    public void setHaohua(String haohua) {
        this.haohua = haohua;
    }

    public String getShiyi() {
        return shiyi;
    }

    public void setShiyi(String shiyi) {
        this.shiyi = shiyi;
    }

    public String getJieqian() {
        return jieqian;
    }

    public void setJieqian(String jieqian) {
        this.jieqian = jieqian;
    }

    public String getZhushi() {
        return zhushi;
    }

    public void setZhushi(String zhushi) {
        this.zhushi = zhushi;
    }

    public String getBaihua() {
        return baihua;
    }

    public void setBaihua(String baihua) {
        this.baihua = baihua;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
