using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace ExpatMig.Models
{
    public class DeviceType
    {
        [Key]
        public int DeviceTypeID { get; set; }
        public string Description { get; set; }

        public bool IsActive { get; set; }
        public int SeqNo { get; set; }
        public int CreatedBy { get; set; }
        public DateTime CreatedDate { get; set; }
        public int? ModifiedBy { get; set; }
        public DateTime? ModifiedDate { get; set; }

        public virtual ICollection<UserDevice> UserDevices { get; set; }

    }
}