package com.example.rabbit.config;



import com.example.rabbit.service.RabbitMqListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {
    public static final String queueName = "clubhouse.image.upload";

    public static final String fanoutExchangeName = "clubhouse.image.upload";

    @Bean
    ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new CachingConnectionFactory();
        return connectionFactory;
    }

    @Bean
    Queue queue() {
        Queue queue = new Queue(queueName, true, true, true);
        return queue;
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Binding binding(FanoutExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // setting listener
    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue());
        simpleMessageListenerContainer.setMessageListener(new RabbitMqListener());
        return simpleMessageListenerContainer;
    }
}
