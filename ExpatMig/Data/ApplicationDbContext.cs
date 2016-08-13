using ExpatMig.Models;
using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace ExpatMig.Data
{
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser, CustomRole,
      int, CustomUserLogin, CustomUserRole, CustomUserClaim>
    {
        public ApplicationDbContext()
            : base("DefaultConnection")
        {
            Configuration.ProxyCreationEnabled = false;
        }

        public static ApplicationDbContext Create()
        {
            return new ApplicationDbContext();
        }


        //  *************       Dbsets  ****************
        public DbSet<Group> Groups { get; set; }
        public DbSet<Thread> Threads { get; set; }
        public DbSet<Topic> Topics { get; set; }

        public DbSet<UserProfile> UserProfiles { get; set; }
        public DbSet<UserDevice> UserDevices { get; set; }

        public DbSet<TravelPlans> TravelPlans { get; set; }
        public System.Data.Entity.DbSet<ExpatMig.Models.City> Cities { get; set; }
    }
}