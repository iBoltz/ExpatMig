using ExpatMig.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ExpatMig.ViewModels
{
    public class GroupViewModel
    {
        public Group ParentGroup;
        public List<Thread> ChildThreads;
    }
}