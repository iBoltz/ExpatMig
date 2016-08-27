var IsLogHelperLoaded = true;
var DiagnosisList = [];
var Loghelper = {
    HandleException: function (methodName,exception)
    {
        try
        {
            DiagnosisList.push(methodName + ":" + exception.message + "<hr />" + exception.stack);
            console.log(exception.message + "<hr />" + exception.stack);
          //  ShowJqMsgBox(DiagnosisList.join("<br />", "You wont see this production!"), "Exception!");
        }
        catch (ex)
        {
            console.log(ex.message);
        }
           
    },
    TraceError: function (Exception)
    {
        
    },
    SendReport: function ()
    {
        try{
            
        }
        catch (ex)
        {

        }

}
}