import org.eclipse.paho.client.mqttv3.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Publisher {
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private String topic;

    public Publisher(String broker, String clientId, String topic) throws MqttException{
        this.mqttClient = new MqttClient(broker, clientId);
        this.topic = topic;
        this.options = new MqttConnectOptions();
        options.setCleanSession(true);
        mqttClient.connect(options);
    }

    public void publish(String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(false);
        mqttClient.publish(topic,mqttMessage);
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "publisher";
        String topic = "sensors";

        try {
            Publisher publisher = new Publisher(broker, clientId, topic);
            System.out.println("message published");
            File file = new File("temperature.txt");
            ArrayList<String> dataList = new ArrayList<String>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                dataList.add(line);
            }
            scanner.close();
            int i = 0;
            for (String data: dataList) {
                i++;
                String message =  data;
                publisher.publish(message);
                Thread.sleep(2000);
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
