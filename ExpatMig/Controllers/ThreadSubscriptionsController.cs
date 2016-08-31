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
using System.Collections;
using ExpatMig.ViewModels;
using Microsoft.AspNet.Identity;

namespace ExpatMig.Controllers.Api
{
    public class ThreadSubscriptionsController : ApiController
    {

        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet, Route("api/ThreadSubscriptions/GetHisThreads/{UserID}")]
        public IQueryable GetHisThreads(int UserID)
        {
         if(db.ThreadSubscriptions.Count() == 0) { return null; }
            var Output = from EachThreads in db.Threads
                         join EachThreadSun in db.ThreadSubscriptions on
                         EachThreads.ThreadID equals EachThreadSun.ThreadID
                         where EachThreadSun.UserID == UserID && EachThreadSun.IsActive==true
                         select EachThreads.ThreadID;
            return Output;

        }

        [HttpPost, Route("api/ThreadSubscriptions/PostThreadSubscriptions")]
        public String PostThreadSubscriptions(ThreadSubscription InputThreadSub)
        {
            try
            {           
            ThreadSubscription NewThreadSub = new ThreadSubscription();
            if (db.ThreadSubscriptions.Count() != 0)
            {
                var ExisitingThreadSub = db.ThreadSubscriptions.Where(x => x.ThreadID == InputThreadSub.ThreadID && x.UserID == InputThreadSub.UserID);
                if (ExisitingThreadSub.Count() >0)
                {
                    NewThreadSub = ExisitingThreadSub.First();
                    return "your request in pipeline";
                }
            }          
                          
                NewThreadSub.ThreadID = InputThreadSub.ThreadID;
                NewThreadSub.UserID = InputThreadSub.UserID;
                NewThreadSub.SeqNo = 0;
                NewThreadSub.IsActive = false;
                NewThreadSub.CreatedBy = InputThreadSub.UserID;
                NewThreadSub.CreatedDate = InputThreadSub.CreatedDate;
                db.ThreadSubscriptions.Add(NewThreadSub);
                db.SaveChanges();
                return "your request send successfully";
         
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }


    }
}