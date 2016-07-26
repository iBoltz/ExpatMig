 

console.log('Started', self);
self.addEventListener('install', function (event) {
    self.skipWaiting();
    console.log('Installed', event);
});
self.addEventListener('activate', function (event) {
    console.log('Activated', event);
});
self.addEventListener('push', function (event) {
    try
    {
        send_message_to_all_clients("Pushed From server");
        console.log('Push message received & posted!', event);
    }
    catch(ex)
    {
        console.log('Errored', ex);
    }
});


self.addEventListener('message', function (event) {
    console.log("SW Received Message: " + event.data);
});

function send_message_to_client(client, msg) {
    return new Promise(function (resolve, reject) {
        var msg_chan = new MessageChannel();

        msg_chan.port1.onmessage = function (event) {
            if (event.data.error) {
                reject(event.data.error);
            } else {
                resolve(event.data);
            }
        };

        client.postMessage("SW Says: '" + msg + "'", [msg_chan.port2]);
    });
}

function send_message_to_all_clients(msg) {
    clients.matchAll().then(clients => {
        clients.forEach(client => {
            send_message_to_client(client, msg).then(m => console.log("SW Received Message: " + m));
        })
    })
}