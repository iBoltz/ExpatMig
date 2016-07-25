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

namespace ExpatMig.Controllers
{
    public class UserDevicesController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: api/UserDevices
        public IQueryable<UserDevice> GetUserDevices()
        {
            return db.UserDevices;
        }

        // GET: api/UserDevices/5
        [ResponseType(typeof(UserDevice))]
        public IHttpActionResult GetUserDevice(int id)
        {
            UserDevice userDevice = db.UserDevices.Find(id);
            if (userDevice == null)
            {
                return NotFound();
            }

            return Ok(userDevice);
        }

        // PUT: api/UserDevices/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutUserDevice(int id, UserDevice userDevice)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != userDevice.UserDeviceID)
            {
                return BadRequest();
            }

            db.Entry(userDevice).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserDeviceExists(id))
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

        // POST: api/UserDevices
        [ResponseType(typeof(UserDevice))]
        public IHttpActionResult PostUserDevice(UserDevice userDevice)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.UserDevices.Where(x => x.ApiRegistrationID == userDevice.ApiRegistrationID).Count() <= 0)
            {
                userDevice.CreatedDate = DateTime.Now;
                db.UserDevices.Add(userDevice);
                db.SaveChanges();

            }

            return CreatedAtRoute("DefaultApi", new { id = userDevice.UserDeviceID }, userDevice);
        }

        // DELETE: api/UserDevices/5
        [ResponseType(typeof(UserDevice))]
        public IHttpActionResult DeleteUserDevice(int id)
        {
            UserDevice userDevice = db.UserDevices.Find(id);
            if (userDevice == null)
            {
                return NotFound();
            }

            db.UserDevices.Remove(userDevice);
            db.SaveChanges();

            return Ok(userDevice);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UserDeviceExists(int id)
        {
            return db.UserDevices.Count(e => e.UserDeviceID == id) > 0;
        }
    }
}