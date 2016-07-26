(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('ChatController', ['$scope', 'ChatService', '$http', 'BridgeService', function ($scope, ChatService, $http, BridgeService) {
                try {
                    $scope.SelectedGroup = "Select Group";
                    $scope.SelectedThreadID = 0;

                    $scope.GetLatest = function () {
                        console.log('Fetching data');
                        var MaxID = $($scope.Bindable).max(function () { return this.TopicID });
                        MaxID = isNaN(MaxID) || !isFinite(MaxID) ? 0 : MaxID;

                        ChatService.GetLatest.query({ id: MaxID }, function (result) {
                            $scope.Bindable = $scope.Bindable.concat(result);
                            ScrollToLastMessage();
                            console.log(result);
                            return result;
                        });
                    };
                    $scope.ListChat = function () {
                        $scope.Bindable = ChatService.ListChats.query({ id: 1 }, function (result) {
                            ScrollToLastMessage();
                            return result;

                        });
                    };
                    $scope.AllGroups = ChatService.ListGroups.query(function (result) {
                        return result;
                    });
                    $scope.getselectgroup = function (SelectedGroup) {
                        $scope.SelectedGroup = SelectedGroup.Description;
                        $scope.AllThreads = ChatService.GetThreads.query({ id: SelectedGroup.GroupID }, function (result) {
                            return result;
                        });
                    }
                    $scope.getselectthhread = function (SelectedID) {
                        $scope.SelectedThreadID = SelectedID;
                    }
 
                    /*****************  Save routines   ********************/
                    $scope.OnEnterPress = function (keyEvent) {
                        if (keyEvent.which === 13) $scope.SaveChanges();
                    }

                    $scope.SaveThread = function () {
                        var ThreadToSave = {
                            "GroupID": $scope.SelectedGroupID,
                            "Description": $scope.ThreadName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607", "MyThread": null

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
                            "Description": $scope.Message,
                            "Slug": null, "IsActive": true,
                            "SeqNo": 1, "CreatedBy": CurrentUserID,
                            "CreatedDate": "2016-07-25T12:48:59.607",
                            "ModifiedBy": 1,
                            "ModifiedDate": "2016-07-25T12:48:59.607",
                            "MyThread": null
                        }
                        var MaxID = $($scope.Bindable).max(function () { return this.TopicID });
                        MaxID = isNaN(MaxID) || !isFinite(MaxID) ? 0 : MaxID;
                        var TopicToPush = {
                            "UserName": CurrentUserName,
                            "TopicID": MaxID + 1,
                            "ThreadID": 1,
                            "Description": $scope.Message,
                            "CreatedBy": CurrentUserID,
                            "CreatedDate": new Date()
                        };
                        //MaxID + 1 for tentative calcuation to avoid wrongly fetching data
                        console.log("TopicToPush :- ", TopicToPush);

                        ChatService.PostChat.save(TopicToSave, function () {

                            $scope.Bindable.push(TopicToPush);
                            $scope.Message = "";
                            angular.element('#txtMessage').trigger('focus');
                            ScrollToLastMessage();
                        })
                    };
                    $scope.SaveGroup = function () {
                        var GroupToSave = {
                            "Description": $scope.GroupName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607", "MyThread": null

                        }
                        ChatService.PostGroup.save(GroupToSave, function () {
                            //alert('Saved!');

                            $scope.AllGroups.push(GroupToSave);
                        })
                    };

                    /*****************  direct calls  ********************/
                    

                    console.log('Call ListChat');
                    $scope.ListChat();
                }
                catch (ex) {
                    console.log("ChatController", ex);
                }
            }]);

})();
