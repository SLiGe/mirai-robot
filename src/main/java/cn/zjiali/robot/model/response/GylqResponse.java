package cn.zjiali.robot.model.response;


import com.google.gson.annotations.SerializedName;

/**
 * @author zJiaLi
 * @since 2021-04-08 18:38
 */
@Deprecated
public class GylqResponse {

    @SerializedName(value = "number1")
    private String number1;
    @SerializedName(value = "number2")
    private String number2;
    @SerializedName(value = "haohua")
    private String haohua;
    @SerializedName(value = "qianyu")
    private String qianyu;
    @SerializedName(value = "shiyi")
    private String shiyi;
    @SerializedName(value = "jieqian")
    private String jieqian;
    @SerializedName(value = "type")
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

    public String getQianyu() {
        return qianyu;
    }

    public void setQianyu(String qianyu) {
        this.qianyu = qianyu;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
