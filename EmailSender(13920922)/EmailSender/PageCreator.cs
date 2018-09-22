using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace EmailSender
{
    class PageCreator
    {        
        private string HTMLPage { get; set; }

        public string PageBuilder(UserInformation UserInfo, List<ReportDetails> RepDetails)
        {
            try
            {
                HtmlPageHeader();
                AdduserText(UserInfo);
                AddTable(RepDetails);
                Htmlpagefooter();
                return HTMLPage;
            }
            catch (Exception)
            {
                return "";
            }            
        }
        public string PageBuilder(UserInformation UserInfo)
        {
            try
            {
                HtmlPageHeader();
                //AdduserText(UserInfo);
                MaxMessage(UserInfo);
                Htmlpagefooter();
                return HTMLPage;
            }
            catch (Exception)
            {
                return "";
            }
        }
        public string PageBuilder(List<ReportDetails> RepDetails)
        {
            try
            {
                HtmlPageHeader();
                //AdduserText(new 
                AddTable(RepDetails);
                Htmlpagefooter();
                return HTMLPage;
            }
            catch (Exception)
            {
                return "";
            }
        }

        public string PageBuilder()
        {
            try
            {
                HtmlPageHeader();
                ErrorMessage();
                Htmlpagefooter();
                return HTMLPage;
            }
            catch (Exception)
            {
                return "";
            }
        }

        private void ErrorMessage()
        {
           HTMLPage += "<div style=\"height: 170px; direction: rtl;\" dir=\"rtl\">  <br />  <br /> "+
               "   با سلام مشتري گرامي درخواست شما به صورت نادرست ارسال شده است لطفا به صورت زير وارد نماييد : <br /> "+
               "    :تعداد گردش : شماره سپرده به صورت کامل    <br /> "+
               " aaaa-aa-aaaaaaa-aa : TransactionCount : <br />    تذکر1: تعداد گردش حداکثر 50 مي باشد   <br />   تذکر2: لطفا بعداز تعدادگردش : رادرج نمایید.    <br /> </div><br /> <div dir=\"rtl\">";
        }

        private void MaxMessage(UserInformation UserInfo)
        {
            string HTMLstring = "";
            try
            {
                string CustomeName = " مشتري گرامي  " + UserInfo.PreName + " " + UserInfo.Name;
                string Deposit = " به شماره " + UserInfo.DepositNubmer;
                string ErrorMax = "سقف تعداد درخواست های روزانه شما به پایان رسیده است و شما برای امروز نمی توانید بیش از 10 بار تقاضای گردش سپرده نمایید ";
                HTMLstring = "<div style=\"height: 170px; direction: rtl;\" dir=\"rtl\"> " +
                              " <br />  <br /> " +
                              "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + CustomeName + "<br /> " +
                              "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + Deposit +
                               ErrorMax + " </div>";
            }
            catch (Exception ex)
            {
            }
            HTMLPage += HTMLstring;
        }

        public void HtmlPageHeader()
        {
             HTMLPage = "<html>  <head>  <title>Logo</title>  " +
                              "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> </head> " +
                              "<body bgcolor=\"#FFFFFF\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\"> " +
                              "<!-- Save for Web Slices (Logo.psd) --> " + "<div dir=\"rtl\" style=\"height: 551px;  background-color: #D1D7DE\">" +
                              "<img src=\"http://omidejelin.ir/images/logo.png\" width=\"115\" height=\"109\" alt=\"\"> ";
        }

        public void Htmlpagefooter()
        {
            HTMLPage += "<!-- End Save for Web Slices --> </div> </body> </html>";            
        }


        public void AdduserText(UserInformation UserInfo)
        {
            string HTMLstring = "";
            try
            {
                if (UserInfo.IsTransaction)
                {
                    string CustomeName = " مشتري گرامي  " + UserInfo.PreName + " " + UserInfo.Name;
                    string Deposit = UserInfo.ActionType + " به شماره " + UserInfo.DepositNubmer;
                    string Date = "  در تاريخ " + UserInfo.Date + " ";
                    string Time = " در ساعت " + UserInfo.Time;
                    string Comment = " بابت " + UserInfo.Comment + " انجام شد.و موجودي سپرده مبلغ ";
                    string RemainAmount = "   " + UserInfo.RemainAmount + " ریال می باشد ";
                    HTMLstring = "<div style=\"height: 170px; direction: rtl;\" dir=\"rtl\"> " +
                                  " <br />  <br /> " +
                                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + CustomeName + "<br /> " +
                                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + Deposit +
                                  "&nbsp; به مبلغ&nbsp; " + UserInfo.Amount + " ريال&nbsp;&nbsp; " + Date + " &nbsp; " + Time +
                                  "<br /> " +
                                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + Comment + "   &nbsp; " +
                                  "" + RemainAmount + " </div>";
                }
                else
                {
                    string CustomeName = " مشتري گرامي  " + UserInfo.Name ;
                    string Deposit =" تعداد "+ UserInfo.TransactionCountRequested.ToString() +"  گردش آخر سپرده  به شماره  " + UserInfo.DepositNubmer +" درخواست داده شده است که تعداد   " + UserInfo.RealTransactionCount + " مورد در دسترس بوده  " ;
                    string Date = "  در تاريخ " + UserInfo.Date + " ";
                    string Time = " و ساعت " + UserInfo.Time;
                    HTMLstring = "<div style=\"height: 170px; direction: rtl;\" dir=\"rtl\"> " +
                                  " <br />  <br /> " +
                                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + CustomeName + "<br /> " +
                                  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + Deposit +
                                  "&nbsp; " + Date + " &nbsp; " + Time + "&nbsp; به شرح ذیل می باشد."+
                                  "<br /> " +
                                  " </div>";
                }

            }
            catch (Exception ex)
            {                
            }
            HTMLPage += HTMLstring;
        }
    
        public void AddTable(List<ReportDetails> RepDetails)
        {
            try
            {
                string TableHeader = "<br /> <div dir=\"rtl\"> <table dir=\"rtl\" frame=\"box\" rules=\"all\" style=\"position:relative; right:5%; width:90%;\"> <tr>  <td class=\"style14\" dir=\"rtl\" style=\" text-align:center; background-color: #FFCC99\">   ردیف</td> <td class=\"style20\" dir=\"rtl\" style=\" text-align:center; background-color: #FFCC99\">  شعبه</td> <td class=\"style20\" style=\" text-align:center; background-color: #FFCC99\">  تاریخ</td> <td class=\"style18\" style=\" text-align:center; background-color: #FFCC99\"> زمان</td> <td class=\"style11\" style=\" text-align:center; background-color: #FFCC99\">شرح سند</td> <td class=\"style16\" style=\" text-align:center; background-color: #FFCC99\"> مبلغ</td> <td class=\"style13\" style=\" text-align:center; background-color: #FFCC99\"> موجودی</td> </tr> ";
                string TableFooter = "</table> </div>";
                string TableBody = "";
                int i = 1;
                foreach (ReportDetails RepDet in RepDetails)
                {
                    RepDet.RowNumber = i.ToString();
                    TableBody += AddRow(RepDet);
                    i++;
                }
                HTMLPage += TableHeader + TableBody + TableFooter;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        private string AddRow(ReportDetails RepDet)
        {
            try
            {
                string Color = "#FFFFFF";
                if (int.Parse(RepDet.RowNumber)%2==0)
                {
                    Color = "#99CCFF";
                }
              string  rowstring= " <tr> " +
                        "<td class=\"style15\" dir=\"rtl\" style=\" text-align:center; background-color: " + Color + "\"> "+RepDet.RowNumber+"</td> "+
                        "<td class=\"style5\"  dir=\"rtl\" style=\" text-align:center; background-color: " + Color + "\"> "+RepDet.BranchName+"</td>"+
                        "<td class=\"style6\"  dir=\"ltr\" style=\" text-align:center; background-color: " + Color + "\"> " + RepDet.Date + "</td>" +
                        "<td class=\"style19\" dir=\"rtl\" style=\" text-align:center; background-color: " + Color + "\">" + RepDet.Time + "</td>" +
                        "<td class=\"style2\"  dir=\"rtl\" style=\" text-align:center; background-color: " + Color + "\"> " + RepDet.Comment + "</td>" +
                        "<td class=\"style17\" dir=\"rtl\" style=\"background-color: " + Color + "\">" + RepDet.Amount + "</td>" +
                        "<td dir=\"rtl\" style=\"background-color: " + Color + "\"> " + RepDet.RemainAmount + "</td>" +
                    "</tr>";
              return rowstring;           
            }
            catch (Exception ex)
            {
                return " ";                
            }
        }
    } 
}
