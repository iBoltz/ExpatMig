(function () {
    'use strict';

    angular.module('xMigApp').
        factory('ChatService', ['$resource', '$cacheFactory', function ($resource, $cacheFactory) {
            return {
                ListChats: $resource('/api/topics/:id'),
                PostChat: $resource('/api/topics'),
                GetThreads: $resource('/api/Threads/GetThreadsByGroupID/:id'),
                PostThread: $resource('/api/Threads'),
                ListGroups: $resource('/api/Groups/:id'),
                PostGroup: $resource('/api/Groups')
            }

        }]).
        factory('BridgeService', function () {
            return {
                //Bridge: {},
                //BridgeTextFilters: {} 

            }

        });
})();