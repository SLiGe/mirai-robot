package cn.zjiali.robot.factory;

import cn.zjiali.robot.config.plugin.PluginConfig;
import cn.zjiali.robot.constant.PluginCode;
import cn.zjiali.robot.constant.PluginProperty;
import cn.zjiali.robot.model.response.*;
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
        if (PluginConfig.getCommand(PluginCode.YELLOW_CALENDAR).equals(message)) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HashMap<String, Object> params = new HashMap<>();
            params.put("key", PluginConfig.getApiKey(PluginCode.YELLOW_CALENDAR));
            params.put("date", date);
            String response = HttpUtil.get(PluginConfig.getApiURL(PluginCode.YELLOW_CALENDAR), params);
            Type type = new TypeToken<JuHeBaseResponse<YellowCalendarResponse>>() {
            }.getType();
            JuHeBaseResponse<YellowCalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
            if (baseResponse.getError_code() == 0) {
                YellowCalendarResponse yellowCalendarResponse = baseResponse.getResult();
                return MessageUtil.replaceMessage(PluginConfig.getTemplate(PluginCode.YELLOW_CALENDAR), yellowCalendarResponse);
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
        if (PluginConfig.getCommand(PluginCode.TODAY_HISTORY).equals(message)) {
            //日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dateStr = month + "/" + day;
            HashMap<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("key", PluginConfig.getApiKey(PluginCode.TODAY_HISTORY));
            paramMap.put("date", dateStr);
            String response = HttpUtil.get(PluginConfig.getApiURL(PluginCode.TODAY_HISTORY), paramMap);
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
        if (PluginConfig.getCommand(PluginCode.CALENDAR).equals(message)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dateStr = year + "-" + month + "-" + day;
            HashMap<String, Object> params = new HashMap<>();
            params.put("key", PluginConfig.getApiKey(PluginCode.CALENDAR));
            params.put("date", dateStr);
            String response = HttpUtil.get(PluginConfig.getApiURL(PluginCode.CALENDAR), params);
            Type type = new TypeToken<JuHeBaseResponse<CalendarResponse>>() {
            }.getType();
            JuHeBaseResponse<CalendarResponse> baseResponse = JsonUtil.toObjByType(response, type);
            if (baseResponse.getError_code() == 0) {
                CalendarResponse calendarResponse = baseResponse.getResult();
                return MessageUtil.replaceMessage(PluginConfig.getTemplate(PluginCode.CALENDAR), calendarResponse);
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
        String msgContentBuilder = "";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Long.toString(senderQQ));
        jsonObject.addProperty("isOne", PluginConfig.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_DAY_ONE));
        jsonObject.addProperty("isGroup", msgType == 1 ? 0 : 1);
        jsonObject.addProperty("isIntegral", PluginConfig.getConfigVal(PluginCode.FORTUNE, PluginProperty.FORTUNE_POINT));
        jsonObject.addProperty("groupNum", Long.toString(groupNum));
        String response = HttpUtil.post(PluginConfig.getApiURL(PluginCode.FORTUNE), jsonObject);
        if (response == null) {
            return "运势服务故障,请联系管理员!";
        }
        Type type = new TypeToken<RobotBaseResponse<FortuneResponse>>() {
        }.getType();
        RobotBaseResponse<FortuneResponse> robotBaseResponse = JsonUtil.toObjByType(response, type);
        if (robotBaseResponse.getStatus() == 500) { //server has  error
            return null;
        }
        FortuneResponse responseData = robotBaseResponse.getData();
        int dataStatus = responseData.getStatus();
        if (dataStatus == 201) {
            return null; // already get fortune
        }
        if (dataStatus == 200) {
            FortuneResponse.DataResponse dataResponse = responseData.getDataResponse();
            return MessageUtil.replaceMessage(PluginConfig.getTemplate(PluginCode.FORTUNE), dataResponse);

        }
        return msgContentBuilder;
    }


    /**
     * 获取一言
     *
     * @return
     */
    public static String getSen() {
        String sen = HttpUtil.get(PluginConfig.getApiURL(PluginCode.ONE_SEN));
        JsonObject jsonObject = new Gson().fromJson(sen, JsonObject.class);
        return jsonObject.get("data").getAsString();
    }
}
