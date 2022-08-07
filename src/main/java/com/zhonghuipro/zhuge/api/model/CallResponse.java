package com.zhonghuipro.zhuge.api.model;

import lombok.Data;

@Data
public class CallResponse {
    Long id;
    Long requestId;
    String data;
    String callStatus;
    private boolean isCallIn;

    public void setIsCallIn(boolean isCallIn) {
        this.isCallIn = isCallIn;
    }

    public boolean getIsCallIn() {
        return isCallIn;
    }
}
