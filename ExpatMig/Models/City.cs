using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class City
    {
        [Key]
        public int CityID { get; set; }
        public string Description { get; set; }


        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int? ModifiedBy { get; set; }
        public DateTime? ModifiedDate { get; set; }


        //public virtual ICollection<UserProfile> MigratingTo { get; set; }

        //public virtual ICollection<UserProfile> NativeCity{ get; set; }

        //public virtual ICollection<TravelLog> TravelingTo { get; set; }

    }
}