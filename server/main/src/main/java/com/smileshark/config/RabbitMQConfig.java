package com.smileshark.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 正常业务交换机
    public static final String BUSINESS_EXCHANGE = "business.exchange";
    // 正常业务队列
    public static final String BUSINESS_QUEUE = "business.queue";
    // 业务队列路由键
    public static final String BUSINESS_ROUTING_KEY = "business.key";

    // 死信交换机
    public static final String DLX_EXCHANGE = "dlx.exchange";
    // 死信队列
    public static final String DLX_QUEUE = "dlx.queue";
    // 死信路由键
    public static final String DLX_ROUTING_KEY = "dlx.key";

    // 声明业务交换机
    @Bean
    public DirectExchange businessExchange() {
        return new DirectExchange(BUSINESS_EXCHANGE);
    }

    // 声明死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    // 声明死信队列
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    // 绑定死信队列到死信交换机
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(DLX_ROUTING_KEY);
    }

    // 声明业务队列，并设置死信交换机
    @Bean
    public Queue businessQueue() {
        return QueueBuilder.durable(BUSINESS_QUEUE)
                // 设置死信交换机
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                // 设置死信路由键
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                // 设置队列消息过期时间（毫秒）
                .withArgument("x-message-ttl", 60*15*1000) // 15分钟过期
                .build();
    }

    // 绑定业务队列到业务交换机
    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue())
                .to(businessExchange())
                .with(BUSINESS_ROUTING_KEY);
    }
}
