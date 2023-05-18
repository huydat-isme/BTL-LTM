var express = require('express');
var app = express();
var http = require('http').createServer(app);
var io = require('socket.io')(http);
var mqtt = require('mqtt');
var client = mqtt.connect('tcp://localhost:1883');

app.use(express.static(__dirname + '/public'));

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
  console.log('A user connected');
});

client.on('connect', function () {
  client.subscribe('Temperature', function (err) {
    if (!err) {
      console.log('MQTT client connected Temperature' );
    }
  });
  client.subscribe('Humidity', function (err) {
    if (!err) {
      console.log('MQTT client connected Humidity' );
    }
  });
  client.subscribe('Pressure', function (err) {
    if (!err) {
      console.log('MQTT client connected Pressure' );
    }
  });
  client.subscribe('Altitude', function (err) {
    if (!err) {
      console.log('MQTT client connected Altitude' );
    }
  });
});

client.on('message', function (topic, message) {
  console.log('Received new message from topic ' + topic + ': ' + message.toString());
  
  if (topic === 'Temperature') {
    io.emit('temperature', message.toString());
  } else if (topic === 'Humidity') {
    io.emit('humidity', message.toString());
  } else if (topic === 'Pressure') {
    io.emit('pressure', message.toString());
  }else if (topic === 'Altitude') {
    io.emit('altitude', message.toString());
  }

});


http.listen(3000, function(){
  console.log('Server is listening on port 3000');
});
