using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ExpatMig.Models
{
    public class UserDevice
    {
        [Key]
        public int UserDeviceID { get; set; }
        public int UserID { get; set; }
        public string ApiRegistrationID { get; set; }
        public string DeviceID { get; set; }
        public string AppVersion { get; set; }

        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int? ModifiedBy { get; set; }
        public DateTime? ModifiedDate { get; set; }

        public virtual ApplicationUser User { get; set; }


    }
}