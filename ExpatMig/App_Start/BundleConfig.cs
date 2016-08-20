using System.Web;
using System.Web.Optimization;

namespace ExpatMig
{
    public class BundleConfig
    {
        // For more information on bundling, visit http://go.microsoft.com/fwlink/?LinkId=301862
        public static void RegisterBundles(BundleCollection bundles)
        {
            bundles.Add(new ScriptBundle("~/bundles/jquery").Include(
                        "~/Scripts/jquery-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryui").Include(
                        "~/Scripts/jquery-ui-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryval").Include(
                        "~/Scripts/jquery.validate*"));

            // Use the development version of Modernizr to develop with and learn from. Then, when you're
            // ready for production, use the build tool at http://modernizr.com to pick only the tests you need.
            bundles.Add(new ScriptBundle("~/bundles/modernizr").Include(
                        "~/Scripts/modernizr-*"));

            bundles.Add(new ScriptBundle("~/bundles/bootstrap").Include(
                      "~/Scripts/bootstrap.js",
                      "~/Scripts/respond.js"));

            bundles.Add(new StyleBundle("~/Content/css").Include(
                "~/Content/themes/base/jquery-ui.min.css",
                "~/Content/themes/ui-darkness/jquery-ui.ui-darkness.min.css",
                "~/Content/emojionearea.css",
                      "~/Content/bootstrap.css",
                      "~/Content/site.css"));


            bundles.Add(new StyleBundle("~/bundles/chatstyle").Include(
                      "~/Content/Chat.css"));

            bundles.Add(new ScriptBundle("~/bundles/global").Include(
                        "~/Scripts/Global/Utils.js",
                        "~/Scripts/Global/iBoltz.Loghelper.js",
                        "~/Scripts/Global/iBoltz.PhotoUploader.js",
                        "~/Scripts/Emoji/emojionearea.js",
                        "~/Scripts/Global/iBoltz.Plugins.js"
                        ));
            bundles.Add(new ScriptBundle("~/bundles/PhotoUploader").Include(
                   "~/Scripts/PhotoUploader/UserProfile.js"));

       bundles.Add(new ScriptBundle("~/bundles/chatng").Include(
                        "~/Scripts/Global/iBoltz.ColorGen.js",
                        "~/Scripts/angular.js",
                        "~/Scripts/angular-resource.js",
                        "~/Scripts/Global/Angular.Directives.js",
                        "~/Scripts/chat/ChatScript.js",
                        "~/Scripts/chat/ChatApp.js",
                        "~/Scripts/chat/ChatService.js",
                        "~/Scripts/chat/ChatController.js",
                        "~/Scripts/UserProfile/UserProfileService.js",
                        "~/Scripts/UserProfile/UserProfileController.js",
                        "~/Scripts/chat/PushServiceWorker.js",
                        "~/Scripts/chat/ChatPush.js",
                        "~/Scripts/PhotoUploader/ChatWindow.js"
                      ));




        }
    }
}
