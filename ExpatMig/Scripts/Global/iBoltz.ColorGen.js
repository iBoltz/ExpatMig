var iBoltzColorGen = {
    GetMyColor: function (Name, UserID) {
        var GeneratedColor = { Red: 0, Green: 0, Blue: 0, };
        var MyColorNo = new iBoltzRandom(UserID).nextRange(1, 255)


        //trim non alpha and get the first alpha
        var FirstLetter = (Name + "").match(/[a-zA-Z]+/g).toString().substring(0, 1)

        var RgbGroupIndex = (GetAlphaIndex(FirstLetter)) % 3 + 1;//get the alphabet index in a-z and find the modulas to group into three R | G | B
        //1 => R, 2=> G, 3 =>B

        switch (RgbGroupIndex) {
            default:
            case 1:
                GeneratedColor.Red = MyColorNo;
                break;
            case 2:
                GeneratedColor.Green = MyColorNo;
                break;
            case 3:
                GeneratedColor.Blue = MyColorNo;
                break;
        }
        return GeneratedColor;
    }
};