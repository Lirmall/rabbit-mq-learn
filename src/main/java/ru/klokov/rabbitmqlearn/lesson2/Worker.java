package ru.klokov.rabbitmqlearn.lesson2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // повторяем, чтобы убедиться, что очередь есть
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // callback — что делать, когда пришло сообщение
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        // подписываемся на очередь
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        // consumer живёт, пока не остановишь его вручную
        System.out.println(" [x] Waiting for messages. To exit press CTRL+C");
    }
}
