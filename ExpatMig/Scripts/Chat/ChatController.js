(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('ChatController', ['$scope', 'ChatService', '$http', 'BridgeService', function ($scope, ChatService, $http, BridgeService) {
                try {
                    $scope.SelectedGroup="Select Group";
                     $scope.SelectedThreadID=0;
                    
                    $scope.Bindable = ChatService.ListChats.query(function (result) {
                        return result;
                    });
                   

                    $scope.AllGroups = ChatService.ListGroups.query(function (result) {
                        return result;
                    });

                   

                    $scope.getselectgroup = function (SelectedGroup) {
                        $scope.SelectedGroup = SelectedGroup.Description;
                        $scope.AllThreads = ChatService.GetThreads.query({ id: SelectedGroup.GroupID }, function (result) {
                            return result;
                        }); 
                    }

                    $scope.getselectthhread=function(SelectedID){
                        $scope.SelectedThreadID=SelectedID;
                    }

                    $scope.SaveGroup = function () {
                        var GroupToSave = {
                            "Description":$scope.GroupName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607","MyThread": null

                        }
                        ChatService.PostGroup.save(GroupToSave, function () {
                            //alert('Saved!');

                            $scope.AllGroups.push(GroupToSave);
                        })
                    };

                    $scope.SaveThread = function () {
                        var ThreadToSave = {
                            "GroupID":$scope.SelectedGroupID,
                            "Description":$scope.ThreadName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607","MyThread": null

                        }
                        ChatService.PostThread.save(ThreadToSave, function () {
                            //alert('Saved!');

                            $scope.AllThreads.push(ThreadToSave);
                        })
                    };
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
