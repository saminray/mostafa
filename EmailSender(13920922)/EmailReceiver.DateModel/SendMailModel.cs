using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using MailInbox.DataModel;

namespace MailInbox.DataModel
{
    public class SendMailModel :BaseModel<byte> 
{
         /// <summary>
        ///جدول ایمیل های ارسالی
        /// </summary>
        public byte? ID { get; set; }

        /// <summary>
        ///شماره مشتری
        /// </summary>
        public long? CustomerID { get; set; }

        /// <summary>
        ///متن ایمیل
        /// </summary>
        public string EmailText { get; set; }

        /// <summary>
        ///آدرس ایمیل مشتری
        /// </summary>
        public string CustomerEmailAddress { get; set; }

        /// <summary>
        ///عنوان ایمیل
        /// </summary>
        public string Subject { get; set; }

        /// <summary>
        ///
        /// </summary>
        public byte? EmailType { get; set; }

        /// <summary>
        ///شماره حساب مشتری
        /// </summary>
        public string DepositNumber { get; set; }

        /// <summary>
        ///وضعیت ایمیل 
        ///ارسال شده 1 ; fail :0
        /// </summary>
        public byte? mailstatus { get; set; }

        /// <summary>
        ///خطاهای موجود در ارسال ایمیل
        /// </summary>
        public string Error { get; set; }

      


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