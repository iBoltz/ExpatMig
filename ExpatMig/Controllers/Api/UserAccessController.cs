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
        public class UserAccessModel
        {
            public String UserName { get; set; }
            public String Password { get; set; }

        }

        [HttpPost, Route("api/UserAccess/Login")]
        public int Login(UserAccessModel AccessModel)
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

        [HttpPost, Route("api/UserAccess/Register")]
        public String Register(UserAccessModel NewAccessUser)
        {
            try
            {
                var manager = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();

                var FoundUser = manager.Find(NewAccessUser.UserName, NewAccessUser.Password);

                if (FoundUser == null)
                {
                    var user = new ApplicationUser { UserName = NewAccessUser.UserName, Email = NewAccessUser.UserName };
                    var NewUser=  manager.CreateAsync(user, NewAccessUser.Password);
                    if (NewUser.Result.Succeeded) {
                        return "success";
                    }
                    else
                    {
                        return NewUser.Result.Errors.First();
                    }
                   
                }
                else
                {
                    return "Already Exists User";
                }
            }
            catch(Exception ex)
            {
                return ex.Message;
            }
           
        }
    }
}
