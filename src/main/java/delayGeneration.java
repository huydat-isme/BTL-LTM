


import org.eclipse.paho.client.mqttv3.MqttException;

public class delayGeneration {
    public  delayGeneration(String broker, String clientId) throws MqttException {
        long timeStamp = System.currentTimeMillis();

        //By capturing the timestamp at the point of message generation and comparing it to the current timestamp
        // at the point of message reception, you can calculate delay time(latency)
        //Publish time generation to broker through topic time
        Publisher latencyPublish = new Publisher(broker, clientId + " timeGeneration ","latency");
        System.out.println("message latency published");
        latencyPublish.publish(String.valueOf(timeStamp));
    }
}
