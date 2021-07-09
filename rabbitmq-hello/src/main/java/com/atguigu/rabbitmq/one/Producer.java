package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发消息
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP连接RabbitMQ的队列
        factory.setHost("192.168.200.136");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         *生成一个队列
         * 参数1：队列名称
         * 参数2：队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
         * 参数3：该队列是否只提供一个消费者进行消费，是否进行消息共享，true：可以多个消费者消费 false：只能一个消费者消费
         * 参数4：是否自动删除 最后一个消费者断开连接以后，该队列是否自动删除 true自动删除 false不自动删除
         * 参数5：其他参数
         */
        Map<String, Object> arguments = new HashMap<>();
        //官方允许是0-255之间 此处设置10 允许优先级范围为0-10 不要设置过大 浪费CPU内存
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
        //发消息
        //String message = "hello world";//初次使用

        /**
         * 发送一个消息
         * 参数1：发送到哪个交换机
         * 参数2：路由的key值是哪个 本次是队列的名称
         * 参数3：其他参数信息
         * 参数4：发送消息的消息体
         */
        //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");

    }
}
