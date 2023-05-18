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
  client.subscribe('sensors', function (err) {
    if (!err) {
      console.log('MQTT client connected');
    }
  });
});

client.on('message', function (topic, message) {
  console.log('Received new temperature data: ' + message.toString());
  io.emit('temperature', message.toString());
});

http.listen(3000, function(){
  console.log('Server is listening on port 3000');
});
