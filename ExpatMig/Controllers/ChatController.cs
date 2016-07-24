using ExpatMig.Data;
using ExpatMig.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ExpatMig.Controllers
{
    public class ChatController : Controller
    {
        private ApplicationDbContext Db = new ApplicationDbContext();
        // GET: Chat
        public ActionResult Index()
        {
            return View();
        }

        // GET: Chat/Details/5
        public ActionResult Details()
        {
            return View("ChatDetails");
        }
        [HttpPost]
        public ActionResult Details(Topic topic)
        {
            topic.ThreadID = 1;
            topic.SeqNo = 1;
            topic.CreatedBy = 1;
            topic.CreatedDate = DateTime.Now;
            topic.ModifiedBy = 1;
            topic.ModifiedDate = DateTime.Now;

            Db.Topics.Add(topic);
            Db.SaveChanges();
            return View("ChatDetails");
        }
        // GET: Chat/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Chat/Create
        [HttpPost]
        public ActionResult Create(FormCollection collection)
        {
            try
            {
                // TODO: Add insert logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Chat/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: Chat/Edit/5
        [HttpPost]
        public ActionResult Edit(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add update logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Chat/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: Chat/Delete/5
        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add delete logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }
    }
}
