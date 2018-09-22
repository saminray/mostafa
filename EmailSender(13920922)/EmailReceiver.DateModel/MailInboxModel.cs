using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using MailInbox.DataModel;

namespace MailInbox.DataModel
{
    public class MailInboxModel :BaseModel<byte> 
{
         /// <summary>
        ///
        /// </summary>
        public byte? ID { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string MessageHeader { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string MessageBody { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string MessageUID { get; set; }

        /// <summary>
        ///
        /// </summary>
        public bool? Read { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string SenderMail { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string receiveDate { get; set; }

        /// <summary>
        ///
        /// </summary>
        public string receiveTime { get; set; }


        public override byte PKValue
        {
            get
            {
                return ID.Value;
            }
            set
            {
                ID = value;
            }
        }
     }
}