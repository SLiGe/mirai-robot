package cn.zjiali.robot.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 观音灵签
 *
 * @author zJiaLi
 * @since 2022-01-28 13:14
 */
@Data
public class GylqRes {

    /**
     * 序号
     */
    private Integer id;

    /**
     * 签语
     */
    private String title;

    /**
     * 诗曰
     */
    private String shi_yue;

    /**
     * 诗意
     */
    private String shi_yi;

    /**
     * 解曰
     */
    private String jie_yue;

    /**
     * 详解
     */
    private String xiang_jie;

    /**
     * 仙机
     */
    private String xian_ji;

    /**
     * 灵签图片
     */
    private String lq_img;

    /**
     * 整体解签
     */
    private String ztjq;

    /**
     * 本签精髓
     */
    private String bqjs;

    /**
     * 凡事做事
     */
    private String fszs;

    /**
     * 爱情婚姻
     */
    private String aqhy;

    /**
     * 工作求职 创业事业
     */
    private String gzqz;

    /**
     * 考试竞赛 升迁竞选
     */
    private String ksjs;

    /**
     * 投资理财
     */
    private String tzlc;

    /**
     * 经商生意
     */
    private String jssy;

    /**
     * 房地交易
     */
    private String fdjy;

    /**
     * 治病健康
     */
    private String zbjk;

    /**
     * 转换变更
     */
    private String zhbg;

    /**
     * 求孕求子
     */
    private String qyqz;

    /**
     * 官司诉讼
     */
    private String gsss;

    /**
     * 寻人寻物
     */
    private String xrxw;

    /**
     * 远行出国
     */
    private String yxcg;

    /**
     * 签诗故事
     */
    private List<Qsgs> qsgsList = new ArrayList<>();

    @Data
    public static class Qsgs{
        private String title;
        private String content;
    }
}
