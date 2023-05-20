# Lập trình mạng - MQTT
---
## Thông tin chung
1. Nhóm: 10 
2. Thành viên + nhiệm vụ:
    * Nguyễn Đức Nam:
    * Đỗ Minh Thái:
    * Trần Đăng Quang:
    * Trần Huy Đạt:
    * Nguyễn Tùng Dương:
3. Đề tài:  
Xây dựng chương trình thực hiện giao thức MQTT cho cảm biến không dây
4. Môi trường chạy:
    * Hệ điều hành: Linux
    * Ngôn ngữ lập trình: Java

## [MQTT](https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html)
1. Chung:
    * MQTT (Message Queuing Telemetry Transport) là giao thức truyền thông điệp (message)
    * Client-Server
    * Mô hình: Publish/Subscribe
    * Sử dụng giao thức TCP để truyền tin 
2. Thông điệp ứng dụng (Application Message):
    * Dữ liệu Payload 
    * Mức chất lượng dịch vụ (QoS)
    * Một tập hợp các thuộc tính (Properties) và tên chủ đề (Topic Name)
3. Publisher:
    * Mở Kết nối mạng đến Server
    * Xuất bản (Publish) Các thông điệp ứng dụng (Application Mesage) mà các Client khác có thể quan tâm
    * Đăng ký yêu cầu các Thông điệp ứng dụng mà nó quan tâm để nhận
    * Hủy đăng ký để loại bỏ yêu cầu Thông điệp ứng dụng
    * Đóng Kết nối mạng đến Server
4. Subscriber:
    * Chấp nhận Kết nối mạng từ Publisher.
    * Chấp nhận Các thông điệp ứng dụng được xuất bản bởi Publish Clients
    * Xử lý yêu cầu Đăng ký và Hủy đăng ký từ Publish Clients
    * Chuyển tiếp Các thông điệp ứng dụng phù hợp với Đăng ký của Publish Client
    * Đóng Kết nối mạng từ Publish Client
5. Gói tin điều khiển:
    * Được gửi thông qua Kết nối mạng
    * 14 gói tin điều khiển:   
    CONNECT | CONNACK | PUBLISH | PUBACK | PUBREC | PUBREL | PUBCOMP | SUBSCRIBE | SUBACK | UNSUBSCRIBE | UNSUBACK | PINGREQ | PINGRESP | DISCONNECT
    
## CODE

### **MQTT publisher client**

Trong class file _**publisher.java**_: 
File chính của cả chương trình, dùng để tạo các node cảm biến (sensor node) kết nối với MQTT broker. Các bước thực hiện dưới đây:

#### **Step 1:** 

_**Hàm khởi tạo Publisher tạo 1 đối tượng sensor node lưu các thông tin: IP address của MQTT broker, ID người dùng, topic mà cảm biến muốn publish đến**_

```XML
    public Publisher(String broker, String clientId, String topic) throws MqttException {
        this.mqttClient = new MqttClient(broker, clientId);
        this.topic = topic;
        this.options = new MqttConnectOptions();
        options.setCleanSession(true);
        mqttClient.connect(options);
    }
```
_**Ở đây, chúng ta có 4 topic chính:**_
- temperature 
- humidity
- pressure
- altitude
- Ngoài ra còn có topic phụ để tính toán độ trễ trung bình(latency) và đếm số lượng các node cảm biến lớn nhất

 4 topic chính sẽ có khoảng 100 node cảm biến/ topic
 
 #### **Step 2:** 
 
 _**Với mỗi sensor node được tạo ra, ta sẽ gọi các class sinh dữ liệu ngẫu nhiên, tương ứng với mỗi topic:**_
 
 - Topic Temperature:
 
 ```XML
public class TemperatureGenerator {
    public double TemperatureGenerator () {
        Faker faker = new Faker();
        double offset = 5.0; // giá trị offset

                double temperature = faker.random().nextInt(100) + offset; // Generate temperature from 0 to 100
                System.out.println("temperature generated successfully");

        return temperature;

    }
}
```

- Topic Humidity:

```XML
public class HumidityGenerator {

    public double HumidityGenerator () {
        Faker faker = new Faker();
        double offset = 6.0; // giá trị offset

        double humididty = faker.random().nextInt(200) + offset; // Generate temperature from 0 to 100
        System.out.println("humidity generated successfully");

        return humididty;

    }
}
```

- Topic Pressure:

```XML
public class PressureGenerator {

    public double PressureGenerator () {
        Faker faker = new Faker();
        double offset = 10.0; // giá trị offset

        double pressure = faker.random().nextInt(1000) + offset; // Generate temperature from 0 to 100
        System.out.println("pressure generated successfully");

        return pressure;

    }
}
```

- Topic Altitude:

```XML
public class AltitudeGenerator {

    public double AltitudeGenerator () {
        Faker faker = new Faker();
        double offset = 7.0; // giá trị offset

        double altitude = faker.random().nextInt(2000) + offset; // Generate temperature from 0 to 100
        System.out.println("altitude generated successfully");

        return altitude;

    }
}
```

 ##### _**Sau khi đã thiết lập và sinh dữ liệu ngẫu nhiên ở các sensor node, cụ thể tham khảo code cho topic _**temperature**_ dưới đây:**_
 
 ```XML
                    String topic1 = "Temperature";
                    for (int i = 0; i < 100; i++) { // 100 sensors node for each topic, like 100 different regions ^^
                        Publisher publisher = new Publisher(broker, clientId + " - temperature " + Integer.toString(i), topic1);
                        System.out.println("message published");
                        //receive random data
                        TemperatureGenerator temp = new TemperatureGenerator();
                        String message = Double.toString(temp.TemperatureGenerator());
```

