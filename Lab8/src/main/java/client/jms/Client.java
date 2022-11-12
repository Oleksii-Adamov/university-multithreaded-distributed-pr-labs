package client.jms;

import client.UserInteraction;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import comm.StringComm;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final String TO_SERVER_QUEUE_NAME = "ToMapServer";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TO_SERVER_QUEUE_NAME, false, false, false, null);

        String fromServerQueueName = channel.queueDeclare().getQueue();

        // receiving info from server
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msgFromServer = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] fields = StringComm.msgFields(msgFromServer);
            if (StringComm.isValidMsg(fields)) {
                switch (fields[0]) {
                    case "addCountry", "updCountry", "addCity", "updCity", "delCity", "delCountry" ->
                            UserInteraction.printIsSuccess(StringComm.isSuccess(fields));
                    case "getCountry" ->
                        UserInteraction.printEintity(StringComm.countryFromFields(fields));
                    case "getCity" ->
                        UserInteraction.printEintity(StringComm.cityFromFields(fields));
                    case "getCountries" ->
                        UserInteraction.printEntityIterable(StringComm.countriesFromFields(fields));
                    case "getCities" ->
                        UserInteraction.printEntityIterable(StringComm.citiesFromFields(fields));
                    case "getCitiesAndCountryNames" ->
                        UserInteraction.printCitiesAndCountryNames(StringComm.citiesAndCountryNamesFromFields(fields));
                    case "numCitiesInCountry" -> {
                        Integer count = StringComm.intFromFields(fields);
                        if (count == null) {
                            System.out.println("Error");
                        } else {
                            System.out.println(count);
                        }
                    }
                    default -> UserInteraction.printIsSuccess(false);
                }
            }
        };
        channel.basicConsume(fromServerQueueName, true, deliverCallback, consumerTag -> { });

        Scanner scanner = new Scanner(System.in);

        System.out.println("To exit press Ctrl+C");
        UserInteraction.printCommands();

        // sending command to server
        String command = scanner.nextLine();
        while (!Objects.equals(command, "quit")) {
            String msgToServer = fromServerQueueName + "#" + command;
            boolean isCommandValid = true;
            switch (command) {
                case "addCountry", "updCountry" ->
                        msgToServer += "#" + StringComm.countryToMsg(UserInteraction.queryCountryFromUser(scanner));
                case "addCity", "updCity" ->
                        msgToServer += "#" + StringComm.cityToMsg(UserInteraction.queryCityFromUser(scanner));
                case "delCity", "delCountry", "numCitiesInCountry", "getCities", "getCity", "getCountry" ->
                        msgToServer += "#" + UserInteraction.queryCodeFromUser(scanner);
                case "getCountries", "getCitiesAndCountryNames" -> {
                    // do nothing
                }
                default -> {
                    isCommandValid = false;
                    System.out.println("Wrong command!");
                }
            }
            if (isCommandValid) {
                channel.basicPublish("", TO_SERVER_QUEUE_NAME, null, msgToServer.getBytes());
            }
            command = scanner.nextLine();
        }
    }
}
