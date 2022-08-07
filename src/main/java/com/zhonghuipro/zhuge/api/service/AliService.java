package com.zhonghuipro.zhuge.api.service;

import com.zhonghuipro.zhuge.api.Constants;
import com.zhonghuipro.zhuge.api.helper.JsonUtils;
import com.zhonghuipro.zhuge.api.model.CallResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AliService {

    Logger logger = LoggerFactory.getLogger(AliService.class);


    /**
     * 根据阿里语义解析业务语义
     *
     * @param contentType 阿里业务类型
     * @param content     阿里语义结果
     * @return 业务语义
     */
    public String getReplyContent(String contentType, String content) {
        if (contentType.equals(Constants.ALI_CONTENT_TYPE_MUTE)) {
            return Constants.REPLY_CONTENT_MUTE;
        }
        if (contentType.equals(Constants.ALI_CONTENT_TYPE_NORMAL)) {
            JSONObject jsonObject = JsonUtils.getJsonObject(content);
            String words = JsonUtils.getJsonString(jsonObject, "words");
            if (words.contains("再说一遍")) {
                return Constants.REPLY_CONTENT_RETRY;
            } else if (
                    words.contains("跳过") ||
                            words.contains("回答完毕")
            ) {
                // TODO 回答内容包含这些词语可能会误判。
                return Constants.REPLY_CONTENT_SKIP;
            }
        }
        return Constants.REPLY_CONTENT_OTHER;
    }

    public JSONObject getAliCallResponse(String callId, CallResponse callResponse) {

//        String action = "donothing";
//        String actionCode = "$tts$";
//        String action_code_param = "{}";
//        boolean isCallIn = Constants.CALL_TYPE_CALL_IN.equals(contentType);
//        if (isCallIn) {
//            action = "answer";
//            action_code_param = "{\\\"tts\\\":\\\"您好，我是钟二。有事请留言。\\\"}";
////            actionCode = "9bd7634a-9aca-4e5c-8a5b-9bfca7280ff0.wav";
//        }
//        if ("normal".equals(contentType)) {
//            action = "play";
//            action_code_param = "{\\\"tts\\\":\\\"好的\\\"}";
//        }
//        String result = "{\n" +
//                "    \"result\": \"success\",\n" +
//                "    \"msg\": \"成功\",\n" +
//                "    \"code\": 200,\n" +
//                "    \"data\": {\n" +
//                "        \"call_id\": \"" + callId + "\",\n" +
//                (isCallIn ?"\"record_flag\": true," : "") +
//                "        \"action\": \"" + action + "\",\n" +
//                "        \"action_code\": \"" + actionCode + "\"," +
//                "        \"action_code_param\": \"" + action_code_param + "\"" +
//                "    }\n" +
//                "}";
//        logger.info("result: " + result);
//        return result;

        JSONObject aliCallResponse = new JSONObject();

        try {

            aliCallResponse.put("result", "success");
            aliCallResponse.put("msg", "成功");
            aliCallResponse.put("code", 200);

            JSONObject aliCallResponseData = new JSONObject();
            aliCallResponse.put("data", aliCallResponseData);
            aliCallResponseData.put("call_id", callId);
            if(callResponse.getIsCallIn()) {
                aliCallResponseData.put("record_flag", true);
                aliCallResponseData.put("action", "answer");
            }
            else {
                aliCallResponseData.put("action", "play");
            }
            aliCallResponseData.put("action_code", "$tts$");
            JSONObject actionCodeParam = new JSONObject();
            aliCallResponseData.put("action_code_param", actionCodeParam.toString());
            actionCodeParam.put("tts", callResponse.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return aliCallResponse;
    }

    public static void main(String[] args) {
        AliService aliService = new AliService();
        String replyContent = aliService.getReplyContent("mute", "{\"role\": \"B\",\"identity\": \"id2\",\"words\": “跳过\",\"begin_offset\": \"1000\",\"end_offset\": \"9000\",\"begin_time\": \"2017-06-01 10:00:00\"}");
        System.out.println(replyContent);
    }
    public static void test() {
        AliService aliService = new AliService();
        CallResponse callResponse = new CallResponse();
        String callId="1100001616500^10000187****";
        callResponse.setData("您好啊");
        JSONObject aliCallResponse = aliService.getAliCallResponse(callId, callResponse);
        System.out.println(aliCallResponse.toString());

    }
}
