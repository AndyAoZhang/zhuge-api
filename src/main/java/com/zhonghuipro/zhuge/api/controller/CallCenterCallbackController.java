package com.zhonghuipro.zhuge.api.controller;

import com.zhonghuipro.zhuge.api.Constants;
import com.zhonghuipro.zhuge.api.helper.JsonUtils;
import com.zhonghuipro.zhuge.api.model.CallResponse;
import com.zhonghuipro.zhuge.api.model.DynamicInfo;
import com.zhonghuipro.zhuge.api.model.ReplyParam;
import com.zhonghuipro.zhuge.api.service.AliService;
import com.zhonghuipro.zhuge.api.service.MessageService;
import com.zhonghuipro.zhuge.api.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

import static com.zhonghuipro.zhuge.api.Constants.CALL_STATUS_START;

@RestController
public class CallCenterCallbackController {

    Logger logger = LoggerFactory.getLogger(CallCenterCallbackController.class);


    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    AliService aliService;


    @RequestMapping("/call/ai/result")
    public String rootRequest(
            @RequestParam Map<String, String> params,
            @RequestParam(name="call_id") String callId,
            @RequestParam Long timestamp,
            @RequestParam(name="content_type") String contentType,
            @RequestParam String content

    ) {
        logger.info(params.toString());
        logger.info(callId);
        logger.info(contentType);
        logger.info(content);

        messageService.logRequest(callId, contentType, content);


        CallResponse callResponse = null;

        JSONObject jsonObject = JsonUtils.getJsonObject(content);
        // TODO fix it null
        assert jsonObject != null;
        Long userId;

        if(Objects.equals(contentType, Constants.CALL_TYPE_CALL_IN)){
            String caller = null;
            try {
                caller = jsonObject.getString("caller");
            } catch (JSONException ignore) {
            }

            userId = userService.getUserId(caller);
            callResponse = userService.acceptUser();
        }
        else {
            String dynamicId = null;
            try {
                dynamicId = jsonObject.getString("dynamic_id");
            } catch (JSONException ignore) {
            }
            // TODO 不能为空
            assert dynamicId != null;
            DynamicInfo dynamicInfo = getDynamicInfo(dynamicId);
            userId = dynamicInfo.getUserId();
            ReplyParam replyParam = new ReplyParam();
            replyParam.setUserId(userId);
            replyParam.setCallStatus(dynamicInfo.getCallStatus());
            replyParam.setReplyContent(aliService.getReplyContent(contentType, content));
            callResponse = userService.replyUser(replyParam);
        }
        String newDynamicId = getDynamicId(userId, callResponse.getCallStatus());
        messageService.logResponse(callId, newDynamicId, userId, callResponse);
        String res = aliService.getAliCallResponse(callId, callResponse).toString();
        logger.info(res);
        return res;


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

    }

    private String getDynamicId(Long userId, String callStatus) {
        return String.format("v1_%d_%s", userId, callStatus);
    }

    private DynamicInfo getDynamicInfo(String dynamicId) {
        // TODO 安全校验
        String[] items = dynamicId.split("_");
        DynamicInfo dynamicInfo = new DynamicInfo();
        dynamicInfo.setVersion(items[0]);
        dynamicInfo.setUserId(Long.valueOf(items[1]));
        dynamicInfo.setCallStatus(items[2]);
        return dynamicInfo;
    }


    public static void main(String[] args) {
        CallCenterCallbackController callCenterCallbackController = new CallCenterCallbackController();
        String dynamicId = callCenterCallbackController.getDynamicId(0L, CALL_STATUS_START);
        System.out.println(dynamicId);
        DynamicInfo dynamicInfo = callCenterCallbackController.getDynamicInfo(dynamicId);
        System.out.println(dynamicInfo);
    }

}
//
//curl --header "Content-Type: application/json" \
//        --request POST \
//        --data '{"username":"xyz","password":"xyz"}' \
//        http://localhost:3000/api/login