/***

Plugin For Show Loading Panel Inline

***/
(function ($) {
    //Pon: reference to create a new plugin => https://learn.jquery.com/plugins/basic-plugin-creation/

    $.fn.ShowLoadingPanel = function (options) {

        try {
            // This is the easiest way to have default options.
            var settings = $.extend({
                OnFinishedCallback: function () { }
            }, options);

            var ParentElement = this;

            if ($("#LoadingPanel").length == 0) {
                $('body').append("<div id='LoadingPanel' class='LoadingPanel'><div class='Image'>&nbsp;</div></div>");
            }//ensure only one loading panel there


            $("#LoadingPanel").css({
                "width": $(this).outerWidth(true),
                "height": $(this).outerHeight(true),
                "top": $(this).offset().top,
                "left": $(this).offset().left,
            })

            setTimeout(function () {
                $(ParentElement).HideLoadingPanel();
            }, 6000);


            $("#LoadingPanel").show('fast');
            return this;
        }
        catch (ex) {

            Loghelper.HandleException("GlowAnimate", ex)

        }
    };

}(jQuery));

(function ($) {
    //Pon: reference to create a new plugin => https://learn.jquery.com/plugins/basic-plugin-creation/
    $.fn.HideLoadingPanel = function (options) {
        try {
            $("#LoadingPanel").hide('fast');
        }
        catch (ex) {
            Loghelper.HandleException('HideLoadingPanel', ex)
        }
    };

}(jQuery));

/***
On enter key

***/
(function ($) {
    $.fn.OnEnterKeyPressed = function (options) {
        try {
            var Param = $.extend({
                // These are the defaults.
                OnEvent: function () { }

            }, options);
            var triggered = false;
            $(this).keydown(function (e) {

                try {
                    triggered = false;
                    var code = (e.keyCode ? e.keyCode : e.which);
                    if (code == 13) {
                        triggered = true;
                        Param.OnEvent();
                        return false;
                    }
                }
                catch (ex) {
                    Loghelper.HandleException("OnEnterEvent", ex)
                }
            });

            $(this).keyup(function (e) {
                try {
                    if (triggered) return;
                    if ((e.keyCode ? e.keyCode : e.which) == 13) {
                        Param.OnEvent();
                    }

                }
                catch (ex) {
                    Loghelper.HandleException("OnEnterEvent", ex)
                }
            });
        }
        catch (ex) {
            Loghelper.HandleException("OnEnterEvent", ex)
        }
    }

}(jQuery));



/***

Plugin For GetMaxIndex

***/
(function ($) {
    //Pon: reference to create a new plugin => https://learn.jquery.com/plugins/basic-plugin-creation/

    $.fn.GetMaxZindex = function (options) {
        try {
            // This is the easiest way to have default options.
            var settings = $.extend({
                // These are the defaults.

            }, options);

            var AllZinxdices = [];
            $(this).each(function () {
                if (!isNaN($(this).css("z-index"))) {
                    AllZinxdices.push($(this).css("z-index"));
                }
            });
            var MaxIndex = Math.max.apply(Math, AllZinxdices);

            // Greenify the collection based on the settings variable.
            return MaxIndex;
        }
        catch (ex) {
            if (typeof IsLogHelperLoaded != 'undefined' && IsLogHelperLoaded) {
                Loghelper.HandleException("GetMaxZindex", ex)
            }
            else {
                console.log(ex.message);
            }
        }
    };

}(jQuery));