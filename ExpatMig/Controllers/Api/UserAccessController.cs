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
using ExpatMig.Data;
using System.Threading.Tasks;

namespace ExpatMig.Controllers.Api
{
    public class UserAccessController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();
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



     
        [HttpPost,Route("api/Account/Register")]
      
        public Boolean Register(LoginModel NewUser)
        {
            try
            {

                var ExistingUser = db.Users.Where(x => x.UserName == NewUser.UserName || x.Email == NewUser.UserName).ToList();
                if (ExistingUser.Count ==0)
                {
                    var manager = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();
                    var user = new ApplicationUser { UserName = NewUser.UserName, Email = NewUser.UserName };
                    var result =  manager.CreateAsync(user, NewUser.Password).Result;
                   
                    return result.Succeeded;
                }
                return false;
            }
            catch(Exception ex)
            {
                return false;
            }
          
        }
    }
}
