using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Mail;
using System.Net;
using System.Threading;
using System.Configuration;
using SBS.CommonUtils;
using System.Web.Security;
using SBS.DataModel;
using OpenPop.TestApplication;
using Microsoft.Win32;
using System.Globalization;
using MailInbox.DataModel;
using MailInbox.DAL;


namespace EmailSender
{
    class EmailBankCore
    {
        public static UserInfo LoggedInUserInfo { get; set; }
        public static UserInfo ReportLoggedInUserInfo { get; set; }
        SBS.FacadeServiceContract.IWithdrawalsSettlementManager iWithdrawals;
        SBS.FacadeServiceContract.ICustomerManager iCustomer;
        static bool CurrentlyInUse = false;
        string UserName = "";
        string PassWord = "";
        System.Threading.Timer CheckTimer;
        System.Threading.Timer CheckTEmailRecivetimer;
        //public bool EmailReciveCheckerISbusy = false;
        TestForm frtes;
        public void EmailBankCoreMethod(string pUsername, string pPassword)
        {
            UserName = pUsername;
            PassWord = pPassword;
            if (!Login(pUsername, pPassword))
            {
                Status = false;
                return;
            }
            else
            {
                Status = true;
            }
            iCustomer = SBS.CommonUtils.FacadeFactory.CustomerManager("", LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, LoggedInUserInfo.TicketStr);
            iWithdrawals = SBS.CommonUtils.FacadeFactory.WithdrawalsSettlementManager("", LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, LoggedInUserInfo.TicketStr);
        }
        public EmailBankCore(string pUsername,string pPassword)
        {
            frtes = new TestForm(ConfigurationManager.AppSettings["EmailAddress"].ToString(), ConfigurationManager.AppSettings["EmailPassword"].ToString(), ConfigurationManager.AppSettings["SMTPServerAddress"].ToString());
            frtes.ShowDialog();
            EmailBankCoreMethod(pUsername, pPassword);            
            CheckTimer = new System.Threading.Timer(this.EmailChecker, this, TimeSpan.FromSeconds(20), TimeSpan.FromSeconds(60));
            CheckTEmailRecivetimer = new System.Threading.Timer(this.EmailReciveChecker, this, TimeSpan.FromSeconds(10), TimeSpan.FromSeconds(60));
        }
            
        public static string HashIt(string pData)
        {
            string ret = "";
            ret = FormsAuthentication.HashPasswordForStoringInConfigFile(pData, "SHA1");
            ret = FormsAuthentication.HashPasswordForStoringInConfigFile(ret, "MD5");
            ret = FormsAuthentication.HashPasswordForStoringInConfigFile(ret, "SHA1");
            return ret;

        }
               
        struct MailInformation
        {
           public long CustomerID;
           public string EmailText;
           public EmailsenderClass MailProperties;
           public string CustomerEmailAddress;
           public string Subject;
           public string EmailType;
           public string ErrorText;
           public string DepositeNumber;
        }