##### _**Ta sẽ tính độ trễ thời gian (delay time) ứng với mỗi gói tin được gửi đến MQTT Broker: và đếm tổng số node cảm biến tương ứng tại mỗi topic **_

```XML
                        //time generation delay
                        delayGeneration dl = new delayGeneration(broker, clientId);

                        //Count the number of temperature node
                        countNumNode++;
```

###### Hàm delayGeneration:

```XML
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
```

##### _**Publish đến mqtt broker:**_

```XML
 publisher.publish(message);
 Thread.sleep(2000);
```

##### _**Cuối cùng là tính tổng số node cảm biến tối đa rồi gửi đến broker:**_
- Hàm _**SensorCount:**_

```XML
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
```

#### _**Step 3:**_

- _**Phần giao diện:**_

##### _**Server.js:**_ Sử dụng các module của NodeJs như Express, socket.io và MQTT để tạo 1 server cho web và kết nối đến broker MQTT
 
 _**Giải thích**_
###### Các module, biến và hàm quan trọng: 

- express: Module Express để xây dựng ứng dụng web.
- app: Một đối tượng Express để tạo và cấu hình ứng dụng web.
- http: Module HTTP được sử dụng để tạo một máy chủ HTTP từ ứng dụng Express.
- io: Socket.io được sử dụng để thiết lập và quản lý kết nối WebSocket.
- mqtt: MQTT là một giao thức truyền thông nhị phân nhẹ cho các thiết bị IoT.
- client: MQTT client object để kết nối đến broker MQTT.
- client.on('connect', function(){...}) : xử lý sự kiện khi client MQTT kết nối thành công với broker MQTT.
Sau khi kết nối thành công, client đăng ký theo dõi các topic như 'Temperature', 'Humidity', 'Pressure', 'Altitude', 
'latency', và 'NodeEstimation'
- client.on('message', function(topic, message){...}) :ử lý sự kiện khi client nhận được một tin nhắn từ broker MQTT trên một trong
 các topic đã đăng ký. Tin nhắn và topic tương ứng được in ra console. Nếu topic là 'Temperature', 'Humidity', 'Pressure', 'Altitude', 
'latency' hoặc 'NodeEstimation', tin nhắn được gửi đến tất cả các kết nối WebSocket đã thiết lập thông qua Socket.io.
 
###### _**Cách hoạt động:**_

- Server được khởi động và lắng nghe trên cổng 3000. Khi máy chủ được khởi động thành công, một thông báo "Server is listening on port 3000" sẽ được in ra console.Sau đó  ứng dụng web sẽ lắng nghe các tin nhắn MQTT từ các topic 'Temperature', 'Humidity', 'Pressure', 'Altitude', 'latency', và 'NodeEstimation', và sau đó gửi chúng đến tất cả các kết nối WebSocket đã thiết lập. Điều này cho phép dữ liệu từ các thiết bị IoT hoặc bất kỳ nguồn dữ liệu MQTT nào được truyền đến ứng dụng web và hiển thị trực tiếp trên giao diện người dùng thông qua kết nối WebSocket.

##### _**Index.html:**_
Tạo ra một trang web có chứa các biểu đồ dữ liệu từ dữ liệu cảm biến. Nó sử dụng thư viện Chart.js để tạo và cập nhật các biểu đồ dựa trên dữ liệu được nhận từ máy chủ thông qua kết nối WebSocket.

###### _**Mỗi biểu đồ được đặt trong một phần tử div có lớp "chart-container" để xác định kích thước và vị trí của biểu đồ.  Mỗi phần tử div biểu diễn một hàng chứa hai biểu đồ, với cấu trúc dạng lưới 2x2. rong phần JavaScript, đầu tiên chúng ta khởi tạo một kết nối WebSocket bằng cách sử dụng thư viện Socket.IO và lưu trữ dữ liệu biểu đồ trong đối tượng "chartData". Đối tượng này chứa các mảng "labels" và "datasets" cho từng loại dữ liệu (nhiệt độ, độ ẩm, áp suất, độ cao).Các mảng này sẽ được cập nhật khi dữ liệu nhận được từ máy chủ.**_

 ###### _**Một số sự kiện được lắng nghe từ kết nối WebSocket để nhận dữ liệu từ máy chủ. Khi dữ liệu nhiệt độ, độ ẩm, áp suất hoặc độ cao được nhận, các mảng "labels" và "datasets" tương ứng trong "chartData" được cập nhật với dữ liệu mới và màu sắc được xác định cho các cột dựa trên giá trị của dữ liệu. Nếu số lượng cột vượt quá giới hạn (được định nghĩa bởi biến "maxColumns"), một biểu đồ mới sẽ được tạo và được thêm vào trang để hiển thị dữ liệu tiếp theo.**_

 ###### _**Cuối cùng, biểu đồ được cập nhật thông qua phương thức "update()" của đối tượng biểu đồ. Điều này sẽ làm cho biểu đồ được cập nhật trực tiếp trên giao diện người dùng mỗi khi dữ liệu mới được nhận.**_
