﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ExpatMig.ViewModels
{
    public class ChatViewModel
    {
        public int TopicID { get; set; }
        public int ThreadID { get; set; }

        public String Description { get; set; }
        public int CreatedBy { get; set; }

        public DateTime CreatedDate { get; set; }

        public String AuthorName { get; set; }
        public int UserDeviceID { get; set; }
        public Boolean IsAndroid { get; set; }
        public string AttachmentURL { get; set; }
        public string AttachmentType { get; set; }
        public string ThreadName { get; set; }
    }
}