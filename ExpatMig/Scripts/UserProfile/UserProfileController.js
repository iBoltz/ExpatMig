(function () {
    'use strict';

    angular
        .module('xMigApp')
            .controller('UserProfileController', ['$scope', 'UserProfileService', '$http', 'BridgeService', '$filter', function ($scope, UserProfileService, $http, BridgeService, $filter) {
                try {

                }
                catch (ex) {
                    console.log("UserProfileController", ex);
                }
            }]);

})();