        private static bool Login(string user, string pass)
        {
            try
            {              
                if (SBS.CommonUtils.FacadeFactory.InitSecurityContex() == false)
                {
                    Console.WriteLine("Can Not Find Server ");
                }
                string strError = "";
                LoggedInUserInfo = FacadeFactory.SecurityManager.SignOn(user, HashIt(pass), ref strError);
                if (LoggedInUserInfo == null)
                {
                    if (!string.IsNullOrEmpty(strError))
                    {
                        Console.WriteLine("0");
                    }
                    else
                    {
                        Console.WriteLine("Login Faild at :" + FarsiDateServer() + "_" + ClockServer());
                    }
                    return false;
                }
                else
                {
                    Console.Clear();
                    Console.WriteLine("Start Email Sending at :" + FarsiDateServer() + "_" + ClockServer());
                    Console.WriteLine("Email Sendig...");
                    ReportLogin(user, pass);
                    return true;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Login Error");
                LogRegister("Login", ex.Message);
            }
            return false;
        }

        private static bool ReportLogin(string user, string pass)
        {
            try
            {
               SBS.FacadeServiceContract.ISSOService ISSO= SBS.CommonUtils.FacadeFactory.InitSecurityContex(SBS.CommonUtils.Properties.Resources.SecondServerIP) ;
                if (ISSO== null)
                {
                    Console.WriteLine("Can Not Find Server ");
                }
                string strError = "";
                ReportLoggedInUserInfo = ISSO.SignOn(user, HashIt(pass), ref strError);
                if (ReportLoggedInUserInfo == null)
                {
                    if (!string.IsNullOrEmpty(strError))
                    {
                        Console.WriteLine("0");
                    }
                    else
                    {
                        Console.WriteLine("Login Faild at :" + FarsiDateServer() + "_" + ClockServer());
                    }
                    return false;
                }
                else
                {
                    Console.Clear();
                    Console.WriteLine("Start Email Sending at :" + FarsiDateServer() + "_" + ClockServer());
                    Console.WriteLine("Email Sendig...");
                    return true;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Login Error");
                LogRegister("Login", ex.Message);
            }
            return false;
        }
        
        private string getDate() 
        {
            try
            {
                PersianCalendar pc = new PersianCalendar();
                int year = pc.GetYear(DateTime.Now);
                string month = pc.GetMonth(DateTime.Now) + "";
                if (int.Parse(month) < 10)
                    month = "0" + month;
                string day = pc.GetDayOfMonth(DateTime.Now) + "";
                if (int.Parse(day) < 10)
                    day = "0" + day;
                return year + "/" + month + "/" + day;
            }
            catch (Exception ex)
            {
                throw ex;
            }              
        }

        long LastProccesID = 0;
        private long getLastID() 
        { 
            try
            {
                string ID = Microsoft.Win32.Registry.GetValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\SBS\\EmailSender", "LastProccesID", LastProccesID).ToString();
                if (ID != null && ID != "")
                {
                    LastProccesID = long.Parse(ID);
                }
                return LastProccesID;
            }catch(Exception ex)
            {
                return LastProccesID;
            }              
        }
        
        private void setLastID(long LastProccesID)
        {
            Microsoft.Win32.Registry.SetValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\SBS\\EmailSender", "LastProccesID", LastProccesID);
        }
        
        public void EmailBiulder()
        {
            long CustomerID = 0;
            try
            {
                string text = "";
                EmailsenderClass mail = new EmailsenderClass();
                mail.UserName ="\"EmailBank@OmideJelin.ir\"<"+ConfigurationManager.AppSettings["EmailAddress"].ToString()+">";
                mail.Password = ConfigurationManager.AppSettings["EmailPassword"].ToString();
                List<SBS.DataModel.CustomerModel> SDCustomermodel = new List<CustomerModel>();
               // setLastID(0);
                try
                {
                    SDCustomermodel = iWithdrawals.SelectForNotifyEmail(getLastID(), getDate());
                }
                catch (Exception)
                {
                    Thread.Sleep(10000);
                    EmailBankCoreMethod(UserName, PassWord);
                    EmailBiulder();
                    return;
                }
                foreach (SBS.DataModel.CustomerModel CustomerModelRow in SDCustomermodel)
                {
                    UserInformation UserInf ;
                    try
                    {
                        
                       CustomerID = CustomerModelRow.ID.Value;
                       UserInf = new UserInformation
                       {
                           PreName = "",
                           Name = CustomerModelRow.DisplayName,
                           ActionType = CustomerModelRow.Moaref + " سپرده ",
                           DepositNubmer = CustomerModelRow.DepositName,
                           Amount = CustomerModelRow.Amount.ToString(),
                           Comment = CustomerModelRow.Comment,
                           Date = CustomerModelRow.Date,
                           RemainAmount = CustomerModelRow.RemainAmount.ToString(),
                           Time = CustomerModelRow.Clock, IsTransaction=true
                       };
                       UserInf.PreName = "";
                        PageCreator Page = new PageCreator();
                        text = Page.PageBuilder(UserInf, ReportDetailsBuilder(UserInf.DepositNubmer,5));
                    }
                    catch (Exception ex)
                    {
                        UserInf = new UserInformation(); 
                        //iWithdrawals.UpdateForNotifyErrorSent(CustomerID);
                    }
                    MailInformation Mailinfo = new MailInformation { CustomerEmailAddress = CustomerModelRow.Email, CustomerID = CustomerID, EmailText = text, MailProperties = mail, Subject = UserInf.ActionType, EmailType="1", DepositeNumber=UserInf.DepositNubmer };
                    if (CustomerID != 0)
                        setLastID(CustomerID);
                    CreateEmailThread(Mailinfo);                   
                }
                
                CurrentlyInUse = false;
            }
            catch (Exception ex)
            {
                CurrentlyInUse = false;
                LogRegister("EmailBiulder", ex.Message);
            }
        }

        private List<ReportDetails> ReportDetailsBuilder(string DepositNumber,int Count)
        {
            try
            {
                if (Count>50)
                {
                    Count = 50;
                }
                //SBS.FacadeServiceContract.IDepositInaugurationManager IDeposit = SBS.CommonUtils.FacadeFactory.DepositInaugurationManager("", LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, LoggedInUserInfo.TicketStr);
                SBS.FacadeServiceContract.IDepositInaugurationManager IDeposit = SBS.CommonUtils.FacadeFactory.DepositInaugurationManager(SBS.CommonUtils.Properties.Resources.SecondServerIP, LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, ReportLoggedInUserInfo.TicketStr);
                string[] DepositInfirmation = DepositNumber.ToString().Split('-');
                string Reportstring = IDeposit.GetLastFundFlowDepositInauguration(DepositInfirmation[0].ToString(), DepositInfirmation[1].ToString(), DepositInfirmation[2].ToString(), DepositInfirmation[3].ToString(),Count);
                return Spliter(Reportstring);
            }
            catch (Exception ex)
            {
                LogRegister("ReportDetailsBuilder", ex.Message);
                return new List<ReportDetails>();
            }
        }

        private  List<ReportDetails> Spliter(string ReportString)
        {
            try
            {
                List<ReportDetails> ReturnList = new List<ReportDetails>(); 
                string[] str=ReportString.Split(';');
                for (int i = 0; i < str.Length-8; i+=8)
                {
                    ReportDetails rep = new ReportDetails
                    {
                        RowNumber = (i / 8 + 1).ToString(),
                        Amount = str[i + 2].ToString(),
                        Date = str[i + 3].ToString(),
                        Time = str[i + 4].ToString(),
                        RemainAmount = str[i + 5].ToString(),
                        Comment = str[i + 6].ToString(),
                        BranchName = str[i + 7].ToString()
                    };
                    ReturnList.Add(rep);
                }
                return ReturnList;
            }
            catch (Exception)
            {
                return new List<ReportDetails>();           
            }
        }

        void EmailChecker(object State)
        {
            try
            {
                if (!CurrentlyInUse)                
                {
                    CurrentlyInUse = true;
                    EmailBiulder();    
                }
                
            }
            catch (Exception ex)
            {
                LogRegister("EmailChecker", ex.Message);
            }
        }

        void EmailReciveChecker(object State)
        {
            try
            {
                Console.WriteLine("emailCheckOn : " + EmailBankCore.ClockServer());
                TestForm.messageBody[] eBankMessage=new TestForm.messageBody[TestForm.messageBodyList.Count];
                TestForm.messageBodyList.CopyTo(eBankMessage);
                if (!TestForm.EmailReciveCheckerISbusy && !TestForm.EmailAddCheckerISbusy)
                {
                    TestForm.EmailReciveCheckerISbusy = true;
                    foreach (TestForm.messageBody MessageRecived in eBankMessage)
                    {
                        Console.WriteLine("New Message Recived From : " + MessageRecived.MailSender );
                        EmailsenderClass mail = new EmailsenderClass();
                        mail.UserName = "\"EmailBank@OmideJelin.ir\"<" + ConfigurationManager.AppSettings["EmailAddress"].ToString() + ">";
                        mail.Password = ConfigurationManager.AppSettings["EmailPassword"].ToString();
                        //______________________________________________________________________________
                        long CustomerID = CustomerIDMining(MessageRecived.DepositeNubmer);
                        if (CustomerID == 0)
                        {
                            PageCreator PC = new PageCreator(); //"omidHaghighatbin@gmail.com"
                            string Page = PC.PageBuilder();
                            MailInformation Mailinfo = new MailInformation 
                            { 
                                CustomerEmailAddress = MessageRecived.MailSender, 
                                CustomerID = CustomerID, 
                                EmailText = Page, 
                                MailProperties = mail, 
                                Subject = "Error", 
                                EmailType ="2", 
                                DepositeNumber=MessageRecived.DepositeNubmer };
                                CreateEmailThread(Mailinfo);
                        }
                        else
                        {
                            SBS.DataModel.CustomerModel CustomerInfo;
                            int ii = 0;
                            while (true)
                            {
                                try
                                {
                                    CustomerInfo = iCustomer.SelectOneWithOutDeepLoad(CustomerID);
                                    break;
                                }
                                catch (Exception ex)
                                {
                                    ii++;
                                    if (ii>10)
                                    {
                                        return;
                                    }
                                    if (Login(UserName, PassWord))
                                    {
                                        iCustomer = SBS.CommonUtils.FacadeFactory.CustomerManager("", LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, LoggedInUserInfo.TicketStr);
                                        iWithdrawals = SBS.CommonUtils.FacadeFactory.WithdrawalsSettlementManager("", LoggedInUserInfo.UserIdentity.ToString(), (short)SBS.CommonUtils.ConstantData.AppUC.Email_SubSystem, LoggedInUserInfo.TicketStr);
                                    }
                                }                                
                            }
                            //CustomerInfo.Email = "Omidhaghighatbin@gmail.com";
                            //CustomerInfo.Email = "mmj@saminray.com";
                            //CustomerInfo.Email = "mmjanzadeh@gmail.com";
                            //______________________________________________________________________________
                            if (!string.IsNullOrEmpty(CustomerInfo.Email))
                            {
                                PageCreator PC = new PageCreator(); //"omidHaghighatbin@gmail.com"   
                                UserInformation UserInf = new UserInformation
                                {
                                    PreName = "",
                                    Name = CustomerInfo.DisplayName,
                                    ActionType = "گردش سپرده",
                                    DepositNubmer = MessageRecived.DepositeNubmer,
                                    Amount = CustomerInfo.Amount.ToString(),
                                    Comment = CustomerInfo.Comment,
                                    Date =FarsiDateServer(),
                                    RemainAmount = CustomerInfo.RemainAmount.ToString(),
                                    Time = ClockServer(), 
                                    IsTransaction=false,                                    
                                };
                                if (MessageRecived.TransactionCountRequested <= 200)
                                    UserInf.TransactionCountRequested= MessageRecived.TransactionCountRequested;
                                else
                                    UserInf.TransactionCountRequested = 200;
                                if (MessageRecived.Blocked)
                                {   
                                    string Page = PC.PageBuilder(UserInf);
                                    MailInformation Mailinfo = new MailInformation
                                    {
                                        CustomerEmailAddress = CustomerInfo.Email,
                                        CustomerID = CustomerID,
                                        EmailText = Page,
                                        MailProperties = mail,
                                        Subject = "بیش از حد مجاز",
                                        EmailType = "2",
                                        DepositeNumber = MessageRecived.DepositeNubmer
                                    };
                                    CreateEmailThread(Mailinfo);
                                }
                                else
                                {
                                    List<ReportDetails> LRep = ReportDetailsBuilder(MessageRecived.DepositeNubmer, MessageRecived.TransactionCountRequested);
                                    UserInf.RealTransactionCount = short.Parse(LRep.Count.ToString());
                                    string Page = PC.PageBuilder(UserInf, LRep);
                                    MailInformation Mailinfo = new MailInformation
                                    {
                                        CustomerEmailAddress = CustomerInfo.Email,
                                        CustomerID = CustomerID,
                                        EmailText = Page,
                                        MailProperties = mail,
                                        Subject = "گردش سپرده",
                                        EmailType = "2",
                                        DepositeNumber = MessageRecived.DepositeNubmer
                                    };
                                    CreateEmailThread(Mailinfo);
                                }
                            }
                        }
                        while (true)
                        {
                            if ( !TestForm.EmailAddCheckerISbusy)
                            {
                                TestForm.EmailAddCheckerISbusy = true;
                                TestForm.messageBodyList.Remove(MessageRecived);
                                TestForm.EmailAddCheckerISbusy = false;
                                break;
                            }
                        }
                    }
                    TestForm.EmailReciveCheckerISbusy = false;
                }                    
            }

            catch (Exception ex)
            {
                LogRegister("EmailReciveChecker", ex.Message);
            
            }
        }

        long CustomerIDMining(string DepositNumber)
        {
            try
            {
                long ID=0;
                string[] S= DepositNumber.Split('-');
                if(S.Length >2)
                    if (!string.IsNullOrEmpty(S[2]))
                    {
                        ID = long.Parse(S[2]);        
                    }                
                return ID;
            }
            catch (Exception ex)
            {
                return 0;    
            }
        }
               
        public static void LogRegister(string Source,string Message)
        {
            try
            {
                System.Diagnostics.EventLog.WriteEntry(Source, Message);
            }
            catch (Exception)
            {              
            }
            
        }

        void CreateEmailThread(MailInformation MailInfo)
        {            
            try
            {                
                ParameterizedThreadStart PTRS = new ParameterizedThreadStart(EmailSendInBackground);
                Thread trMailSender = new Thread(PTRS);                
                trMailSender.Start(MailInfo);
            }
            catch (Exception)
            {
               // iWithdrawals.UpdateForNotifyErrorSent(MailInfo.CustomerID);
            }
        }

        private void Insert(SendMailModel sendModel, MailInformation Mailinfo, SendMailDO sendmailDO)
        {
            try
            {
                sendModel.CustomerID = Mailinfo.CustomerID;
                sendModel.EmailText = Mailinfo.EmailText;
                sendModel.DepositNumber = Mailinfo.DepositeNumber;
                sendModel.CustomerEmailAddress = Mailinfo.CustomerEmailAddress;
                sendModel.EmailType = Mailinfo.EmailType;
                sendModel.Subject = Mailinfo.Subject;
                sendModel.SendDate = FarsiDateServer();
                sendModel.SendTime = ClockServer();
                sendmailDO.Insert(sendModel, true);
            }catch(Exception ex){}        
        }

        void EmailSendInBackground(object MailInf)
        {            
            SendMailDO sendmailDO = new SendMailDO();
            SendMailModel sendModel = new SendMailModel();
            MailInformation MailInform = (MailInformation)MailInf;
            try
            {
                //MailInform.CustomerEmailAddress = "shrsw342@gmail.com";
               //MailInform.CustomerEmailAddress = "Omidhaghighatbin@gmail.com";
                string ErrorMessage = "";
                if (MailInform.MailProperties.EmailSender(MailInform.MailProperties.UserName, MailInform.CustomerEmailAddress, MailInform.Subject, MailInform.EmailText, true, 0, ref ErrorMessage))
                {
                    sendModel.Status =true;                   
                    sendModel.Error = string.Empty;
                    Insert(sendModel, MailInform, sendmailDO);
                    Console.WriteLine( "Deposite Number : " + MailInform.DepositeNumber + " Message send to : " + MailInform.CustomerEmailAddress + " , Success");
                } 
                else
                {
                    sendModel.Status = false;
                    sendModel.Error = ErrorMessage;
                    Insert(sendModel, MailInform, sendmailDO);
                    Console.WriteLine("Deposite Number : " + MailInform.DepositeNumber + " Message Dont send to : " + MailInform.CustomerEmailAddress + " , Error : " +ErrorMessage);
                }                            
            }
            catch (Exception exp)
            {
                LogRegister("EmailSendInBackground", exp.Message);
            }
        }      
        public static string FarsiDateServer()
        {
            try
            {

                DateTime Time1 = DateTime.Now;
                System.Globalization.PersianCalendar PC = new System.Globalization.PersianCalendar();
                string year = PC.GetYear(Time1).ToString(); if (year.Length == 1) year = "0" + year;
                string month = PC.GetMonth(Time1).ToString(); if (month.Length == 1) month = "0" + month;
                string day = PC.GetDayOfMonth(Time1).ToString(); if (day.Length == 1) day = "0" + day;
                year = year.Remove(0, 2);
                return year + "/" + month + "/" + day;
            }
            catch (Exception exp)
            {
                throw new Exception(exp.Message);
            }
        }
        public static string ClockServer()
        {
            try
            {
                DateTime Time1 = DateTime.Now;
                System.Globalization.PersianCalendar PC = new System.Globalization.PersianCalendar();
                string hour = PC.GetHour(Time1).ToString(); if (hour.Length == 1) hour = "0" + hour;
                string minute = PC.GetMinute(Time1).ToString(); if (minute.Length == 1) minute = "0" + minute;
                string second = PC.GetSecond(Time1).ToString(); if (second.Length == 1) second = "0" + second;
                return hour + ":" + minute + ":" + second;
            }
            catch (Exception exp)
            {
                throw new Exception(exp.Message);
            }
        }
        public bool Status { get; set; }

        
    }
}
