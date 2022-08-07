package com.zhonghuipro.zhuge.api.service;

import com.zhonghuipro.zhuge.api.model.CallResponse;
import com.zhonghuipro.zhuge.api.model.ReplyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zhonghuipro.zhuge.api.Constants.*;

@Service
public class UserService {

    @Autowired
    QuestionService questionService;

    /**
     * 根据用户联系方式，获取用户ID
     * @param caller 联系方式手机号或座机号
     * @return userId
     */
    public long getUserId(String caller) {
        // TODO
        return 0L;

    }

    /**
     * 用户接入
     * @return 响应结果
     */
    public CallResponse acceptUser() {
        CallResponse callResponse = new CallResponse();
        callResponse.setCallStatus(CALL_STATUS_START);
        callResponse.setData(CALL_CONTENT_START);
        callResponse.setIsCallIn(true);
        return callResponse;
    }

    /**
     * 用户交互
     * @param replyParam 状态信息
     * @return 响应结果
     */
    public CallResponse replyUser(ReplyParam replyParam) {
        CallResponse callResponse = new CallResponse();
        callResponse.setIsCallIn(false);
        if(replyParam.getCallStatus().equals(CALL_STATUS_START)){
            setTipsResponse(callResponse);
        }
        else if(replyParam.getCallStatus().equals(CALL_STATUS_QUESTION)){
            if(replyParam.getReplyContent().equals(REPLY_CONTENT_MUTE)){
                setTipsResponse(callResponse);
            }
            else if(replyParam.getReplyContent().equals(REPLY_CONTENT_RETRY)){
                setResponseGetQuestion(replyParam.getUserId(), callResponse);
            }
            else if(replyParam.getReplyContent().equals(REPLY_CONTENT_SKIP)){
                questionService.pickUserNewQuestion(replyParam.getUserId());
                setResponseGetQuestion(replyParam.getUserId(), callResponse);
            }
            else {
                // TODO 报错
            }
        }
        else if(replyParam.getCallStatus().equals(CALL_STATUS_TIPS)){
            setResponseGetQuestion(replyParam.getUserId(), callResponse);
        }
        return callResponse;
    }

    private void setResponseGetQuestion(Long userId, CallResponse callResponse) {
        String question = questionService.getUserCurrentQuestion(userId);
        if(question.isEmpty()){
            questionService.pickUserNewQuestion(userId);
            question = questionService.getUserCurrentQuestion(userId);
            if(question.isEmpty()){
                // TODO 如果没有问题怎么办？报错
            }
        }
        callResponse.setData(question);
        callResponse.setCallStatus(CALL_STATUS_QUESTION);
    }

    private void setTipsResponse(CallResponse callResponse) {
        callResponse.setCallStatus(CALL_STATUS_TIPS);
        callResponse.setData(CALL_CONTENT_TIPS);
    }
}
