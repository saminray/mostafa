using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace EmailSender
{
    class UserInformation
    {
        public string PreName { get; set; }
        public string Name { get; set; }
        public string ActionType { get; set; }
        public string DepositNubmer { get; set; }
        public string Date { get; set; }
        public string Time { get; set; }
        public string Amount { get; set; }
        public string Comment { get; set; }
        public string RemainAmount { get; set; }
        public bool IsTransaction { get; set; }
        public short TransactionCountRequested { get; set; }
        public short RealTransactionCount { get; set; }    
    }
}
