package cn.zjiali.robot.model.response;

import lombok.Data;

/**
 * 月老灵签
 *
 * @author zJiaLi
 * @since 2022-01-28 19:43
 */
@Data
public class YllqRes {

    private Integer id;

    private String title;

    /**
     * 签诗
     */
    private String qian_shi;

    /**
     * 图片
     */
    private String lq_img;

    /**
     * 解签
     */
    private String jie_qian;

    /**
     * 注
     */
    private String zhu;

    /**
     * 白话浅释
     */
    private String bhqs;
}
