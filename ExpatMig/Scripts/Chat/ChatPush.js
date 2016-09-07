var RegisteredUserDeviceID = -1;

if ('serviceWorker' in navigator) {
    console.log('Service Worker is supported');
    navigator.serviceWorker.register('../PushServiceWorker.js').then(function (reg) {
        console.log(':^)', reg);
        navigator.serviceWorker.ready.then(function (reg) {

            reg.pushManager.subscribe({
                userVisibleOnly: true
            }).then(function (sub) {
                var DeviceID = sub.endpoint.replace("https://android.googleapis.com/gcm/send/", "");
                //call the register API here
                console.log('DeviceID registerd in iBoltz System:', DeviceID);

                RegisterForPushInServer(DeviceID)

            });
        });
    }).catch(function (err) {
        console.log(':^(', err);
    });


}

function RegisterForPushInServer(DeviceID) {
    var ThisDevice = {
        "UserID": CurrentUserID,
        "DeviceTypeID": 2,//means chrome browser
        "ApiRegistrationID": DeviceID,
        "IsActive": true, "SeqNo": 1, "CreatedBy": CurrentUserID
    };

    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/api/userdevices",
        data: ThisDevice,
        success: function (data) {
            console.log('userdeviceid',data);
            RegisteredUserDeviceID = data.UserDeviceID;
           // alert('RegisteredUserDeviceID ' + RegisteredUserDeviceID);
            SetCookie('RegisteredUserDeviceID', RegisteredUserDeviceID, 30);
        },
        error: function (error) {
            jsonValue = jQuery.parseJSON(error.responseText);
            //jError('An error has occurred while saving the new part source: ' + jsonValue, { TimeShown: 3000 });
        }
    });
}


function send_message_to_sw(msg) {
    navigator.serviceWorker.controller.postMessage("Client 1 says '" + msg + "'");
}

if ('serviceWorker' in navigator) {
    // Handler for messages coming from the service worker
    navigator.serviceWorker.addEventListener('message', function (event) {
        try {
            console.log("Client 1 Received Message: " + event.data);
            angular.element(document.getElementById('divChatController')).scope().GetLatest();


            event.ports[0].postMessage("Client 1 Says 'Hello back!'");
        }
        catch (ex) {
            console.log('Errror accessing angular', ex);
        };

    });
}

