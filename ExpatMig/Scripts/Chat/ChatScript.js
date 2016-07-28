$(document).ready(function () {

    $("#btnTester").click(function (x) {
        ScrollToLastMessage()
    });
});



function ScrollToLastMessage()
{
    var LastItem = $('#ChatHistory .row').last();
    var scroller = $('#ChatHistory');
    var height = scroller[0].scrollHeight - $('#ChatHistory .row').height();
    ////LastItem.offset().top + LastItem.height()
   
    $('#ChatHistory').animate({
        scrollTop:height
    }, 2000);
}