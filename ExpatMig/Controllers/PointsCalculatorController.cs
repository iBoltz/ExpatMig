using ExpatMig.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ExpatMig.Controllers
{
    public class PointsCalculatorController : Controller
    {
        public ActionResult PRCalculator()
        {


            return View("PRCalculator");
        }
        [HttpPost]
        public ActionResult PRCalculator(PointsCalculatorModel prcalc)
        {
            int selectedAge = Convert.ToInt32(Request["ageID"]);
            int selectedVisaType = Convert.ToInt32(Request["visaID"]);
            int engLanguageType = Convert.ToInt32(Request["engID"]);
            int ozEdTypeLowGrowth = Convert.ToInt32(Request["LowGrowth"]);
            int qualiType = Convert.ToInt32(Request["qualiID"]);
            int oexpType = Convert.ToInt32(Request["oexpID"]);
            int ozexpType = Convert.ToInt32(Request["ozexpID"]);
            int transType = Convert.ToInt32(Request["translatorID"]);
            int professionalEdu = Convert.ToInt32(Request["profID"]);




            switch (selectedAge)
            {
                case 0:
                    prcalc.score = prcalc.score + 0;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 25;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 30;
                    break;
                case 3:
                    prcalc.score = prcalc.score + 25;
                    break;
                case 4:
                    prcalc.score = prcalc.score + 15;
                    break;
                case 5:
                    prcalc.score = prcalc.score + 0;
                    break;
                case 6:
                    prcalc.score = prcalc.score + 0;
                    break;

            }

            switch (selectedVisaType)
            {
                case 0:
                    prcalc.score = prcalc.score + 0;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 5;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 10;
                    break;

            }
            switch (engLanguageType)
            {
                case 0:
                    prcalc.score = prcalc.score + 0;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 10;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 20;
                    break;

            }
            if (prcalc.australianEdu)
            {
                prcalc.score = prcalc.score + 5;
                switch (ozEdTypeLowGrowth)
                {
                    case 0:
                        prcalc.score = prcalc.score + 5;
                        break;
                    case 1:
                        prcalc.score = prcalc.score + 0;
                        break;


                }
                switch (professionalEdu)
                {
                    case 0:
                        prcalc.score = prcalc.score + 5;
                        break;
                    case 1:
                        prcalc.score = prcalc.score + 0;
                        break;


                }

            }

            switch (qualiType)
            {
                case 0:
                    prcalc.score = prcalc.score + 20;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 15;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 10;
                    break;
                case 3:
                    prcalc.score = prcalc.score + 0;
                    break;

            }
            switch (oexpType)
            {
                case 0:
                    prcalc.score = prcalc.score + 15;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 10;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 5;
                    break;
                case 3:
                    prcalc.score = prcalc.score + 0;
                    break;

            }
            switch (ozexpType)
            {
                case 0:
                    prcalc.score = prcalc.score + 20;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 15;
                    break;
                case 2:
                    prcalc.score = prcalc.score + 10;
                    break;
                case 3:
                    prcalc.score = prcalc.score + 5;
                    break;
                case 4:
                    prcalc.score = prcalc.score + 0;
                    break;

            }

            if (prcalc.partnerEngProf && prcalc.partnerLessthan50 && prcalc.partnerSkilled)
            {
                prcalc.score = prcalc.score + 5;
            }
            switch (transType)
            {
                case 0:
                    prcalc.score = prcalc.score + 5;
                    break;
                case 1:
                    prcalc.score = prcalc.score + 0;
                    break;
            }


            return View("PRCalculatorResult", prcalc);
        }
    }
}