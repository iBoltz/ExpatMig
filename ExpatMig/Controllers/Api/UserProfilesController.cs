using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using ExpatMig.Data;
using ExpatMig.Models;
using Microsoft.AspNet.Identity;

namespace ExpatMig.Controllers.Api
{
    public class UserProfilesController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpPost, Route("api/UserProfiles/UpdatePhoto")]
        public bool UpdatePhoto([FromBody]UserProfile MyProfile)
        {

            var FoundUser = db.UserProfiles.Where(x => x.UserID == MyProfile.UserID).FirstOrDefault();
            if (FoundUser == null)
            {
                return false;
            }
            else
            {
                FoundUser.ProfilePic = MyProfile.ProfilePic;
                FoundUser.ModifiedBy = int.Parse(User.Identity.GetUserId());
                FoundUser.ModifiedDate = DateTime.Now;

                db.Entry(FoundUser).State = EntityState.Modified;
                db.SaveChanges();

            }



            return true;
        }







        // GET: api/UserProfiles
        public IQueryable<UserProfile> GetUserProfiles()
        {
            return db.UserProfiles;
        }

        //by userid
        // GET: api/UserProfiles/5
        [ResponseType(typeof(UserProfile))]
        public IHttpActionResult GetUserProfile(int id)
        {
            UserProfile userProfile = db.UserProfiles.
                Include(u => u.MigratingTo).
                Include(u => u.NativeCity).
                Where(x => x.UserID == id).FirstOrDefault();

            if (userProfile == null)
            {
                return NotFound();
            }

            return Ok(userProfile);
        }

        // PUT: api/UserProfiles/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutUserProfile(int id, UserProfile userProfile)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != userProfile.UserProfileID)
            {
                return BadRequest();
            }

            db.Entry(userProfile).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserProfileExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/UserProfiles
        [ResponseType(typeof(UserProfile))]
        public IHttpActionResult PostUserProfile(UserProfile userProfile)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.UserProfiles.Add(userProfile);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = userProfile.UserProfileID }, userProfile);
        }

        // DELETE: api/UserProfiles/5
        [ResponseType(typeof(UserProfile))]
        public IHttpActionResult DeleteUserProfile(int id)
        {
            UserProfile userProfile = db.UserProfiles.Find(id);
            if (userProfile == null)
            {
                return NotFound();
            }

            db.UserProfiles.Remove(userProfile);
            db.SaveChanges();

            return Ok(userProfile);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UserProfileExists(int id)
        {
            return db.UserProfiles.Count(e => e.UserProfileID == id) > 0;
        }
    }
}