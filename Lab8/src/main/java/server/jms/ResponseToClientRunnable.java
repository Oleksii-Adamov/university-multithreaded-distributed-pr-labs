package server.jms;

import com.rabbitmq.client.Channel;
import comm.StringComm;
import database.MapDAO;
import server.ServerActionFromFields;

import java.io.IOException;

public class ResponseToClientRunnable implements Runnable {

    private String msgFromClient;
    private Channel channel;

    private MapDAO map;

    public ResponseToClientRunnable(Channel channel, MapDAO map, String msgFromClient) {
        this.channel = channel;
        this.msgFromClient = msgFromClient;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            String[] fields = StringComm.msgFields(msgFromClient);
            if (StringComm.isValidMsg(fields)) {
                String clientQueueName = fields[0];
                String msgToClient = ServerActionFromFields.actAndGetRespond(map, fields, fields[1] + "#");
                channel.basicPublish("", clientQueueName, null, msgToClient.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
