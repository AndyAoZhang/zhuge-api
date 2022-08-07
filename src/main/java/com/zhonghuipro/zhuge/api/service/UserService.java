package com.zhonghuipro.zhuge.api.service;

import com.zhonghuipro.zhuge.api.model.CallResponse;
import com.zhonghuipro.zhuge.api.model.DynamicInfo;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * 根据用户联系方式，获取用户ID
     * @param caller 联系方式手机号或座机号
     * @return 用户IDuserId
     */
    public long getUserId(String caller) {
        // TODO
        return 0L;

    }

    /**
     * 用户接入
     * @param userId 用户ID
     * @return 响应结果
     */
    public CallResponse acceptUser(long userId) {
        // TODO
        return null;
    }

    /**
     * 用户交互
     * @param dynamicInfo 状态信息
     * @return 响应结果
     */
    public CallResponse replyUser(DynamicInfo dynamicInfo) {
        // TODO
        return null;
    }
}
