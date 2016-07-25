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
        console.log('Push message received', event);
        $("#btnRefresh").trigger("click"); 
    }
    catch(ex)
    {
        console.log('Errored', ex);
    }
    


});
