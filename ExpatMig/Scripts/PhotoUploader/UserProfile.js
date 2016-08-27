var IsPhotoUploaderLoaded = true;
$(document).ready(function () {
    
    if (ProfilePic != undefined)
    {
        $('.MultiplePhotoUpload #imgPhoto').html("<img style='margin:15%;border-radius:15px;' src='/utils/photohandler.ashx?Width=150&frompath=" + ProfilePic + "'/>")

    }
    
    var OnPhotoUploaded = function (ImagePath) {
        var data = {
            "UserID": CurrentUserID,
            "ProfilePic": ImagePath
        }

        $.ajax({
            type: "POST",
            url: "/api/UserProfiles/UpdatePhoto/",
            dataType: "json",
            data: data,
            success: function (result) {
                $('.MultiplePhotoUpload #imgPhoto').html("<img style='margin:15%;border-radius:15px;' src='/utils/photohandler.ashx?Width=150&frompath=" + ImagePath + "'/>")

                console.log("done uploading ", result);
            },
            error: function (e) {
                console.log("error while uploading", e);

            }
        });

    };


    $('.DropBox').AttachImage({
        OnPhotoUploaded:OnPhotoUploaded
    });

});
