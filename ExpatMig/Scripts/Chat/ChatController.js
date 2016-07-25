(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('ChatController', ['$scope', 'ChatService', '$http', 'BridgeService', function ($scope, ChatService, $http, BridgeService) {
                try {

                    $scope.Bindable = ChatService.ListChats.query(function (result) {
                        return result;
                    });




                    $scope.SaveChanges = function () {

                        var TopicToSave = {
                            "TopicID": 2,
                            "ThreadID": 1,
                            "Description": $scope.Message, "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607", "MyThread": null
                        }


                        ChatService.PostChat.save(TopicToSave, function () {
                            //alert('Saved!');

                            $scope.Bindable.push(TopicToSave);
                        })
                    };


                    //$scope.Bindable = [{ name: 'Jeni', country: 'Norway' },
                    //                   { name: 'Ram', country: 'Sweden' },
                    //                   { name: 'Raj', country: 'Denmark' }
                    //];

                }
                catch (ex) {
                    //Loghelper.HandleException("ChatController", ex);
                }
            }]);

})();
