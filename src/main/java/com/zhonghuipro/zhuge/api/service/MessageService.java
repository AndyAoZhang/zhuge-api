package com.zhonghuipro.zhuge.api.service;

import com.zhonghuipro.zhuge.api.model.CallResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    Logger logger = LoggerFactory.getLogger(MessageService.class);

    /**
     * 记录消息请求
     * @param callId 会话ID
     * @param contentType 消息类型
     * @param content 消息内容
     */
    public void logRequest(String callId, String contentType, String content) {
        logger.info(callId, contentType, content);
        // TODO 实现
    }

    /**
     * 记录消息响应
     * @param callId 会话ID
     * @param newDynamicId 新的业务ID
     * @param userId 用户ID
     * @param callResponse 响应内容
     */
    public void logResponse(String callId, String newDynamicId, Long userId, CallResponse callResponse) {
        logger.info(callId, newDynamicId, userId, callResponse);
        // TODO 实现
    }
}
