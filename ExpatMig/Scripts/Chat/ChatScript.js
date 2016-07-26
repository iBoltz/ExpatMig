$(document).ready(function () {

    $("#btnTester").click(function (x) {

        //var Topic = [
        //    { name: 'pon', age: 36 },
        //    { name: 'Shankari', age: 56 },
        //    { name: 'Siva', age: 86 },
        //    { name: 'John', age: 16 },
        //    { name: 'Michael', age: 28 }
        //];

        //var values = $(Topic).max(function () {return this.age });

        angular.element(document.getElementById('divChatController')).scope().GetLatest();


        console.log("max values","")

    });
});