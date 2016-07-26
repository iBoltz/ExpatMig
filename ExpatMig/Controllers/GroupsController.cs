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
    public class GroupsController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: api/Groups
        public IQueryable<Group> GetGroups()
        {
            
            return db.Groups;
        }

        // GET: api/Groups/5
        [ResponseType(typeof(Group))]
        public IHttpActionResult GetGroup(int id)
        {
            Group Group = db.Groups.Find(id);
            if (Group == null)
            {
                return NotFound();
            }

            return Ok(Group);
        }

        // PUT: api/Groups/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutGroup(int id, Group Group)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != Group.GroupID)
            {
                return BadRequest();
            }

            db.Entry(Group).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GroupExists(id))
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

        // POST: api/Groups
        [ResponseType(typeof(Group))]
        public IHttpActionResult PostGroup(Group Group)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Groups.Add(Group);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = Group.GroupID }, Group);
        }

        // DELETE: api/Groups/5
        [ResponseType(typeof(Group))]
        public IHttpActionResult DeleteGroup(int id)
        {
            Group Group = db.Groups.Find(id);
            if (Group == null)
            {
                return NotFound();
            }

            db.Groups.Remove(Group);
            db.SaveChanges();

            return Ok(Group);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool GroupExists(int id)
        {
            return db.Groups.Count(e => e.GroupID == id) > 0;
        }
    }
}