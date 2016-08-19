using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Microsoft.AspNet.Identity.Owin;
using System.Web;
using ExpatMig.Models;

namespace ExpatMig.Controllers.Api
{
    public class UserAccessController : ApiController
    {
        public class LoginModel
        {
            public String UserName { get; set; }
            public String Password { get; set; }

        }

        [HttpPost, Route("api/UserAccess/Login")]
        public int Login(LoginModel AccessModel)
        {
            var manager = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();

            var FoundUser = manager.Find(AccessModel.UserName, AccessModel.Password);

            if (FoundUser != null)
            {
                return FoundUser.Id;
            }
            else
            {
                return -1;
            }

        }
    }
}
