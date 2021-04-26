package cn.zjiali.robot.factory;

import cn.zjiali.robot.config.plugin.CalendarConfig;
import cn.zjiali.robot.config.plugin.FortuneConfig;
import cn.zjiali.robot.config.plugin.TodayOfHistoryConfig;
import cn.zjiali.robot.config.plugin.YellowCalendarConfig;
import cn.zjiali.robot.constant.ServerUrl;
import cn.zjiali.robot.entity.response.*;
import cn.zjiali.robot.util.HttpUtil;
import cn.zjiali.robot.util.JsonUtil;
import cn.zjiali.robot.util.MessageUtil;
import cn.zjiali.robot.util.ObjectUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 消息工厂
 *
 * @author zJiaLi
 * @since 2021-04-04 21:04
 */
public class MessageFactory {

    /**
     * 获取老黄历消息
     *
     * @param message
     * @return
     */
    public static String getYellowCalendarMessage(String message) {
        if (YellowCalendarConfig.yellow_calendar_command.equals(message)) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HashMap<String, String> params = new HashMap<>();
            params.put("key", CalendarConfig.calendar_key);
            params.put("date", date);
            String response = HttpUtil.httpGet(YellowCalendarConfig.url, params);
            Type type = new TypeToken<JuHeBaseResponse<YellowCalendarResponse>>() {
            }.getType();
            JuHeBaseResponse<YellowCalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
            if (baseResponse.getError_code() == 0) {
                YellowCalendarResponse yellowCalendarResponse = baseResponse.getResult();
                return MessageUtil.replaceMessage(YellowCalendarConfig.template, yellowCalendarResponse);
            }
        }
        return null;
    }

    /**
     * 获取历史上的今天消息
     *
     * @param message
     * @return
     */
    public static String getTodayOnHistoryMessage(String message) {
        if (TodayOfHistoryConfig.today_of_history_command.equals(message)) {
            //日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dateStr = month + "/" + day;
            HashMap<String, String> paramMap = new HashMap<>(2);
            paramMap.put("key", TodayOfHistoryConfig.key);
            paramMap.put("date", dateStr);
            String response = HttpUtil.httpGet(ServerUrl.TODAY_ON_HISTORY_URL, paramMap);
            JuHeBaseResponse<List<TodayOnHistoryResponse>> todayOnHistoryResponse = JsonUtil.toObjByType(response, new TypeToken<JuHeBaseResponse<List<TodayOnHistoryResponse>>>() {
            }.getType());
            if (todayOnHistoryResponse != null) {
                if (todayOnHistoryResponse.getError_code() == 0) {
                    List<TodayOnHistoryResponse> todayOnHistoryResponseResult = todayOnHistoryResponse.getResult();
                    if (!ObjectUtil.isNullOrEmpty(todayOnHistoryResponseResult)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (TodayOnHistoryResponse item : todayOnHistoryResponseResult) {
                            stringBuilder.append(item.getDate()).append("\n").append(item.getTitle());
                        }
                        return stringBuilder.toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取万年历消息
     *
     * @param message 原始消息
     * @return
     */
    public static String getCalendarMessage(String message) {
        if (CalendarConfig.calendar_command.equals(message)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dateStr = year + "-" + month + "-" + day;
            HashMap<String, String> params = new HashMap<>();
            params.put("key", CalendarConfig.calendar_key);
            params.put("date", dateStr);
            String response = HttpUtil.httpGet(ServerUrl.CALENDAR_DAY_URL, params);
            Type type = new TypeToken<JuHeBaseResponse<CalendarResponse>>() {
            }.getType();
            JuHeBaseResponse<CalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
            if (baseResponse.getError_code() == 0) {
                CalendarResponse calendarResponse = baseResponse.getResult();
                return MessageUtil.replaceMessage(CalendarConfig.template, calendarResponse);
            }
        }
        return null;
    }


    /**
     * 获取运势消息
     *
     * @param senderQQ 发送人QQ
     * @param groupNum 群组
     * @param msgType  消息类型
     * @return
     */
    public static String getFortuneMsg(long senderQQ, long groupNum, int msgType) {
        StringBuilder msgContentBuilder = new StringBuilder();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("isOne", FortuneConfig.fortune_day_one);
        jsonObject.addProperty("isGroup", msgType == 1 ? 0 : 1);
        jsonObject.addProperty("isIntegral", FortuneConfig.fortune_point);
        jsonObject.addProperty("groupNum", Long.toString(groupNum));
        String response = HttpUtil.httpPost(ServerUrl.FORTUNE_URL, jsonObject);
        System.out.println(response);
        if (response == null) {
            return "运势服务故障,请联系管理员!";
        }
        JsonObject resObj = JsonUtil.json2obj(response, JsonObject.class);
        final String status = resObj.get("status").getAsString();
        if ("40010".equals(status)) {
            return null;
        }
        if ("20011".equals(status)) {
            String data = resObj.get("data").toString();
            FortuneResponse fortuneResponse = JsonUtil.json2obj(data, FortuneResponse.class);
            msgContentBuilder.append("\uD83C\uDF13您的今日运势为：").append(fortuneResponse.getFortuneSummary());
            if ("1".equals(FortuneConfig.fortune_star_num))
                msgContentBuilder.append("\n\uD83C\uDF1F星指数：").append(fortuneResponse.getLuckyStar());
            if ("1".equals(FortuneConfig.fortune_sign_text))
                msgContentBuilder.append("\n\uD83D\uDCD7签文：").append(fortuneResponse.getSignText());
            if ("1".equals(FortuneConfig.fortune_un_sign))
                msgContentBuilder.append("\n\uD83D\uDCDD解签：").append(fortuneResponse.getUnSignText());

        }
        return msgContentBuilder.toString();
    }


    /**
     * 获取一言
     *
     * @return
     */
    public static String getSen() {
        String sen = HttpUtil.httpGet(ServerUrl.SEN_URL);
        JsonObject jsonObject = new Gson().fromJson(sen, JsonObject.class);
        return jsonObject.get("data").getAsString();
    }
}
