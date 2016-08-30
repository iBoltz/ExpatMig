var LastClickedSearchResultTopicID = -1;
$(document).ready(function () {

    //HideLoadingChat();
    RegisterEmoji();


});

function RegisterSearchResultClick() {
    $('.SearchResults li').bind('click', function () {
        //alert('test');
        $('.SearchResults li').removeClass('SelectedSearchResult');

        $(this).addClass('SelectedSearchResult');
        LastClickedSearchResultTopicID = $(this).find('#lblTopicID').text();
        //alert(LastClickedSearchResultTopicID );
    });
}
function RegisterThreadsClick() {
    $('.thread-panel-list .list-item').bind('click', function () {
        $('.thread-panel-list .list-item').removeClass('list-item-selected');
        $(this).addClass('list-item-selected');
    });
}
function xpand(thisitem) {
    $('#pnlEnlargePhoto').on('shown.bs.modal', function () {
        //alert('width ');
        $('#pnlEnlargePhoto .modal-dialog').width($('#pnlEnlargePhoto img').width() + 40);//20px for padding each side

    })
    //$('#divTopic img').bind('click', function () { alert($this) });
    //console.log('registerd img click ' + $(thisitem).attr('src'));
    var imagePath = $(thisitem).attr('src');
    imagePath = imagePath.replace('Width=150&', '');
 

    $('#pnlEnlargePhoto img').attr('src', imagePath);
    $('#pnlEnlargePhoto').modal('show');

}

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

    if ($('.SearchResults li').length > 0) {
        var FoundItem = $('#pnlSearchResult_' + LastClickedSearchResultTopicID);
        FoundItem.addClass('SelectedTopicSearched');
        //alert(FoundItem.length);
        if (FoundItem.length > 0) {
            height = FoundItem.offset().top;
        }
    }

    ////LastItem.offset().top + LastItem.height()

    $('#ChatHistory').animate({
        scrollTop: height
    }, 200);
}

