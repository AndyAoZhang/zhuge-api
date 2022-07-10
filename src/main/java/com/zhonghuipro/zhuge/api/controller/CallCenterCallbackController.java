package com.zhonghuipro.zhuge.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CallCenterCallbackController {

    Logger logger = LoggerFactory.getLogger(CallCenterCallbackController.class);

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
        String action = "donothing";
        String actionCode = "";
        String action_code_param = "{}";
        boolean isCallIn = "callin".equals(content_type);
        if (isCallIn) {
            action = "answer";
            actionCode = "http://alicom-fc-record-biz.oss-cn-hangzhou.aliyuncs.com/Freeswitch_ac4e5748-ffa4-11ec-a2e9-69206b587a01_record.wav?Expires=1657989143&OSSAccessKeyId=bypFNbGJVk73PsLI&Signature=PtgFgia6U4%2Bet8Eva8jVdUrh%2B%2Fk%3D";
        }
        if ("normal".equals(content_type)) {
            action = "play";
            actionCode = "$name$";
            action_code_param = "{\\\"name\\\":\\\"牛逼\\\"}";
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


    @RequestMapping("/call/ai/records")
    public String rootRequest(
            @RequestBody String records
    ) {
        logger.info("records: " + records);
        return "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"成功\"\n" +
                "}\n";
    }
}