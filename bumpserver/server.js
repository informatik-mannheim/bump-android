var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;

var sockets = {};

server.listen(port, function () {
  console.log('Server listening at port %d', port);
});

io.sockets.on('connection', function (socket) {
  var loggedIn = false;
  socket.on('logIn', function (data) {
    if (!loggedIn) {
      socket.deviceID = data.deviceID;
      socket.timestamp = data.timestamp;
      sockets[data.deviceID] = socket;
      loggedIn = true;
      socket.emit('loggedIn');
      console.log("Device logged in with timestamp: " + data.timestamp);
    }
    var partner = findPartner(socket);
    if (partner != undefined) {
      console.log("partner is logged in");
      socket.emit('partnerIsLoggedIn');
      socket.partner = partner;
      sockets[partner.deviceID].partner = socket;
      io.sockets.connected[partner.id].emit('partnerIsLoggedIn');
    } else {
      console.log("partner is not logged in");
    }
  });

  socket.on('message', function(message) {
    io.sockets.connected[socket.partner.id].emit('message', message);
    socket.emit('messageDelivered');
  });

  socket.on('image', function(image) {
    io.sockets.connected[socket.partner.id].emit('image', image);
    socket.emit('imageDelivered');
  });

  socket.on('disconnect', function () {
    if (loggedIn) {
      loggedIn = false;
      logOut(socket);
    }
  });

  socket.on('logOut', function() {
    if (loggedIn) {
      loggedIn = false;
      logOut(socket);
    }
  });

});

function logOut(socket) {
  delete sockets[socket.deviceID];
  socket.emit('loggedOut');
  console.log("Device: " + socket.deviceID + " logged out");
}

function findPartner(socket) {
  var partner;
   Object.keys(sockets).forEach(function(obj) {
      var sock = sockets[obj];
      if (socket.id != sock.id) {
         if (match(sock, {timestamp: socket.timestamp})) {
          partner = sock;
         }
      }
  });
   return partner;
}

function match(item, filter) {
  var keys = Object.keys(filter);
  return keys.every(function (key) {
    return Math.abs(item[key] - filter[key]) < 300;
  });
}

Object.size = function(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};


