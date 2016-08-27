using ExpatMig.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace ExpatMig.ViewModels
{
    public class UserProfileViewModel
    {
        private UserProfile userProfile;

        public UserProfileViewModel()
        {

        }
        public UserProfileViewModel(UserProfile userProfile)
        {
            this.BirthDay = userProfile.BirthDay;
            this.Experience = userProfile.Experience;
            this.FirstName = userProfile.FirstName;
            this.LastName= userProfile.LastName;
            this.LinkedIn = userProfile.LinkedIn;
            this.MigratingToID = userProfile.MigratingToID;
            this.NativeCityID= userProfile.NativeCityID;
            this.Nick = userProfile.Nick;
            this.PhoneNumber= userProfile.PhoneNumber;
            this.Sector= userProfile.Sector;
            this.Suburb = userProfile.Suburb;
            this.UserProfileID= userProfile.UserProfileID;
            this.VisaGrantOn= userProfile.VisaGrantOn;
            this.VisaType= userProfile.VisaType;

        }

        [Key]
        public int UserProfileID { get; set; }


        public String Nick { get; set; }
        [Display(Name = "First Name")]
        public String FirstName { get; set; }
        [Display(Name = "Last Name")]
        public String LastName { get; set; }
        [Display(Name = "Phone Number")]
        public String PhoneNumber { get; set; }
        [Display(Name = "Birth Day")]
        [DisplayFormat(DataFormatString = "{0:dd-MMM-yy}", ApplyFormatInEditMode = true)]
        public DateTime? BirthDay { get; set; }
        [Display(Name = "City - Native")]
        public int NativeCityID { get; set; }
        [Display(Name = "City - Migrating")]
        public int? MigratingToID { get; set; }
        public double? Experience { get; set; }
        public String Sector { get; set; }
        public String LinkedIn { get; set; }
        [Display(Name = "Visa Type")]
        public String VisaType { get; set; }
        [DisplayFormat(DataFormatString = "{0:dd-MMM-yy}", ApplyFormatInEditMode = true)]
        [Display(Name = "Visa Granted On")]
        public DateTime? VisaGrantOn { get; set; }
        public string Suburb { get; set; }

        public virtual ICollection<TravelLog> MyTravels { get; set; }

        [ForeignKey("NativeCityID")]
        public virtual City NativeCity { get; set; }

        [ForeignKey("MigratingToID")]
        public virtual City MigratingTo { get; set; }
    }
}