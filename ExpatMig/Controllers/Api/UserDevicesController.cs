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
            UserDevice ThisDevice =null;

            if (userDevice.DeviceID == null)
            {
                // for Browsers
                var FoundDevices = db.UserDevices.Where(x => x.ApiRegistrationID == userDevice.ApiRegistrationID).ToList();

                if (FoundDevices.Count > 0) { ThisDevice = FoundDevices.OrderByDescending(x => x.UserDeviceID).First(); }
            }
            else
            {
                // for Androids
                var ExistingDevices = db.UserDevices.Where(x => x.DeviceID == userDevice.DeviceID).ToList();

                if (ExistingDevices.Count > 0) { ThisDevice = ExistingDevices.OrderByDescending(x => x.UserDeviceID).First(); }

            }

            if (ThisDevice != null)
            {
               
                ThisDevice.UserID = userDevice.UserID;
                ThisDevice.ApiRegistrationID = userDevice.ApiRegistrationID;
                ThisDevice.ModifiedBy = userDevice.ModifiedBy;
                ThisDevice.ModifiedDate = DateTime.Now;
                db.Entry(ThisDevice).State = EntityState.Modified;
              

            }
            else
            {
                userDevice.CreatedDate = DateTime.Now;
                db.UserDevices.Add(userDevice);
               
            }
            db.SaveChanges();
            ThisDevice = db.UserDevices.Where(x => x.ApiRegistrationID == userDevice.ApiRegistrationID).First();
            return CreatedAtRoute("DefaultApi", new
            {
                id = ThisDevice.UserDeviceID
            }, ThisDevice);
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