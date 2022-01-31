package cn.zjiali.robot.model.response;

import lombok.Data;

/**
 * 财神灵签
 *
 * @author zJiaLi
 * @since 2022-01-29 09:01
 */
@Data
public class CslqRes {

    private Integer id;

    /**
     * 概述
     */
    private String desc;

    /**
     * 诗曰
     */
    private String shi_yue;

    private String lq_img;

    /**
     * 米力仙注
     */
    private String mlxz;

    /**
     * 吉凶
     */
    private String ji_xiong;
}
