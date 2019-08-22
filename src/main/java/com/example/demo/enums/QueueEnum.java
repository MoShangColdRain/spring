package com.example.demo.enums;

/**
 * 队列配置枚举
 */
public enum QueueEnum {

    /**
     * 用户注册
     * 创建账户消息队列
     */
    USER_REGISTER_CREATE_ACCOUNT("register.account"),
    /**
     * 用户注册
     * 发送注册成功邮件消息队列
     */
    USER_REGISTER_SEND_MAIL("register.mail")
    ;
    /**
     * 队列名称
     */
    private String name;

    public String getName() {
        return name;
    }


    QueueEnum(String name) {
        this.name = name;
    }
}
