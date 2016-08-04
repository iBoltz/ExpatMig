
(function () {
    'use strict';
    angular.module('last-item-loaded', []).
    directive('lastItemLoaded', ['$timeout',
        function ($timeout) {
            return function (scope, element, attrs) {
                //alert(scope.$last);
                if (scope.$last) {
                    if (attrs.lastItemLoaded != "") {
                        scope.$eval(attrs.lastItemLoaded);
                    } else {
                        scope.LastItemLoaded(element);
                    }

                    $timeout(function () {//on fully rendered
                        //timeout is the prescribed way to find the digest is fully applied by ng
                        //so this is the standard way to find ther rendering
                        scope.$eval(attrs.lastItemLoadedRendered);
                    });
                }
            };

        }]);

    angular.module('on-item-databound', []).
     directive('onItemDatabound', ['$timeout',
         function ($timeout) {
             return {
                 link: function (scope, element, attr) {
                     //scope.$apply(attr.onItemDatabound);
                     //console.log("in directive === ",  element);
                     $timeout(function () {
                         scope.$apply(attr.onItemDatabound);
                        //attr.onItemDatabound(elem);
                     });
                     //attr.onItemDatabound();
                     
                     //if (!scope.$$phase) {
                     //    console.log($(elem).html());
                     //    scope.$apply(attr.OnItemDatabound)
                     //}//else attr.onItemDatabound(elem);
                 }
             }
         }]);



    angular.module('upward-infinite-scroll', []).
        directive('upwardInfiniteScroll', ['$timeout',
            function ($timeout) {
                return {
                    link: function (scope, elem, attr, ctrl) {
                        var raw = elem[0];
                        //console.log('test scrolling', raw);
                        elem.bind('scroll', function () {

                            if (raw.scrollTop <= 0) {
                                var sh = raw.scrollHeight;
                                scope.$apply(attr.upwardInfiniteScroll);

                                $timeout(function () {
                                    elem.animate({
                                        scrollTop: raw.scrollHeight - sh
                                    }, 500);
                                }, 0);
                            }
                        });

                        //scroll to bottom
                        $timeout(function () {
                            scope.$apply(function () {
                                elem.scrollTop(raw.scrollHeight);
                            });
                        }, 0);
                    }
                }
            }]);




})();

