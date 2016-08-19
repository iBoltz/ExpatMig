(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('ChatController', ['$scope', 'ChatService', '$http', 'BridgeService', '$filter', '$sce',
                function ($scope, ChatService, $http, BridgeService, $filter, $sce) {

                    var TopicPageIndex = 0;
                    var HasReachedTop = false;
                    var FirstTopicItem = null;
                    var LoadedItems = 0;
                    var PageSize = 15;
                    var FreshLoad = true;
                    $scope.AllTopics = [];
                    try {

                        function FormatViewModal(Topics) {
                            Topics.forEach(function (EachItem, index, theArray) {
                                theArray[index] = FormatTopic(EachItem);
                            });
                            return Topics;
                        }
                        function FormatTopic(Topic) {
                            Topic.Color = iBoltzColorGen.GetMyColor(Topic.UserName, Topic.CreatedBy);
                            if (IsDateToday(Topic.CreatedDate)) {
                                Topic.CreatedDateString = $filter('date')(Topic.CreatedDate, "hh:mm a");
                            } else {
                                Topic.CreatedDateString = $filter('date')(Topic.CreatedDate, "dd-MMM-yy hh:mm");
                            }

                            Topic.OwnerAlignLeft = Topic.CreatedBy == CurrentUserID ? '18%' : '2%';

                            
                           

                            return Topic;
                        }
                        $scope.RenderWithEmoji=function(Description)
                        {
                            return $sce.trustAsHtml(emojione.toImage(Description));

                        }
                        function ListChatPaged(SelectedThreadID, PageIndex) {
                            $('#ChatHistory').ShowLoadingPanel();
                            var FullList = ChatService.ListChats.query({ id: SelectedThreadID, PageIndex: PageIndex }, function (result) {
                                //ScrollToLastMessage();
                                var Formated = FormatViewModal(result);
                                if (Formated.length <= 0) {
                                    HasReachedTop = true;
                                    ShowToast('No more new messges');
                                    return;
                                }
                                console.log(" Existing data in memory " + $scope.AllTopics.length)

                                if (TopicPageIndex == 0) {
                                    $scope.AllTopics = Formated;//fresh load if pageindex=0;
                                } else {
                                    $scope.AllTopics = Formated.concat($scope.AllTopics);
                                }

                                return $scope.AllTopics;

                            });

                            // $scope.AllTopics = FullList;

                            console.log(FullList);
                        };
                        $scope.SelectedGroup = "Select Group";
                        $scope.SelectedThreadID = 0;
                        $scope.LoadMoreTopics = function () {

                            console.log("In LoadMoreTopics", FreshLoad)
                            FreshLoad = false;
                            TopicPageIndex += 1;
                            console.log(" fetching page " + TopicPageIndex)
                            if (HasReachedTop) return;
                            ListChatPaged($scope.SelectedThreadID, TopicPageIndex);


                        };
                        $scope.GetLatest = function () {
                            console.log('Fetching data');
                            var MaxID = $($scope.AllTopics).max(function () { return this.TopicID });
                            MaxID = isNaN(MaxID) || !isFinite(MaxID) ? 0 : MaxID;

                            ChatService.GetLatest.query({ id: MaxID }, function (result) {

                                $scope.AllTopics = FormatViewModal($scope.AllTopics.concat(result));

                                //console.log(FullList);
                                return result;
                            });
                        };
                        $scope.ListChat = ListChatPaged;
                        $scope.AllGroups = ChatService.ListGroups.query(function (result) {
                            return result;
                        });
                        $scope.ListThreadsForGroup = function (SelectedGroup) {
                            $scope.SelectedGroup = SelectedGroup.Description;
                            $scope.SelectedGroupID = SelectedGroup.GroupID;
                            $scope.AllThreads = ChatService.GetThreads.query({ id: $scope.SelectedGroupID }, function (result) {
                                return result;
                            });
                        }
                        $scope.ListTopicsForThread = function (SelectedID) {
                            $scope.SelectedThreadID = SelectedID;
                            //$scope.AllTopics = [];
                            TopicPageIndex = 0;
                            HasReachedTop = 0;
                            FreshLoad = true;
                            $scope.ListChat(SelectedID, TopicPageIndex);

                        }


                        $scope.OnItemDatabound = function (element) {
                            console.log('FreshLoad', FreshLoad)
                            if (FreshLoad) return;

                            if (LoadedItems >= PageSize - 1) {
                                LoadedItems = 0;
                                //all items loaded irrespective inserts, push, replace array

                                if (FirstTopicItem != null) {
                                    //console.log($(FirstTopicItem).text());

                                    $('#ChatHistory').animate({
                                        scrollTop: FirstTopicItem.offset().top - 100
                                    }, 0);
                                    $('#ChatHistory').HideLoadingPanel();
                                }
                                FirstTopicItem = $('#ChatHistory li').first();

                            } else {
                                LoadedItems += 1;
                            }

                        };

                        $scope.ShowUserPrfile = function (UserID) {

                            BridgeService.BridgeUserProfile(UserID);

                        };

                        /*****************  Last Item Loaded  ********************/
                        $scope.OnLastGroupLoaded = function (element) {
                            //self.alert("loaded");
                            //$('#ddlGroups').selectpicker('refresh');
                            if ($scope.AllGroups.length > 0) {
                                $scope.SelectedGroup = $scope.AllGroups[0].Description;
                                $scope.ListThreadsForGroup($scope.AllGroups[0]);
                            }
                        };

                        $scope.OnLastThreadLoaded = function (element) {
                            if ($scope.AllThreads.length > 0) {

                                $scope.SelectedThreadID = $scope.AllThreads[0].ThreadID;
                                $scope.ListChat($scope.SelectedThreadID, TopicPageIndex);
                            }
                        };

                        $scope.OnLastTopicLoaded = function (element) {
                            ScrollToLastMessage();
                            $('#ChatHistory').HideLoadingPanel();
                        };


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
                            $('#txtMessage').ShowLoadingPanel();
                            var UserDeviceID = 0;

                            if (typeof (RegisteredUserDeviceID) != undefined) {
                                UserDeviceID = RegisteredUserDeviceID;
                             //   alert(UserDeviceID);
                            }


                            var TopicToSave = {
                                "TopicID": 2,
                                "ThreadID": $scope.SelectedThreadID,
                                "Description": $scope.Message,
                                "Slug": null, "IsActive": true,
                                "SeqNo": 1, "CreatedBy": CurrentUserID,
                                "CreatedDate": new Date(),
                                "UserDeviceID": UserDeviceID
                            }
                            console.log("Current Time is", new Date());

                            var MaxID = $($scope.AllTopics).max(function () { return this.TopicID });
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
                                $('#txtMessage').HideLoadingPanel();
                                $scope.AllTopics.push(FormatTopic(TopicToPush));

                                $scope.Message = "";
                                $(".emojionearea-editor").html("");
                                $(".emojionearea-editor").trigger('focus');
                                //angular.element('#txtMessage').trigger('focus');
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
