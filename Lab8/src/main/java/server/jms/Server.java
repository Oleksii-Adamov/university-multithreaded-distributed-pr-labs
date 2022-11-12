package server.jms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import database.MapDAO;

import java.nio.charset.StandardCharsets;

public class Server {

    private static final String TO_SERVER_QUEUE_NAME = "ToMapServer";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TO_SERVER_QUEUE_NAME, false, false, false, null);
        System.out.println("Server started");

        MapDAO map = new MapDAO();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msgFromClient = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Thread thread = new Thread(new ResponseToClientRunnable(channel, map, msgFromClient));
            thread.start();
        };
        channel.basicConsume(TO_SERVER_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
