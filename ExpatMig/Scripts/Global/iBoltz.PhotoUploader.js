(function ($) {
    //Pon: reference to create a new plugin => https://learn.jquery.com/plugins/basic-plugin-creation/

    $.fn.AttachImage = function (options) {

        try {
            // This is the easiest way to have default options.
            var settings = $.extend({
                OnPhotoUploaded: function () { alert('No photo uploaded call back defined!'); }
            }, options);

            var DropBox = this;
            var fluPhoto = this.find('#fluPhoto');
            PhotoUploadManager.InitUploadTool(DropBox);

            this.find('#imgPhoto').click(function () {
                fluPhoto.click();
            });



            fluPhoto.on('change', function () {
                PhotoUploadManager.UploadSinglePhoto(fluPhoto)
            });
            PhotoUploadManager.OnPhotoUploaded = settings.OnPhotoUploaded;
            return this;
        }
        catch (ex) {

            Loghelper.HandleException("GlowAnimate", ex)

        }
    };

}(jQuery));


var PhotoUploadManager = {
    OnPhotoUploaded: function () { },
    InitUploadTool: function (DropBox) {
        try {

            if (!Modernizr.draganddrop) {
                //  alert("This browser doesn't support File API and Drag & Drop features of HTML5!");
                return;
            }
            var fluPhoto = DropBox.find('#fluPhoto');
            DropBox[0].addEventListener("dragenter", OnDragEnter, false);
            DropBox[0].addEventListener("dragover", OnDragOver, false);
            DropBox[0].addEventListener("drop", OnDrop, false);

            DropBox.find("#filImage").click(function () {
                UploadPhotos();
            });
            function OnDragEnter(e) {
                e.stopPropagation();
                e.preventDefault();
            }
            function OnDragOver(e) {
                e.stopPropagation();
                e.preventDefault();
            }
            function OnDrop(e) {
                e.stopPropagation();
                e.preventDefault();
                var selectedFiles = e.dataTransfer.files;
             
                //$("#DropBox").text(selectedFiles.length + " file(s) selected for uploading!");
                PhotoUploadManager.UploadPhotos(selectedFiles, fluPhoto);
            }
        }
        catch (ex) {
            Loghelper.HandleException("InitUploadTool", ex.message)
        }

    },
    UploadSinglePhoto: function (FileUploaderClientID) {
        try {

            var selectedFiles = [];
            var fileInput = FileUploaderClientID;// $('#' + FileUploaderClientID);

            var fileData = fileInput.prop("files")[0];   // Gettingfiles

            selectedFiles.push(fileData);
            PhotoUploadManager.UploadPhotos(selectedFiles, fileInput);
        }
        catch (ex) {
            Loghelper.HandleException("UploadSinglePhoto", ex.message);
        }
    },
    UploadPhotos: function (selectedFiles, fileInput) {
        try {

            var data = new FormData();
            for (var i = 0; i < selectedFiles.length; i++) {
                if (selectedFiles[i] != null) {
                    data.append(selectedFiles[i].name, selectedFiles[i]);
                }
            }
            $.ajax({
                type: "POST",
                url: "/utils/MultipleImageHandler.ashx",
                contentType: false,
                processData: false,
                data: data,
                xhr: function () {
                    var myXhr = $.ajaxSettings.xhr();
                    if (myXhr.upload) {
                        myXhr.upload.addEventListener('progress', PhotoUploadManager.DoProgress, false);
                    }
                    return myXhr;
                },
                success: function (result) {


                    var ParentDiv = fileInput.parent().parent();
                    ParentDiv.find("#hidUploadedFiles").val(result);
                    ParentDiv.find('#btnGetUploadedFiles').trigger('click');


                    ParentDiv.find('#imgPhoto').html("<img style='margin:15%;border-radius:15px;' src='/utils/photohandler.ashx?Width=150&frompath=" + result + "'/>")
                    //$("#DropBox .PhotoUpload").html("Files Uploaded Successfully!<br />" + result); 
                    PhotoUploadManager.OnPhotoUploaded(result);
                },
                error: function (e) {
                    console.log("error while uploading", e);
                    $("#DropBox").text("There was error uploading files!");
                    Loghelper.HandleException(e);
                    //  $("#DropBox").text("There was error uploading files!" + e.responseText);
                }
            });
        }
        catch (ex) {
            Loghelper.HandleException("UploadPhotos", ex.message);

        }
    },
    DoProgress: function (e) {
        try {
            if (e.lengthComputable) {
                var max = e.total;
                var current = e.loaded;

                var Percentage = current / max;
                //console.log(Percentage);
                var TotalWidth = $('.MultiplePhotoUpload .ProgressBar').width()
                var CurrentWidth = TotalWidth * Percentage;
                $('.MultiplePhotoUpload .Progress').width(CurrentWidth);


                if (Percentage >= 100) {
                    // process completed  
                    $('.MultiplePhotoUpload .Progress').width(0);
                }
            }
        }
        catch (ex) {
            Loghelper.HandleException("DoProgress", ex.message);
        }
    }
};
