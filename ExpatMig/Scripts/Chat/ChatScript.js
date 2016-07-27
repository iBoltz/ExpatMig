$(document).ready(function () {

    $("#btnTester").click(function (x) {
        ScrollToLastMessage()
    });
});



function ScrollToLastMessage()
{
    var LastItem= $('#ChatHistory .row').last();
    $('#ChatHistory').animate({
        scrollTop:LastItem.offset().top + LastItem.height()
    }, 2000);
}