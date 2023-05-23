import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.ConcurrentHashMap;


public class SensorCount {

    //Client subscrbing to all topics in mqtt broker in order to calculate the max nodes connected
    //Never calculate this "subscriber"!
    String clientId = "count-er";
    String topic = "NodeEstimation";

    public SensorCount(String broker, long countNode) {
        //St/ring topic = "sensors";

        try {
            Publisher counting = new Publisher(broker, clientId, topic);
            System.out.println("calculate the number of max node published");
            counting.publish(String.valueOf(countNode));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}