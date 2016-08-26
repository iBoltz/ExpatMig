$(document).ready(function () {

    //HideLoadingChat();
    RegisterEmoji();
   
});

function RegisterEmoji() {
    $('#txtMessage').emojioneArea({
        hideSource: false,
        placeholder: null,
        container: null,
        events: {
            keydown: function (editor, event) {
                if (event.keyCode == 13) {
                    //var e = jQuery.Event('keydown', { which: $.ui.keyCode.ENTER });
                    //angular.element('#btnSendMessage').scope().SaveChanges();
                    //$('#btnSendMessage').trigger('click');
                    angular.element('#txtMessage').trigger('focus');
                    angular.element('#btnSendMessage').triggerHandler('click');


                }
                //console.log(event);
            }
        }
    });

}

function ScrollToLastMessage() {
    var LastItem = $('#ChatHistory .row').last();
    var scroller = $('#ChatHistory');
    var height = scroller[0].scrollHeight - $('#ChatHistory .row').height();
    ////LastItem.offset().top + LastItem.height()

    $('#ChatHistory').animate({
        scrollTop: height
    }, 200);
}

