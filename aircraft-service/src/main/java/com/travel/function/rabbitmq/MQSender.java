package com.travel.function.rabbitmq;

import com.travel.function.entity.MiaoShaMessage;
import com.travel.function.redisManager.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {


	@Autowired
    AmqpTemplate amqpTemplate ;
	
	public void sendMiaoshaMessage(MiaoShaMessage mm) {
		try {
			String msg = RedisClient.beanToString(mm);
			log.info("mq发送订单信息：msg{}",msg);
			amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
		} catch (AmqpException e) {
			throw new AmqpException("***mq信息发送失败!***");
		}
	}
	
	public void send(Object message) {
		String msg = RedisClient.beanToString(message);
		log.info("mq发送订单信息：msg{}",msg);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}

	public void sendTopic(Object message) {
		String msg = RedisClient.beanToString(message);
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}

}
