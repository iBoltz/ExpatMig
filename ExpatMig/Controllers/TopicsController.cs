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
using ExpatMig.Utils;
using Newtonsoft.Json;
using System.Web.Script.Serialization;

namespace ExpatMig.Controllers
{
    public class TopicsController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet, Route("api/Topics/AllTopicsForThisThread/{ThreadID}")]
        public IQueryable AllTopicsForThisThread(int ThreadID)
        {
            var Output = from EachTopic in db.Topics
                         join EachUser in db.Users on EachTopic.CreatedBy equals EachUser.Id
                         where EachTopic.ThreadID == ThreadID
                         select new
                         {
                             EachUser.UserName,
                             EachTopic.TopicID,
                             EachTopic.ThreadID,
                             EachTopic.Description,
                             EachTopic.CreatedBy,
                             EachTopic.CreatedDate
                         };
            return Output;
        }


        [HttpGet, Route("api/Topics/GetLatest/{id}")]
        public IQueryable GetLatest(int id)
        {
            var Output = from EachTopic in db.Topics
                         join EachUser in db.Users on EachTopic.CreatedBy equals EachUser.Id
                         where EachTopic.TopicID > id
                         select new
                         {
                             EachUser.UserName,
                             EachTopic.TopicID,
                             EachTopic.ThreadID,
                             EachTopic.Description,
                             EachTopic.CreatedBy,
                             EachTopic.CreatedDate
                         };

            return Output;
        }

        // GET: api/Topics/5
        [ResponseType(typeof(Topic))]
        public IHttpActionResult GetTopic(int id)
        {
            Topic topic = db.Topics.Find(id);
            if (topic == null)
            {
                return NotFound();
            }

            return Ok(topic);
        }

        // GET: api/Topics/5




        // PUT: api/Topics/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutTopic(int id, Topic topic)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != topic.TopicID)
            {
                return BadRequest();
            }

            db.Entry(topic).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TopicExists(id))
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

        // POST: api/Topics
        [ResponseType(typeof(Topic))]
        public IHttpActionResult PostTopic(Topic topic)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Topics.Add(topic);
            db.SaveChanges();

          
            var TopicMessage = new JavaScriptSerializer().Serialize(topic);

            foreach (var ThatUserID in db.Topics.Select(x => x.CreatedBy).Distinct())
            {
                if (ThatUserID == topic.CreatedBy) continue;
                var HisDevices = db.UserDevices.Where(x => x.UserID == ThatUserID);

                foreach (var EachDevice in HisDevices)
                {
                    var Notification = new NotificationsModel
                    {
                        NotificationType = AppConstants.NotificationTypes.Topics,
                        NotificationDataType="TopicsModel",
                        NotificationData= TopicMessage,
                        NotificationCreatedDate =DateTime.Now.ToString()
                    };

                    var NotifierMessage = new JavaScriptSerializer().Serialize(Notification);

                    PushNotificationsFacade.SendNotification(EachDevice, NotifierMessage);
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = topic.TopicID }, topic);
        }

        // DELETE: api/Topics/5
        [ResponseType(typeof(Topic))]
        public IHttpActionResult DeleteTopic(int id)
        {
            Topic topic = db.Topics.Find(id);
            if (topic == null)
            {
                return NotFound();
            }

            db.Topics.Remove(topic);
            db.SaveChanges();

            return Ok(topic);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool TopicExists(int id)
        {
            return db.Topics.Count(e => e.TopicID == id) > 0;
        }
    }
}