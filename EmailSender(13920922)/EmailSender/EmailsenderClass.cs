using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Mail;
using System.Net;


namespace EmailSender
{

    public class SMTPProperty
    {
        public SmtpClient SMTP {get; set;}
        public bool ISbusy { get; set; }
        public long TTL { get; set; }
    }
    class EmailsenderClass
    {
        /// <summary>
        /// Email Address OR SMTP server Username 
        /// </summary>
        public string UserName { get; set; }
        /// <summary>
        /// Email Password OR SMTP server Password
        /// </summary>
        public string Password { get; set; }
        /// <summary>
        ///SMTP Server Address Or name For Connect
        /// </summary>
        public string SMTPserverAddress { get; set; }
        /// <summary>
        ///SMTP Server Address Or name For Connect
        /// </summary>
        public int Port { get; set; }
        string CridentionalUserName { get; set; }
        public EmailsenderClass()
        {
            Port =int.Parse( System.Configuration.ConfigurationSettings.AppSettings["Port"].ToString());
            SMTPserverAddress = System.Configuration.ConfigurationSettings.AppSettings["SMTPServerAddress"].ToString();
            CridentionalUserName = System.Configuration.ConfigurationSettings.AppSettings["EmailAddress"].ToString();
            //SMTPserverAddress = "mail.omidejelin.ir";
        }
        
        public bool EmailSender(string From, string To,string Subject,string Body,bool IsBodyHTML,int RepeatCount,ref string ErrorMessage)
        {
            int c = RepeatCount + 1;
            SMTPProperty smtpprop = new SMTPProperty();
            try
            {
                if (RepeatCount <= 3)
                {
                    smtpprop = GetActiveSmtpClient();
                    MailMessage mail = new MailMessage();
                    mail.From = new MailAddress(From);
                    mail.To.Add(To);
                    mail.Subject = Subject;
                    mail.Body = Body;
                    mail.IsBodyHtml = IsBodyHTML;
                    smtpprop.ISbusy = true;
                    smtpprop.SMTP.Send(mail);
                    smtpprop.ISbusy = false;
                    ErrorMessage = "";
                    return true;

                    //SmtpClient smtp = new SmtpClient(SMTPserverAddress, Port);
                    //smtp.Timeout = 1000000;
                    //smtp.EnableSsl = false;
                    //smtp.UseDefaultCredentials = false;
                    //smtp.Credentials = new NetworkCredential(CridentionalUserName, Password);
                    //MailMessage mail = new MailMessage();
                    //mail.From = new MailAddress(From);
                    //mail.To.Add(To);
                    //mail.Subject = Subject;
                    //mail.Body = Body;
                    //mail.IsBodyHtml = IsBodyHTML;
                    //smtp.Send(mail);
                    //ErrorMessage = "";
                    //smtp.Dispose();
                    //return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception Ex)
            {
                EmailBankCore.LogRegister("EmailSender To : " + To , Ex.Message);
                //smtpprop.ISbusy = false;
                ErrorMessage = Ex.Message;
                System.Threading.Thread.Sleep(1000);
                bool bl=EmailSender(From, To, Subject, Body, IsBodyHTML,c,ref ErrorMessage);
                return bl;
            }
        }

        static List<SMTPProperty> SMTPList = new List<SMTPProperty>();

        public SmtpClient GetNewSMTP()
        {
            try
            {
                SmtpClient smtp = new SmtpClient(SMTPserverAddress, Port);
                smtp.Timeout = 1000000;
                smtp.EnableSsl = false;
                smtp.UseDefaultCredentials = false;
                smtp.Credentials = new NetworkCredential(CridentionalUserName, Password);
                return smtp;
            }
            catch (Exception ex)
            {
                EmailBankCore.LogRegister("GetNewSMTP", ex.Message);
                SmtpClient smtp = new SmtpClient(SMTPserverAddress, Port);
                smtp.Timeout = 1000000;
                smtp.EnableSsl = false;
                smtp.UseDefaultCredentials = false;
                smtp.Credentials = new NetworkCredential(CridentionalUserName, Password);
                return smtp;
            }
        }

        public SMTPProperty GetActiveSmtpClient() 
        {
            try
            {
                if (SMTPList.Count <= 0)
                {
                    SmtpClient smtp = GetNewSMTP();
                    SMTPProperty smtpp = new SMTPProperty { SMTP = smtp, ISbusy = false, TTL = Environment.TickCount };
                    SMTPList.Add(smtpp);
                    return SMTPList[SMTPList.Count - 1];
                }
                else
                {
                    int i = 0;
                    while (SMTPList[i].ISbusy)
                    {
                        i++;
                        if (i >= SMTPList.Count)
                        {
                            if (i <= 50)
                            {
                                SmtpClient smtp = GetNewSMTP();
                                SMTPProperty smtpp = new SMTPProperty { SMTP = smtp, ISbusy = false, TTL = Environment.TickCount };
                                SMTPList.Add(smtpp);
                                return SMTPList[SMTPList.Count - 1];
                            }
                            else
                            {
                                System.Threading.Thread.Sleep(1000);
                                return GetActiveSmtpClient();
                            }
                        }
                    }
                    long Tick = Environment.TickCount;
                    if (( - SMTPList[i].TTL) > 1000000)
                    {
                        SmtpClient smtp = GetNewSMTP();
                        SMTPProperty smtpp = new SMTPProperty { SMTP = smtp, ISbusy = false, TTL = Environment.TickCount };
                        SMTPList[i] = smtpp;
                    }
                    return SMTPList[i];
                }
            }
            catch (Exception ex)
            {
                EmailBankCore.LogRegister("GetActiveSmtpClient", ex.Message);
                SmtpClient smtp = new SmtpClient(SMTPserverAddress, Port);
                smtp.UseDefaultCredentials = false;
                smtp.EnableSsl = false;
                smtp.Credentials = new NetworkCredential(CridentionalUserName, Password);
                SMTPProperty smtpp = new SMTPProperty { SMTP = smtp, ISbusy = false, TTL = Environment.TickCount };
                return smtpp;
            }
            
        }

        public bool GmailSender(string From, string To, string Subject, string Body,bool IsBodyHTML)
        {
            try
            {
                MailMessage mail = new MailMessage();
                mail.From = new MailAddress(From);
                mail.To.Add(To);
                mail.Subject = Subject;
                mail.Body = Body;
                mail.IsBodyHtml = IsBodyHTML;
                SmtpClient smtp = new SmtpClient(From, 587);
                smtp.UseDefaultCredentials = false;
                smtp.EnableSsl = true;
                smtp.Credentials = new NetworkCredential(UserName, Password); 
                //smtp.Credentials = new NetworkCredential("ms.shafeian@gmail.com", "m8211106756");
                smtp.Timeout = 3000;
                smtp.Send(mail);
                smtp.Dispose();
                return true;
            }
            catch (Exception Ex)
            {
                EmailBankCore.LogRegister("GmailSender", Ex.Message);
                //System.Diagnostics.EventLog.WriteEntry(To, Ex.ToString());
                return false;             
            }
        }
        
    }
}
