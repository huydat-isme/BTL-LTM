package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.ConcurrentHashMap;


public class SensorCount {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "count-er";
        //St/ring topic = "sensors";

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            ConcurrentHashMap<String, Integer> topics = new ConcurrentHashMap<>();

            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    try {
                        // Subscribe to all topics
                        mqttClient.subscribe("#", 0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Add the topic to the ConcurrentHashMap or increment its count
                    topics.compute(topic, (key, count) -> (count == null) ? 1 : count + 1);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            mqttClient.connect(connectOptions);

            // Wait for some time to allow the topic messages to be received
            Thread.sleep(5000);

            // Print the number of unique topics received
            int topicCount = topics.size();
            System.out.println("Number of topics published: " + topicCount);

            mqttClient.disconnect();
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}