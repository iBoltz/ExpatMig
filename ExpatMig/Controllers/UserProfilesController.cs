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

namespace ExpatMig.Controllers
{
    [Authorize]
    public class UserProfilesController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: UserProfiles
        public ActionResult Index()
        {
            var userProfiles = db.UserProfiles.Include(u => u.MigratingTo).Include(u => u.NativeCity);
            return View(userProfiles.ToList());
        }

        // GET: UserProfiles/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            UserProfile userProfile = db.UserProfiles.Find(id);
            if (userProfile == null)
            {
                return HttpNotFound();
            }
            return View(userProfile);
        }

        // GET: UserProfiles/Create
        public ActionResult Create()
        {
            ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description");
            ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description");
            return View();
        }

        // POST: UserProfiles/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "UserProfileID,UserID,Nick,FirstName,LastName,PhoneNumber,BirthDay,NativeCityID,MigratingToID,Experience,Sector,LinkedIn,VisaType,VisaGrantOn,Suburb,IsActive,SeqNo,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate")] UserProfile userProfile)
        {
            if (ModelState.IsValid)
            {
                db.UserProfiles.Add(userProfile);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description", userProfile.MigratingToID);
            ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description", userProfile.NativeCityID);
            return View(userProfile);
        }

        // GET: UserProfiles/Edit/5
        public ActionResult Edit(int? id)
        {
            ViewBag.CurrentUserID = User.Identity.GetUserId();
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            var FoundUser = db.UserProfiles.Where(x => x.UserID == id).FirstOrDefault();
            if (FoundUser == null)
            {
                ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description", -1);
                ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description", -1);

                return View(new UserProfileViewModel());
            }
            else
            {
                ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description", FoundUser.MigratingToID);
                ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description", FoundUser.NativeCityID);
                if (!string.IsNullOrEmpty(FoundUser.ProfilePic))
                {
                    ViewBag.ProfilePic = FoundUser.ProfilePic.Trim();
                }
                UserProfileViewModel userProfile = new UserProfileViewModel(FoundUser);
                if (userProfile == null)
                {
                    return HttpNotFound();
                }
                if (FoundUser.UserID != int.Parse(User.Identity.GetUserId()))
                {
                    return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
                }
                return View(userProfile);
            }

        }

        // POST: UserProfiles/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Exclude = "UserID,IsActive,SeqNo,CreatedBy,CreatedDate",
            Include = "UserProfileID,Nick,FirstName,LastName,PhoneNumber,BirthDay,NativeCityID,MigratingToID,Experience,Sector,LinkedIn,VisaType,VisaGrantOn,Suburb")]
            UserProfileViewModel HisViewModel)
        {
            var userProfile = db.UserProfiles.Where(x => x.UserProfileID == HisViewModel.UserProfileID).FirstOrDefault();

            if (ModelState.IsValid)
            {

                try
                {
                    var IsNew = userProfile == null;

                    if (IsNew)
                    {
                        userProfile = new UserProfile();
                        userProfile.UserID = int.Parse(User.Identity.GetUserId());
                    }
                    userProfile.BirthDay = HisViewModel.BirthDay;
                    userProfile.Experience = HisViewModel.Experience;
                    userProfile.FirstName = HisViewModel.FirstName;
                    userProfile.LastName = HisViewModel.LastName;
                    userProfile.LinkedIn = HisViewModel.LinkedIn;

                    userProfile.MigratingToID = HisViewModel.MigratingToID;
                    userProfile.NativeCityID = HisViewModel.NativeCityID;
                    userProfile.Nick = HisViewModel.Nick;
                    userProfile.PhoneNumber = HisViewModel.PhoneNumber;
                    userProfile.Sector = HisViewModel.Sector;
                    userProfile.Suburb = HisViewModel.Suburb;
                    userProfile.VisaGrantOn = HisViewModel.VisaGrantOn;
                    userProfile.VisaType = HisViewModel.VisaType;



                    if (IsNew)
                    {
                        userProfile.CreatedBy = int.Parse(User.Identity.GetUserId());
                        userProfile.CreatedDate = DateTime.Now;
                        db.UserProfiles.Add(userProfile);
                        db.SaveChanges();
                    }
                    else
                    {
                        db.Entry(userProfile).State = EntityState.Modified;
                        userProfile.ModifiedBy = int.Parse(User.Identity.GetUserId());
                        userProfile.ModifiedDate = DateTime.Now;
                    }
                    db.SaveChanges();
                    return RedirectToAction("Index");
                }
                catch (Exception ex)
                {
                    throw ex;
                }

            }
            ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description", userProfile.MigratingToID);
            ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description", userProfile.NativeCityID);
            return View(userProfile);
        }

        // GET: UserProfiles/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            UserProfile userProfile = db.UserProfiles.Find(id);
            if (userProfile == null)
            {
                return HttpNotFound();
            }
            return View(userProfile);
        }

        // POST: UserProfiles/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            UserProfile userProfile = db.UserProfiles.Find(id);
            db.UserProfiles.Remove(userProfile);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
