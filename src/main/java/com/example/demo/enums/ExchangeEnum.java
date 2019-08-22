package com.example.demo.enums;

/**
 * rabbitmq交换配置枚举
 */
public enum ExchangeEnum {

    USER_REGISTER_TOPIC_EXCHANGE("register.fanout.exchange")
    ;
    private String name;

    public String getName() {
        return name;
    }

    ExchangeEnum(String name) {
        this.name = name;
    }
}
