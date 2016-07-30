(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('ChatController', ['$scope', 'ChatService', '$http', 'BridgeService', '$filter', function ($scope, ChatService, $http, BridgeService, $filter) {
                try {

                    function FormatViewModal(Topics) {
                        Topics.forEach(function (EachItem, index, theArray) {
                            theArray[index] = FormatTopic(EachItem);
                        });
                        return Topics;
                    }
                    function FormatTopic(Topic)
                    {
                        Topic.Color = iBoltzColorGen.GetMyColor(Topic.UserName, Topic.CreatedBy);
                        if (IsDateToday(Topic.CreatedDate)) {
                            Topic.CreatedDateString = $filter('date')(Topic.CreatedDate, "hh:mm a");
                        } else {
                            Topic.CreatedDateString = $filter('date')(Topic.CreatedDate, "dd-MMM-yy hh:mm");
                        }
                        return Topic;
                    }
                    $scope.SelectedGroup = "Select Group";
                    $scope.SelectedThreadID = 0;

                    $scope.GetLatest = function () {
                        console.log('Fetching data');
                        var MaxID = $($scope.Bindable).max(function () { return this.TopicID });
                        MaxID = isNaN(MaxID) || !isFinite(MaxID) ? 0 : MaxID;

                        ChatService.GetLatest.query({ id: MaxID }, function (result) {

                            $scope.Bindable = FormatViewModal($scope.Bindable.concat(result));

                            ScrollToLastMessage();
                            console.log(FullList);
                            return result;
                        });
                    };
                    $scope.ListChat = function (SelectedThreadID) {
                        var FullList = ChatService.ListChats.query({ id: SelectedThreadID }, function (result) {
                            ScrollToLastMessage();
                            return FormatViewModal(result);

                        });
                        $scope.Bindable = FullList;
                        console.log($scope.Bindable);
                    };
                    $scope.AllGroups = ChatService.ListGroups.query(function (result) {
                        return result;
                    });
                    $scope.getselectgroup = function (SelectedGroup) {
                        $scope.SelectedGroup = SelectedGroup.Description;
                        $scope.SelectedGroupID = SelectedGroup.GroupID;
                        $scope.AllThreads = ChatService.GetThreads.query({ id: $scope.SelectedGroupID }, function (result) {
                            return result;
                        });
                    }
                    $scope.getselectthhread = function (SelectedID) {
                        $scope.SelectedThreadID = SelectedID;
                        $scope.ListChat(SelectedID);
                    }

                    /*****************  Save routines   ********************/
                    $scope.OnEnterPress = function (keyEvent) {
                        if (keyEvent.which === 13) $scope.SaveChanges();
                    }

                    $scope.SaveThread = function () {
                        if ($scope.SelectedGroupID == undefined) {
                            alert('Please select group first');
                            return;
                        }
                        if ($scope.ThreadName == undefined) {
                            alert('Please type new thread name!!');
                            return;
                        }

                        var ThreadToSave = {
                            "GroupID": $scope.SelectedGroupID,
                            "Description": $scope.ThreadName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607", "MyThread": null

                        }
                        ChatService.PostThread.save(ThreadToSave, function () {
                            $scope.AllThreads = ChatService.GetThreads.query({ id: $scope.SelectedGroupID }, function (result) {
                                return result;
                            });
                            //  $scope.AllThreads.push(ThreadToSave);
                        })
                    };
                    $scope.SaveChanges = function () {
                        if ($scope.Message == undefined) {
                            alert('Type your message !!');
                            return;
                        }
                        if ($scope.SelectedThreadID == null) { return }
                        var TopicToSave = {
                            "TopicID": 2,
                            "ThreadID": $scope.SelectedThreadID,
                            "Description": $scope.Message,
                            "Slug": null, "IsActive": true,
                            "SeqNo": 1, "CreatedBy": CurrentUserID,
                            "CreatedDate": new Date()
                        }
                        var MaxID = $($scope.Bindable).max(function () { return this.TopicID });
                        MaxID = isNaN(MaxID) || !isFinite(MaxID) ? 0 : MaxID;
                        var TopicToPush = {
                            "UserName": CurrentUserName,
                            "TopicID": MaxID + 1,
                            "ThreadID": $scope.SelectedThreadID,
                            "Description": $scope.Message,
                            "CreatedBy": CurrentUserID,
                            "CreatedDate": new Date()
                        };
                        //alert(new Date());
                        //MaxID + 1 for tentative calcuation to avoid wrongly fetching data
                        console.log("TopicToPush :- ", TopicToPush);

                        ChatService.PostChat.save(TopicToSave, function () {

                            $scope.Bindable.push(FormatTopic(TopicToPush));
                            
                            $scope.Message = "";
                            angular.element('#txtMessage').trigger('focus');
                            ScrollToLastMessage();
                        })
                    };
                    $scope.SaveGroup = function () {
                        if ($scope.GroupName == undefined) {
                            alert('Please type new group name!!');
                            return;
                        }
                        var GroupToSave = {
                            "Description": $scope.GroupName,
                            "Slug": null, "IsActive": true, "SeqNo": 1, "CreatedBy": 1, "CreatedDate": "2016-07-25T12:48:59.607", "ModifiedBy": 1, "ModifiedDate": "2016-07-25T12:48:59.607", "MyThread": null

                        }
                        ChatService.PostGroup.save(GroupToSave, function () {
                            $scope.AllGroups = ChatService.ListGroups.query(function (result) {
                                return result;
                            });
                            //  $scope.AllGroups.push(GroupToSave);
                        })
                    };

                    /*****************  direct calls  ********************/


                    ///  console.log('Call ListChat');
                    // $scope.ListChat();
                }
                catch (ex) {
                    console.log("ChatController", ex);
                }
            }]);

})();
