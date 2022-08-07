package com.zhonghuipro.zhuge.api.model;

import lombok.Data;

@Data
public class CallResponse {
    Long id;
    Long requestId;
    String data;
    String callStatus;
}
