var IsDebugMode = false;

jQuery.expr[':'].ContainsCaseInsensitive = function (a, i, m) { return jQuery(a).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0; };
jQuery.expr[':'].Equals = function (a, i, m) { return (jQuery(a).text().toUpperCase() == m[3].toUpperCase()); };
jQuery.fn.center = function () { this.css("position", "absolute"); this.css("top", ($(window).height() - this.height()) / 2 + $(window).scrollTop() + "px"); this.css("left", ($(window).width() - this.width()) / 2 + $(window).scrollLeft() + "px"); return this; }
jQuery.fn.CenterInsideAnother = function (Parent) { this.css("position", "absolute"); this.css("top", (Parent.height() - this.height()) / 2 + Parent.scrollTop() + Parent.offset().top + "px"); this.css("left", (Parent.width() - this.width()) / 2 + Parent.scrollLeft() + Parent.offset().left + "px"); return this; }
jQuery.fn.hasScrollBar = function () {
    return this.get(0) ? this.get(0).scrollHeight > this.innerHeight() : false;
};


jQuery.expr[':'].ContainsWordByWord = function (SearcheableText, i, m) {
    var TextData = jQuery(SearcheableText).text().toUpperCase();
    var Keywords = m[3].toUpperCase();
    var ResultIndex = 0;
    var Splitted = Keywords.split(' ')
    for (ItemIndex in Splitted) {
        if (TextData.indexOf(Splitted[ItemIndex]) > 0 || Splitted[ItemIndex] == '') ResultIndex += 1;

    }


    return ResultIndex == Splitted.length;
};

//************************************* array query extensions  ******************************************************/
/*usage
var maxWidth = $("a").max(function () { return $(this).width(); });
var minWidth = $("a").min(function () { return $(this).width(); });
*/

$.fn.max = function (selector) {
    return Math.max.apply(null, this.map(function (index, el) { return selector.apply(el); }).get());
}

$.fn.min = function (selector) {
    return Math.min.apply(null, this.map(function (index, el) { return selector.apply(el); }).get());
}



/***
var ids = this.fruits.select(function(v){
    return v.Id;
});
***/
Array.prototype.select = function (expr) {
    var arr = this;
    //do custom stuff
    return arr.map(expr); //or $.map(expr);
};

/*

var persons = [{ name: 'foo', age: 1 }, { name: 'bar', age: 2 }];

// returns an array with one element:
var result1 = persons.where({ age: 1, name: 'foo' });

// returns the first matching item in the array, or null if no match
var result2 = persons.firstOrDefault({ age: 1, name: 'foo' }); 


*/
Array.prototype.where = function (filter) {

    var collection = this;

    switch (typeof filter) {

        case 'function':
            return $.grep(collection, filter);

        case 'object':
            for (var prop in filter) {
                if (!filter.hasOwnProperty(prop))
                    continue; // ignore inherited properties

                collection = $.grep(collection, function (item) {
                    return item[prop] === filter[prop];
                });
            }
            return collection.slice(0); // copy the array 
            // (in case of empty object filter)

        default:
            throw new TypeError('func must be either a' +
                'function or an object of properties and values to filter by');
    }
};


Array.prototype.firstOrDefault = function (func) {
    return this.where(func)[0] || null;
};

/****************************************          Ensure Loghelper loaded         *******************************************/

$(document).ready(function () {
    if (typeof IsLogHelperLoaded == 'undefined' || !IsLogHelperLoaded) {
        self.alert('Log Not Found PLease let administrator know about this issue!');
    }


});

