using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class TravelLog
    {
        [Key]
        public int TravelLogID { get; set; }
        public int UserProfileID { get; set; }

        public string FlightPlanned { get; set; }

        public int? TravelingToID { get; set; }
        public Decimal? FlightCost { get; set; }
        public string FlightBookedWebsite { get; set; }
        public DateTime? ExpectedMoveOn { get; set; }
        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int ModifiedBy { get; set; }
        public DateTime ModifiedDate { get; set; }


        [ForeignKey("TravelingToID")]
        public virtual City TravelingTo { get; set; }

        [ForeignKey("UserProfileID")]
        public UserProfile MyUserProfile { get; set; }
    }
}