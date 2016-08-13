using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class TravelPlans
    {
 
        [Key]
        public int TravelPlanID { get; set; }

        public DateTime ExpectedMoveOn { get; set; }
        [MaxLength(250)]
        public String FlightPlanned { get; set; }
        [MaxLength(100)]
        public String FlightCost { get; set; }
        public String FlightBookedWebsite { get; set; }
    
        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int? ModifiedBy { get; set; }
        public DateTime? ModifiedDate { get; set; }
      
    

        
    }
}