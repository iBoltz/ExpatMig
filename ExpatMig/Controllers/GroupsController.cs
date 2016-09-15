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
using System.Web.Script.Serialization;
using ExpatMig.ViewModels;

namespace ExpatMig.Controllers
{
    public class GroupsController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        [HttpGet, Route("api/Groups/Full")]
        public IQueryable GetGroups()
        {
          
            var GroupList = db.Groups.Include(x => x.MyThreads).ToList();


            var filter = from EachGroup in GroupList
                         group EachGroup.MyThreads.Select(x => ConvertToThread(x.ThreadID, x.GroupID, x.Description, x.SeqNo, x.IsActive, x.CreatedBy, x.CreatedDate, x.ModifiedBy, x.ModifiedDate))
                         by new { EachGroup.GroupID, EachGroup.Description, EachGroup.SeqNo, EachGroup.IsActive, EachGroup.CreatedBy, EachGroup.CreatedDate, EachGroup.ModifiedBy, EachGroup.ModifiedDate } into g
                         select new GroupViewModel
                         {
                             ParentGroup = ConvertToGroup(g.Key.GroupID, g.Key.Description, g.Key.SeqNo,
                           g.Key.IsActive, g.Key.CreatedBy, g.Key.CreatedDate, g.Key.ModifiedBy, g.Key.ModifiedDate),
                             ChildThreads = g.First().ToList()
                         };

           
           return filter.AsQueryable();
        }
       
        private Thread ConvertToThread(int ThreadID,int GroupID, String Description, int SeqNo, Boolean IsActive,
           int CreatedBy, DateTime CreatedDate, int ModifiedBy, DateTime ModifiedDate)
        {
            var ThisThread = new Thread();
            ThisThread.ThreadID = ThreadID;
            ThisThread.GroupID = GroupID;
            ThisThread.Description = Description;
            ThisThread.SeqNo = SeqNo;
            ThisThread.IsActive = IsActive;
            ThisThread.CreatedBy = CreatedBy;
            ThisThread.CreatedDate = CreatedDate;
            ThisThread.ModifiedBy = ModifiedBy;
            ThisThread.ModifiedDate = ModifiedDate;
            return ThisThread;
        }

        private Group ConvertToGroup(int GroupID,String Description,int SeqNo,Boolean IsActive,
            int CreatedBy,DateTime CreatedDate,int ModifiedBy,DateTime ModifiedDate)
        {
            var NewGroup = new Group();
            NewGroup.GroupID = GroupID;
            NewGroup.Description = Description;
            NewGroup.SeqNo = SeqNo;
            NewGroup.IsActive = IsActive;
            NewGroup.CreatedBy = CreatedBy;
            NewGroup.CreatedDate = CreatedDate;
            NewGroup.ModifiedBy = ModifiedBy;
            NewGroup.ModifiedDate = ModifiedDate;
            return NewGroup;
        }
        private List<Thread> GetThreads()
        {
            var AllThreads= db.Threads.ToList();
            return AllThreads;
        }

     
        [HttpGet, Route("api/Groups/All")]
        public IQueryable<Group> GetGroup()
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