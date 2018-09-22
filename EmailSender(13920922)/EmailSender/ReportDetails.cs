using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace EmailSender
{
    class ReportDetails
    {
        public string RowNumber { get; set; }
        public string BranchName { get; set; }
        public string Date { get; set; }
        public string Time { get; set; }
        public string Comment { get; set; }
        public string Amount { get; set; }
        public string RemainAmount { get; set; }
    }
}
