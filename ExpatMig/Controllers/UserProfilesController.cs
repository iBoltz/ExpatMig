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

namespace ExpatMig.Controllers
{
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
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            UserProfile userProfile = db.UserProfiles.Find(id);
            if (userProfile == null)
            {
                return HttpNotFound();
            }
            ViewBag.MigratingToID = new SelectList(db.Cities, "CityID", "Description", userProfile.MigratingToID);
            ViewBag.NativeCityID = new SelectList(db.Cities, "CityID", "Description", userProfile.NativeCityID);
            return View(userProfile);
        }

        // POST: UserProfiles/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "UserProfileID,UserID,Nick,FirstName,LastName,PhoneNumber,BirthDay,NativeCityID,MigratingToID,Experience,Sector,LinkedIn,VisaType,VisaGrantOn,Suburb,IsActive,SeqNo,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate")] UserProfile userProfile)
        {
            if (ModelState.IsValid)
            {
                db.Entry(userProfile).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
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
