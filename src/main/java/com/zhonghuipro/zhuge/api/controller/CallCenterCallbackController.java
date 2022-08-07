package com.zhonghuipro.zhuge.api.controller;

import com.zhonghuipro.zhuge.api.Constants;
import com.zhonghuipro.zhuge.api.helper.JsonUtils;
import com.zhonghuipro.zhuge.api.model.CallResponse;
import com.zhonghuipro.zhuge.api.model.DynamicInfo;
import com.zhonghuipro.zhuge.api.service.MessageService;
import com.zhonghuipro.zhuge.api.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class CallCenterCallbackController {

    Logger logger = LoggerFactory.getLogger(CallCenterCallbackController.class);


    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    @RequestMapping("/call/ai/result")
    public String rootRequest(
            @RequestParam Map<String, String> params,
            @RequestParam String call_id,
            @RequestParam Long timestamp,
            @RequestParam String content_type,
            @RequestParam String content

    ) {
        logger.info(params.toString());
        logger.info(call_id);
        logger.info(content_type);
        logger.info(content);


        CallResponse callResponse = null;

        JSONObject jsonObject = JsonUtils.getJsonObject(content);
        // TODO fix it null
        assert jsonObject != null;
        Long userId = null;


        if(Objects.equals(content_type, Constants.CALL_TYPE_CALL_IN)){
            String caller = null;
            try {
                caller = jsonObject.getString("caller");
            } catch (JSONException ignore) {
            }

            userId = userService.getUserId(caller);
            callResponse = userService.acceptUser(userId);
        }
        else {
            String dynamicId = null;
            try {
                dynamicId = jsonObject.getString("dynamic_id");
            } catch (JSONException ignore) {
            }
            DynamicInfo dynamicInfo = getDynamicInfo(dynamicId);
            callResponse = userService.replyUser(dynamicInfo);
            userId = dynamicInfo.getUserId();
        }
        String newDynamicId = getDynamicId(userId, callResponse.getCallStatus());

        String action = "donothing";
        String actionCode = "$tts$";
        String action_code_param = "{}";
        boolean isCallIn = Constants.CALL_TYPE_CALL_IN.equals(content_type);
        if (isCallIn) {
            action = "answer";
            action_code_param = "{\\\"tts\\\":\\\"您好，我是钟二。有事请留言。\\\"}";
//            actionCode = "9bd7634a-9aca-4e5c-8a5b-9bfca7280ff0.wav";
        }
        if ("normal".equals(content_type)) {
            action = "play";
            action_code_param = "{\\\"tts\\\":\\\"好的\\\"}";
        }
        String result = "{\n" +
                "    \"result\": \"success\",\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"call_id\": \"" + call_id + "\",\n" +
                (isCallIn ?"\"record_flag\": true," : "") +
                "        \"action\": \"" + action + "\",\n" +
                "        \"action_code\": \"" + actionCode + "\"," +
                "        \"action_code_param\": \"" + action_code_param + "\"" +
                "    }\n" +
                "}";
        logger.info("result: " + result);
        return result;
    }

    private String getDynamicId(Long userId, String callStatus) {
        return String.format("v1_%d_%s", userId, callStatus);
    }

    private DynamicInfo getDynamicInfo(String dynamicId) {
        // TODO 安全校验
        String[] items = dynamicId.split("_");
        DynamicInfo dynamicInfo = new DynamicInfo();
        dynamicInfo.setUserId(Long.valueOf(items[1]));
        dynamicInfo.setCallStatus(items[2]);
        return dynamicInfo;
    }

}