function CheckIllegalChars(sender, args) {
    var temp = $('#' + sender.controltovalidate).val().trim();

    var Found = temp.match(/^(?!(.|\n)*<[a-z!\/?])(?!(.|\n)*&#)(.|\n)*$/i);



    args.IsValid = (Found != null);

}

function RemovePx(strVal) {
    strVal = LCase(strVal);
    return (parseInt(strVal.replace('px', '')))
}

function CheckDate(source, args) {
    var strFormat = "dd-mmm-yyyy";
    var CONtrOL = document.getElementById(source.controltovalidate);

    if (isDate(CONtrOL.value)) {
        CONtrOL.value = FormatDate(CONtrOL.value, strFormat);
        return args.IsValid = true;
    }
    else {
        return args.IsValid = false;
    }

}
function CheckTime(source, args) {
    var strFormat = "hh:mm";
    var CONtrOL = document.getElementById(source.controltovalidate);

    if (isTime(CONtrOL.value)) {
        CONtrOL.value = FormatTime(CONtrOL.value, strFormat);
        return args.IsValid = true;
    }
    else {
        return args.IsValid = false;
    }

}

function isTime(DateToCheck) {
    if (DateToCheck == "") { return true; }
    var m_strDate = FormatTime(DateToCheck);
    if (m_strDate == "") {
        return false;
    }
    var m_arrDate = m_strDate.split(":");
    var m_Hour = m_arrDate[0];
    var m_Minutes = m_arrDate[1];

    if (m_Hour > 24) { return false; }
    if (m_Minutes > 59) { return false; }

    return true;
}

function FormatTime(DateToFormat, FormatAs) {
    if (DateToFormat == "") { return ""; }
    if (!FormatAs) { FormatAs = "hh:mm"; }
    var strReturnDate;
    var strHour;
    var strMins;
    var Separator;
    if (DateToFormat.indexOf(".") > -1) {
        Separator = ".";
    }

    if (DateToFormat.indexOf(":") > -1) {
        Separator = ":";
    }

    if (DateToFormat.indexOf("-") > -1) {
        Separator = "-";
    }
    var arrDate;
    arrDate = DateToFormat.split(Separator);
    if (arrDate.length > 2) {
        return "";
    }
    strhour = arrDate[0];
    strMins = arrDate[1];

    switch (FormatAs) {
        case "hh:mm":
            return strhour + ":" + strMins;

    }

}

function isDate(DateToCheck) {
    if (DateToCheck == "") { return true; }
    var m_strDate = FormatDate(DateToCheck);
    if (m_strDate == "") {
        return false;
    }
    var m_arrDate = m_strDate.split("/");
    var m_DAY = m_arrDate[0];
    var m_MONTH = m_arrDate[1];
    var m_YEAR = m_arrDate[2];
    if (m_YEAR.length > 4) { return false; }
    m_strDate = m_MONTH + "/" + m_DAY + "/" + m_YEAR;
    var testDate = new Date(m_strDate);
    if (testDate.getMonth() + 1 == m_MONTH) {
        return true;
    }
    else {
        return false;
    }
} //end function




function FormatDate(DateToFormat, FormatAs) {
    if (DateToFormat == "") { return ""; }
    if (!FormatAs) { FormatAs = "dd/mm/yyyy"; }

    var strReturnDate;
    FormatAs = FormatAs.toLowerCase();
    DateToFormat = DateToFormat.toLowerCase();
    var arrDate
    var arrMonths = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    var strMONTH;
    var Separator;

    while (DateToFormat.indexOf("st") > -1) {
        DateToFormat = DateToFormat.replace("st", "");
    }

    while (DateToFormat.indexOf("nd") > -1) {
        DateToFormat = DateToFormat.replace("nd", "");
    }

    while (DateToFormat.indexOf("rd") > -1) {
        DateToFormat = DateToFormat.replace("rd", "");
    }

    while (DateToFormat.indexOf("th") > -1) {
        DateToFormat = DateToFormat.replace("th", "");
    }

    if (DateToFormat.indexOf(".") > -1) {
        Separator = ".";
    }

    if (DateToFormat.indexOf("-") > -1) {
        Separator = "-";
    }


    if (DateToFormat.indexOf("/") > -1) {
        Separator = "/";
    }

    if (DateToFormat.indexOf(" ") > -1) {
        Separator = " ";
    }

    arrDate = DateToFormat.split(Separator);
    DateToFormat = "";
    for (var iSD = 0; iSD < arrDate.length; iSD++) {
        if (arrDate[iSD] != "") {
            DateToFormat += arrDate[iSD] + Separator;
        }
    }
    DateToFormat = DateToFormat.substring(0, DateToFormat.length - 1);
    arrDate = DateToFormat.split(Separator);

    if (arrDate.length < 3) {
        return "";
    }

    var DAY = arrDate[0];
    var MONTH = arrDate[1];
    var YEAR = arrDate[2];




    if (parseFloat(arrDate[1]) > 12) {
        DAY = arrDate[1];
        MONTH = arrDate[0];
    }

    if (parseFloat(DAY) && DAY.toString().length == 4) {
        YEAR = arrDate[0];
        DAY = arrDate[2];
        MONTH = arrDate[1];
    }


    for (var iSD = 0; iSD < arrMonths.length; iSD++) {
        var ShortMonth = arrMonths[iSD].substring(0, 3).toLowerCase();
        var MonthPosition = DateToFormat.indexOf(ShortMonth);
        if (MonthPosition > -1) {
            MONTH = iSD + 1;
            if (MonthPosition == 0) {
                DAY = arrDate[1];
                YEAR = arrDate[2];
            }
            break;
        }
    }

    var strTemp = YEAR.toString();
    if (strTemp.length == 1) {
        YEAR = "0" + YEAR;

    }
    strTemp = YEAR.toString();
    if (strTemp.length == 2) {

        if (parseFloat(YEAR) > 40) {
            YEAR = "19" + YEAR;
        }
        else {
            YEAR = "20" + YEAR;
        }

    }
    if (YEAR < 1900 || YEAR > 2900) {
        return '';
    }
    if (strTemp.length == 3) {
        return '';

    }

    if (parseInt(MONTH) < 10 && MONTH.toString().length < 2) {
        MONTH = "0" + MONTH;
    }
    if (parseInt(DAY) < 10 && DAY.toString().length < 2) {
        DAY = "0" + DAY;
    }
    switch (FormatAs) {
        case "dd/mm/yyyy":
            return DAY + "/" + MONTH + "/" + YEAR;
        case "mm/dd/yyyy":
            return MONTH + "/" + DAY + "/" + YEAR;
        case "dd-mmm-yyyy":
            return DAY + "-" + arrMonths[MONTH - 1].substring(0, 3) + "-" + YEAR;
        case "mmm/dd/yyyy":
            return arrMonths[MONTH - 1].substring(0, 3) + " " + DAY + " " + YEAR;
        case "dd/mmmm/yyyy":
            return DAY + " " + arrMonths[MONTH - 1] + " " + YEAR;
        case "mmmm/dd/yyyy":
            return arrMonths[MONTH - 1] + " " + DAY + " " + YEAR;
    }

    if (parseInt(YEAR) < 1900 || parseInt(YEAR) > 2900) {
        return '';
    }

    return DAY + "/" + strMONTH + "/" + YEAR;;

}


function GetUtcDateString(dtIp) {
    var month = new Array(12);
    month[0] = "Jan";
    month[1] = "Feb";
    month[2] = "Mar";
    month[3] = "Apr";
    month[4] = "May";
    month[5] = "Jun";
    month[6] = "Jul";
    month[7] = "Aug";
    month[8] = "Sep";
    month[9] = "Oct";
    month[10] = "Nov";
    month[11] = "Dec";

    var UtcDateString = dtIp.getUTCDate() + '-' + month[dtIp.getUTCMonth()] + '-' + dtIp.getUTCFullYear()
                        + ' ' + dtIp.getUTCHours() + ':' + dtIp.getUTCMinutes() + ':' + dtIp.getUTCSeconds()
                        + '.' + dtIp.getUTCMilliseconds();

    return UtcDateString;
}


function FormatNumber(intNum, intDecimalDigits) {
    if (isNaN(parseInt(intNum))) return "NaN";

    var intTmpNum = intNum;
    var iSign = intNum < 0 ? -1 : 1;

    intTmpNum *= Math.pow(10, intDecimalDigits);
    intTmpNum = Math.round(Math.abs(intTmpNum))
    intTmpNum /= Math.pow(10, intDecimalDigits);
    intTmpNum *= iSign;

    var intTmpNumStr = new String(intTmpNum);

    return intTmpNumStr;
}

function Len(strInput) {
    return strInput.length;
}
function UCase(strInput) {
    return strInput.toUpperCase();
}

function LCase(strInput) {
    return strInput.toLowerCase();
}
function Mid(strInput, intStart, intLength) {
    return strInput.substring(intStart, intLength)
}

function Trim(str, chars) {
    return LTrim(RTrim(str, chars), chars);
}

function LTrim(str, chars) {
    chars = chars || "\\s";
    return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
}

function RTrim(str, chars) {
    chars = chars || "\\s";
    return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
}

function IsCookieEnabled() {
    var cookieEnabled = (navigator.cookieEnabled) ? true : false;

    if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
        document.cookie = "testcookie";
        cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
    }
    return (cookieEnabled);
}


function SetCookie(name, value, days) {

    if (days) {
        var date = new Date();

        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    }
    else var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}

function ReadCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function DeleteCookie(name) {
    createCookie(name, "", -1);
}

function MakeSameSize(SourceDiv, TargetDiv) {
    $(TargetDiv).css("width", $(SourceDiv).width() + 'px');
    $(TargetDiv).css("height", $(SourceDiv).height() + 'px');

    $(TargetDiv).css("top", $(SourceDiv).position().top + 'px');
    $(TargetDiv).css("left", $(SourceDiv).position().left + 'px');
}


/***************************************        app specific utils  **************************************/
function ShowBaloonTip(ItemToHighlight, Message) {
    if (!jQuery().qtip) return;
    try {
        $(ItemToHighlight).qtip({
            content: Message,
            show: {
                ready: true,
                event: false
            },
            hide: {
                delay: 50,
                fixed: true,
                event: "click"
            },
            style: {
                tip: { corner: true, width: 16, height: 12 },
                classes: "ui-tooltip-shadow ui-tooltip-rounded QtipStyle ui-tooltip-red "
            },
            position: {
                at: "right middle",
                my: "left top"
            }
        });


        setTimeout(function () {
            $(ItemToHighlight).qtip("destroy");
        }, 6000);
    }
    catch (ex) {
        alert(ex.description);
    }
}
function ShowBaloonTipDark(ItemToHighlight, Message) {
    $(ItemToHighlight).qtip({
        content: Message,
        show: {
            ready: true,
            event: false
        },
        hide: {
            delay: 400,
            fixed: true,
            event: "click"
        },
        style: {
            tip: { corner: true, width: 16, height: 12 },
            classes: "ui-tooltip-shadow ui-tooltip-rounded QtipStyle ui-tooltip-dark "
        },
        position: {
            at: "right middle",
            my: "left top"
        }
    });



    setTimeout(function () {
        $(ItemToHighlight).qtip("destroy");
    }, 6000);
}


function ShowJqToast(Message) {
    try {
        var Lines = CountMatch(Message, '<br />', 0);
        var TotalHeight = Lines * 100
        if (TotalHeight > 500) TotalHeight = 300;
        if (TotalHeight < 250) TotalHeight = 100;
        var TotalWidth = $(window).width();// 3/4 of the screen
        // if (TotalWidth > 800) TotalWidth = 800;


        if ($('#JqToast').length == 0) {
            $('body').append("<div id='JqToast' class='JqToast text-center' style='width:" + TotalWidth
                + ",height:" + TotalHeight + "'></div>");

            $('#JqToast').append("<div id='ToastText' class='ToastText'>" + Message + "</div>");
        }
        $('.JqToast').width(TotalWidth + 'px');

        $('.JqToast').center(); //this  is the div of the dialog
        $('.JqToast').css('left', 0);//bug in center function
        $('.JqToast').css({
            'display': 'block',
            'z-index': $('div').GetMaxZindex() + 3
        });

        $(".JqToast").animate({
            "opacity": "1"
        }, { queue: false, duration: 1000 }, 'swing', function () { $('.JqToast').center(); });
        $('#ToastText').html(Message);

        setTimeout(function () {

            $(".JqToast").animate({
                "opacity": "0"
            }, 1000, 'swing', function () {
                $(".JqToast").css("display", "none");
            });

        }, 5000);


    }
    catch (ex) {
        alert(ex);
    }
}

function ShowJqMsgBox(Message, Title) {
    try {
        $('.JqMessagBox').css("display", "none");
        if ($('#JqMessagBox').length == 0) {
            $('body').append("<div id='JqMessagBox' class='JqMessagBox'></div>");


            $('#JqMessagBox').append("<div id='MessageText' class='MessageText'>" + Message + "</div>");
            $('#JqMessagBox').append("<div id='MessageControlBar' class='MessageControlBar'>" +
                "<button onclick='CloseJqMsgBox();'>Close</button></div>");


        }
        var Lines = CountMatch(Message, '<br />', 0);
        var TotalHeight = Lines * 50
        if (TotalHeight > 500) TotalHeight = 500;
        if (TotalHeight < 250) TotalHeight = 250;
        var TotalWidth = $(window).width() * 0.75;// 3/4 of the screen
        if (TotalWidth > 800) TotalWidth = 800;
        var LoadingBox = $(".JqMessagBox").dialog({
            width: TotalWidth,
            height: TotalHeight,
            modal: true,
            title: Title
        });
        //    self.alert('ShowNotificationPanel');
        //    return;
        $('.ui-dialog').center(); //this  is the div of the dialog
        $('#MessageText').html(Message);

    }
    catch (ex) {
        alert(ex);
    }
}



function ShowToast(Message) {
    try {
        var Lines = CountMatch(Message, '<br />', 0);
        var TotalHeight = Lines * 100
        if (TotalHeight > 500) TotalHeight = 300;
        if (TotalHeight < 250) TotalHeight = 100;
        var TotalWidth = $(window).width();// 3/4 of the screen
        // if (TotalWidth > 800) TotalWidth = 800;


        if ($('#JqToast').length == 0) {
            $('body').append("<div id='JqToast' class='JqToast text-center' style='width:" + TotalWidth
                + ",height:" + TotalHeight + "'></div>");

            $('#JqToast').append("<div id='ToastText' class='ToastText'>" + Message + "</div>");
        }
        $('.JqToast').width(TotalWidth + 'px');

        $('.JqToast').center(); //this  is the div of the dialog
        $('.JqToast').css('left', 0);//bug in center function
        $('.JqToast').css({
            'display': 'block',
            'z-index': $('div').GetMaxZindex() + 3
        });

        $(".JqToast").animate({
            "opacity": "1"
        }, { queue: false, duration: 1000 }, 'swing', function () { $('.JqToast').center(); });
        $('#ToastText').html(Message);

        setTimeout(function () {

            $(".JqToast").animate({
                "opacity": "0"
            }, 1000, 'swing', function () {
                $(".JqToast").css("display", "none");
            });

        }, 5000);



    }
    catch (ex) {
        alert(ex);
    }
}

function CountMatch(string, subString, allowOverlapping) {

    string += ""; subString += "";
    if (subString.length <= 0) return string.length + 1;

    var n = 0, pos = 0;
    var step = (allowOverlapping) ? (1) : (subString.length);

    while (true) {
        pos = string.indexOf(subString, pos);
        if (pos >= 0) { n++; pos += step; } else break;
    }
    return (n);
}

function CountMatch(string, subString, allowOverlapping) {

    string += ""; subString += "";
    if (subString.length <= 0) return string.length + 1;

    var n = 0, pos = 0;
    var step = (allowOverlapping) ? (1) : (subString.length);

    while (true) {
        pos = string.indexOf(subString, pos);
        if (pos >= 0) { n++; pos += step; } else break;
    }
    return (n);
}
function CloseJqMsgBox() {
    $(".JqMessagBox").dialog('close');

    $('#MessageText').html("");
    $("#MessageControlBar").hide();



}

function ArrayRemove(input, ToRemove) {
    var FoundIndex = input.indexOf(ToRemove);

    if (FoundIndex > -1) {
        input.splice(FoundIndex, 1);
    }
}


/****************************************   Random  *************************************/

function iBoltzRandom(seed) {
    // LCG using GCC's constants
    this.m = 0x80000000; // 2**31;
    this.a = 1103515245;
    this.c = 12345;

    this.state = seed ? seed : Math.floor(Math.random() * (this.m - 1));
}
iBoltzRandom.prototype.nextInt = function () {
    this.state = (this.a * this.state + this.c) % this.m;
    return this.state;
}
iBoltzRandom.prototype.nextFloat = function () {
    // returns in range [0,1]
    return this.nextInt() / (this.m - 1);
}
iBoltzRandom.prototype.nextRange = function (start, end) {
    // returns in range [start, end): including start, excluding end
    // can't modulu nextInt because of weak randomness in lower bits
    var rangeSize = end - start;
    var randomUnder1 = this.nextInt() / this.m;
    return start + Math.floor(randomUnder1 * rangeSize);
}
iBoltzRandom.prototype.choice = function (array) {
    return array[this.nextRange(0, array.length)];
}

function GetAlphaIndex(Digit) {
    var AlphaList = ["a", "b", "c", "d", "e", "f", "g", "h",
        "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z"];
    return AlphaList.findIndex(function (DigitInList) { return DigitInList == Digit.toLowerCase() });
}


/********************************** Current Date ******************************/

function IsDateToday(GivenDate) {

    //Get today's date
    var todaysDate = new Date();

    //call setHours to take the time out of the comparison
    if (new Date(GivenDate).toDateString() == todaysDate.toDateString()) {
        return true;
    }
    return false;
}





$(document).ready(function () {

    $('.Loading').click(function () {

        if ($("#LoadingPanel").css('display') == 'block') {
            $("#txtMessage").HideLoadingPanel();
        } else {
            $("#txtMessage").ShowLoadingPanel();
        }
    });
});
