# zhuge-api

#### 介绍
诸葛系统后端项目

#### 软件架构
软件架构说明
```mermaid
graph LR
subgraph SA[zhuge-api]
    sa1[互动接口]
end

subgraph SB[zhuge-data]
    sb1[通知接口]
end

subgraph SC[阿里云]
    sc1[电话号码]
end

subgraph SP[用户]
    sp1[张三]
end

sp1 --呼入/说话/静音/挂断 --> sc1 -- 输入用户操作 --> sa1

sa1 --输出回复操作--> sc1 --语音/TTS/静音/挂断--> sp1

sc1 -- 批量通知 --> sb1

```

#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 feature_xxx 分支
3.  提交代码
4.  新建 Pull Request
