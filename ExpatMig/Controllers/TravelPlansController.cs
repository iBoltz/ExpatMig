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
    public class TravelPlansController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: api/TravelPlans
        public IQueryable<TravelPlans> GetTravelPlans()
        {
            
            return db.TravelPlans;
        }

    

        // GET: api/TravelPlans/5
        [ResponseType(typeof(TravelPlans))]
        public IHttpActionResult GetTravelPlans(int id)
        {
            TravelPlans TravelPlans = db.TravelPlans.Find(id);
            if (TravelPlans == null)
            {
                return NotFound();
            }

            return Ok(TravelPlans);
        }

        // PUT: api/TravelPlans/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutTravelPlans(int id, TravelPlans TravelPlans)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != TravelPlans.TravelPlanID)
            {
                return BadRequest();
            }

            db.Entry(TravelPlans).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TravelPlansExists(id))
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

        // POST: api/TravelPlans
        [ResponseType(typeof(TravelPlans))]
        public IHttpActionResult PostTravelPlans(TravelPlans TravelPlans)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.TravelPlans.Add(TravelPlans);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = TravelPlans.TravelPlanID }, TravelPlans);
        }

        // DELETE: api/TravelPlans/5
        [ResponseType(typeof(TravelPlans))]
        public IHttpActionResult DeleteTravelPlans(int id)
        {
            TravelPlans TravelPlans = db.TravelPlans.Find(id);
            if (TravelPlans == null)
            {
                return NotFound();
            }

            db.TravelPlans.Remove(TravelPlans);
            db.SaveChanges();

            return Ok(TravelPlans);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TravelPlansExists(int id)
        {
            return db.TravelPlans.Count(e => e.TravelPlanID == id) > 0;
        }
    }
}