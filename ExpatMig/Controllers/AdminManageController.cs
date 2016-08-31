using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using ExpatMig.Data;
using ExpatMig.Models;
using Microsoft.AspNet.Identity;
using ExpatMig.ViewModels;
using System.Threading.Tasks;
using System.Threading;

namespace ExpatMig.Controllers
{
    [Authorize]
    public class AdminManageController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpPost]
        public ActionResult Index(int[] ids)
        {
                    
           if (db.ThreadSubscriptions.Count() == 0) { return View(Bindable()); }

            var ExistingActiveThreads = db.ThreadSubscriptions.Where(x => x.IsActive == true).ToList();

            List<ThreadSubscription> SelectedThreadSubs = new List<ThreadSubscription>();

            if (ids == null)
            {
                DeActivateExistingRequest(ExistingActiveThreads);
                return View(Bindable());
            }
            else
            {
                List<int> SelectedIDs = new List<int>(ids);


                 SelectedThreadSubs = (from eachThreadSub in db.ThreadSubscriptions
                                          join eachID in SelectedIDs on eachThreadSub.ThreadSubscriptionID equals eachID
                                          select eachThreadSub).ToList();

            }

          
            if(SelectedThreadSubs.Count>0 && ExistingActiveThreads.Count() > 0)
            {
                //Mached Id's to skip
                var MatchedItems = (from Eachitem in SelectedThreadSubs
                                    join Eachdata in ExistingActiveThreads on Eachitem.ThreadSubscriptionID equals Eachdata.ThreadSubscriptionID
                                    select Eachitem).ToList();
                if (MatchedItems.Count > 0)
                {
                    foreach (ThreadSubscription item in MatchedItems) SelectedThreadSubs.Remove(item);
                    foreach (ThreadSubscription item in MatchedItems) ExistingActiveThreads.Remove(item);

                }
            }
          

           

            ActivateNewRequests(SelectedThreadSubs);
            DeActivateExistingRequest(ExistingActiveThreads);

            if (ids != null)
            {
                ViewBag.Message = "Access Granded";
            }
            else
            {
                ViewBag.Message = "No record selected";
            }


            return View(Bindable());
        }

        private List<ThreadSubscription> Bindable()
        {
            return db.ThreadSubscriptions.Include(u => u.MyThread).Include(u => u.User).ToList();
        }
        private void ActivateNewRequests(List<ThreadSubscription> SelectedThreadSubs)
        {
            if (SelectedThreadSubs.Count > 0)
            {
                SelectedThreadSubs = SelectedThreadSubs.AsQueryable().Include(u => u.MyThread).Include(u => u.User).ToList();
                //Activate new Request
                foreach (ThreadSubscription element in SelectedThreadSubs)
                {
                   
                        //Here is a new thread
                        UpdateThreadSubscription(element, true);
                  

                }
            }

        }
        private void DeActivateExistingRequest(List<ThreadSubscription> ExistingActiveThreads)
        {
         
            if (ExistingActiveThreads.Count() > 0)
            {

                //Deactivate 
                foreach (ThreadSubscription element in ExistingActiveThreads)
                {
                  
                        UpdateThreadSubscription(element, false);
                  
                }

            }
        }
        private void UpdateThreadSubscription(ThreadSubscription item,Boolean IsChecked)
        {
            Task.Run(() => {
           
              
            if (IsChecked)
            {
                item.IsActive = true;
                item.ModifiedBy = item.CreatedBy;
                item.ModifiedDate = DateTime.Now;
            }
            else
            {
                item.IsActive = false;
            }
                db.Entry(item).State = EntityState.Modified;
                db.SaveChanges();
            });
        }
        // GET: AdminManage
        public ActionResult Index()
        {
           return View(Bindable());
           
        }
    }
}

