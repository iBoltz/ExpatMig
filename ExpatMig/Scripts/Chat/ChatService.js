(function () {
    'use strict';

    angular.module('xMigApp').
        factory('ChatService', ['$resource', '$cacheFactory', function ($resource, $cacheFactory) {
            return {
                ListChats: $resource('/api/topics/AllTopicsForThisThread/:id'),
                GetLatest: $resource('/api/topics/getlatest/:id'),
                PostChat: $resource('/api/topics')
            }

        }]).
        factory('BridgeService', function () {
            return {
                //Bridge: {},
                //BridgeTextFilters: {} 

            }

        });
})();