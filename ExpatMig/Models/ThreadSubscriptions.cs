using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class ThreadSubscription
    {

        [Key]
        public int ThreadSubscriptionID { get; set; }
        public int ThreadID { get; set; }       
        public int UserID { get; set; }
        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int? ModifiedBy { get; set; }
        public DateTime? ModifiedDate { get; set; }
        public virtual Thread MyThread { get; set; }
        public virtual ApplicationUser User { get; set; }

    }
}