
import org.eclipse.paho.client.mqttv3.*;



public class Publisher {
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private String topic;

    public Publisher(String broker, String clientId, String topic) throws MqttException {
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
        mqttClient.publish(topic, mqttMessage);
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "publisher"; // client that receives data from (4-max) sensor nodes=
        long countNumNode = 0;// To count the max node
        Publisher publisher;

        try {
            // time generation
            //delayGeneration dl = new delayGeneration(broker, clientId);

            //temperature
            String topic1 = "Temperature";
            for (int i = 0; i < 20; i++) { // 100 sensors node for each topic, like 100 different regions ^^
                publisher = new Publisher(broker, clientId + " - temperature " + Integer.toString(i), topic1);
                System.out.println("message published");
                //receive random data
                TemperatureGenerator temp = new TemperatureGenerator();
                String message = Double.toString(temp.TemperatureGenerator());

                //time generation delay
                delayGeneration dl = new delayGeneration(broker, clientId);

                //Count the number of temperature node
                countNumNode++;

                publisher.publish(message);
                Thread.sleep(2000);
            }

            //   else if (j == 2) {
            String topic2 = "Humidity";
            for (int i = 0; i < 20 ; i++) {
                publisher = new Publisher(broker, clientId + " - humidity " + Integer.toString(i), topic2);
                System.out.println("message published");
                //receive random data
                HumidityGenerator humid = new HumidityGenerator();
                String message = Double.toString(humid.HumidityGenerator());

                //time generation delay
                delayGeneration dl = new delayGeneration(broker, clientId);

                //Count the number of humidity node
                countNumNode++;

                publisher.publish(message);
                Thread.sleep(2000);
            }

            //   else if (j == 3) {
            String topic3 = "Pressure";
            for (int i = 0; i <20; i++) {
                publisher = new Publisher(broker, clientId + " - pressure " + Integer.toString(i), topic3);
                System.out.println("message published");
                //receive random data
                PressureGenerator press = new PressureGenerator();
                String message = Double.toString(press.PressureGenerator());

                //time generation delay
                delayGeneration dl = new delayGeneration(broker, clientId);

                //Count the number of pressure node
                countNumNode++;

                publisher.publish(message);
                Thread.sleep(2000);
            }

            //else if (j == 4) {
            String topic4 = "Altitude";
            for (int i = 0; i < 20; i++) {
                publisher = new Publisher(broker, clientId + " - altitude " + Integer.toString(i), topic4);
                System.out.println("message published");
                //receive random data
                AltitudeGenerator altitude = new AltitudeGenerator();
                String message = Double.toString(altitude.AltitudeGenerator());

                //time generation delay
                delayGeneration dl = new delayGeneration(broker, clientId);

                //Count the number of altitude node
                countNumNode++;

                publisher.publish(message);
                Thread.sleep(2000);
            }

            SensorCount counting = new SensorCount(broker, countNumNode);

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
