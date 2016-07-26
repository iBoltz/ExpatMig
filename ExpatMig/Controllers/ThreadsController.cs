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
    public class ThreadsController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: api/Threads
        public IQueryable<Thread> GetThreads()
        {
            
            return db.Threads;
        }

        [Route("api/Threads/GetThreadsByGroupID/{GroupId}")]
       public IQueryable<Thread> GetThreadByGroupID(int GroupID)
        {
            var ThisGroupThreads= db.Threads.Where(x => x.GroupID == GroupID).AsQueryable();
            return ThisGroupThreads;
        }


        // GET: api/Threads/5
        [ResponseType(typeof(Thread))]
        public IHttpActionResult GetThread(int id)
        {
            Thread Thread = db.Threads.Find(id);
            if (Thread == null)
            {
                return NotFound();
            }

            return Ok(Thread);
        }

        // PUT: api/Threads/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutThread(int id, Thread Thread)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != Thread.ThreadID)
            {
                return BadRequest();
            }

            db.Entry(Thread).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ThreadExists(id))
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

        // POST: api/Threads
        [ResponseType(typeof(Thread))]
        public IHttpActionResult PostThread(Thread Thread)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Threads.Add(Thread);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = Thread.ThreadID }, Thread);
        }

        // DELETE: api/Threads/5
        [ResponseType(typeof(Thread))]
        public IHttpActionResult DeleteThread(int id)
        {
            Thread Thread = db.Threads.Find(id);
            if (Thread == null)
            {
                return NotFound();
            }

            db.Threads.Remove(Thread);
            db.SaveChanges();

            return Ok(Thread);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ThreadExists(int id)
        {
            return db.Threads.Count(e => e.ThreadID == id) > 0;
        }
    }
}