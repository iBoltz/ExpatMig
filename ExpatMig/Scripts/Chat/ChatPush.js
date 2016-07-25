if ('serviceWorker' in navigator) {
    console.log('Service Worker is supported');
    navigator.serviceWorker.register('../Scripts/Chat/PushServiceWorker.js').then(function (reg) {
        console.log(':^)', reg);

        reg.pushManager.subscribe({
            userVisibleOnly: true
        }).then(function (sub) {
            var DeviceID = sub.endpoint.replace("https://android.googleapis.com/gcm/send/", "");
            //call the register API here
            console.log('DeviceID:', DeviceID);
           
            RegisterForPushInServer(DeviceID)

        });
    }).catch(function (err) {
        console.log(':^(', err);
    });
}

function RegisterForPushInServer(DeviceID)
{
    var ThisDevice = {
        "UserID": CurrentUserID,
        "ApiRegistrationID": DeviceID,
        "IsActive": true, "SeqNo": 1, "CreatedBy": CurrentUserID
    };

    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/api/userdevices",
        data: ThisDevice,
        success: function (data) {
            
        },
        error: function (error) {
            jsonValue = jQuery.parseJSON(error.responseText);
            //jError('An error has occurred while saving the new part source: ' + jsonValue, { TimeShown: 3000 });
        }
    });
}