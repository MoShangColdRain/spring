package com.example.demo.service;

import com.example.demo.enums.ExchangeEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public interface QueueMessageService extends RabbitTemplate.ConfirmCallback{

    void send(Object message, ExchangeEnum exchangeEnum, String routingKey) throws Exception;

}
