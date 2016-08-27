(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('UserProfileController', ['$scope', 'UserProfileService', '$http', 'BridgeService', '$filter', function ($scope, UserProfileService, $http, BridgeService, $filter) {
                try {



                    function GetHisProfile(HisUserID) {
                        $scope.HisUserID = HisUserID;
                        $scope.HisProfile = UserProfileService.GetUserProfile.get({ id: HisUserID }, function (result) {
                            return result;
                        });
                    }


                    BridgeService.BridgeUserProfile = function (UserID) {
                        GetHisProfile(UserID);
                    };

                    //BridgeService.BridgeUserProfile = {
                    //    ShowUserProfile: function (UserID) {
                    //        alert('Got you - service works - ' + UserID);
                    //    }

                    //};






                }
                catch (ex) {
                    console.log("UserProfileController", ex);
                }
            }]);

})();
