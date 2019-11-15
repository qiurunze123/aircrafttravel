package com.travel.function.service;

import com.rabbitmq.client.Channel;
import com.travel.function.rabbitmq.MiaoShaMessage;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author 邱润泽 bullock
 */
public interface RabbitMqService {
    public void receive(String message);

    public void sendMiaoshaMessage(MiaoShaMessage mm);
}
