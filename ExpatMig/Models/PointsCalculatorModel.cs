using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class PointsCalculatorModel
    {
        public bool australianEdu { get; set; }

        public bool partnerLessthan50 { get; set; }
        public bool partnerEngProf { get; set; }
        public bool partnerSkilled { get; set; }
        public int score { get; set; }
    }
}