﻿var IsPhotoUploaderLoaded = true;
$(document).ready(function () {
    
    if (ProfilePic != undefined)
    {
        $('.MultiplePhotoUpload #imgPhoto').html("<img style='margin:15%;border-radius:15px;' src='/utils/photohandler.ashx?Width=150&frompath=" + ProfilePic + "'/>")

    }
    
    PhotoUploadManager.OnPhotoUploaded = function (ImagePath) {
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
                alert(result);
                console.log("done", e);
            },
            error: function (e) {
                console.log("error while uploading", e);

            }
        });

    };

});