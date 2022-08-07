package com.zhonghuipro.zhuge.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    Logger logger = LoggerFactory.getLogger(QuestionService.class);
    /**
     * 获取用户题库
     * @param userId 用户ID
     * @return 题目
     */
    public String getUserCurrentQuestion(Long userId) {
        logger.info(String.format("getUserCurrentQuestion: %d", userId));

        // TODO
        return "请做个自我介绍";
    }

    /**
     * 获取用户新的题目
     * @param userId 用户ID
     */
    public void pickUserNewQuestion(Long userId) {
        logger.info(String.format("pickUserNewQuestion: %d", userId));
        // TODO
    }
}
