package com.zhonghuipro.zhuge.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CallCenterCallbackController {

    Logger logger = LoggerFactory.getLogger(CallCenterCallbackController.class);

    @RequestMapping("/call/ai/result")
    public String rootRequest(
//            @RequestParam Map<String, String> params,
//            @RequestParam String call_id,
//            @RequestParam Long timestamp,
//            @RequestParam String content_type,
//            @RequestParam String content

    ) {
        return "{\n" +
                "    \"result\": \"success\",\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"call_id\": \"100001616500^100001871490\",\n" +
                "        \"action\": \"play\",\n" +
                "        \"action_code\": \"411111\",\n" +
                "        \"extend\": \"111\"\n" +
                "    }\n" +
                "}\n";
//        return getDemoResult(params, call_id, content_type, content);
    }

    private String getDemoResult(Map<String, String> params, String call_id, String content_type, String content) {
        logger.info(params.toString());
        logger.info(call_id);
        logger.info(content_type);
        logger.info(content);
        String action = "donothing";
        String actionCode = "";
        String action_code_param = "{}";
        if ("callin".equals(content_type)) {
            action = "answer";
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
                "        \"action\": \"" + action + "\",\n" +
                "        \"action_code\": \"" + actionCode + "\"," +
                "        \"action_code_param\": \"" + action_code_param + "\"" +
                "    }\n" +
                "}";
        logger.info("result: " + result);
        return result;
    }
}