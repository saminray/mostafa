using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MailInbox.DataModel;
using MailInbox.DAL.Helper;
using System.Data;

namespace MailInbox.DAL
{
    public class SendMailDO : BaseDataObject<long, SendMailModel>
    {
        public SendMailDO()
        {
            IsCachable = true;
            mTableName = "SendMail";
            mPKName = "ID";

        }


        protected override void AddParameters(SendMailModel pModel, IDBManager idbm, bool pAddPKAsINParam)
        {
            if (pAddPKAsINParam)
            {
                idbm.AddInParameter(mPKName, pModel.ID, SqlDbType.BigInt);
            }
            else
                idbm.AddOutParameter(mPKName, SqlDbType.BigInt);
            idbm.AddInParameter("CustomerID", pModel.CustomerID, SqlDbType.BigInt);
            idbm.AddInParameter("CustomerEmailAddress", pModel.CustomerEmailAddress, SqlDbType.Text);
            idbm.AddInParameter("DepositNumber", pModel.DepositNumber, SqlDbType.NChar);
            idbm.AddInParameter("EmailText", pModel.EmailText, SqlDbType.Text);
            idbm.AddInParameter("EmailType", pModel.EmailType, SqlDbType.NChar);
            idbm.AddInParameter("Subject", pModel.Subject, SqlDbType.NChar);
            idbm.AddInParameter("Status", pModel.Status, SqlDbType.Bit);
            idbm.AddInParameter("Error", pModel.Error, SqlDbType.Text);
            idbm.AddInParameter("SendDate", pModel.SendDate, SqlDbType.NVarChar);
            idbm.AddInParameter("SendTime", pModel.SendTime, SqlDbType.NVarChar);
        }


        protected override SendMailModel LoadFromReader(System.Data.IDataReader iDataReader)
        {
            SendMailModel ret = new SendMailModel()
            {
                ID = ToNullableLong(iDataReader["ID"]),
                CustomerID = ToNullableLong(iDataReader["CustomerID"]),
                CustomerEmailAddress = ToNullableString(iDataReader["CustomerEmailAddress"]),
                DepositNumber = ToNullableString(iDataReader["DepositNumber"]),
                EmailType =ToNullableString(iDataReader["EmailType"]),
                EmailText = ToNullableString(iDataReader["EmailText"]),
                Subject = ToNullableString(iDataReader["Subject"]),
                Status = ToNullableBool(iDataReader["mailstatus"]),
                Error = ToNullableString(iDataReader["Error"]),
                SendDate = ToNullableString(iDataReader["SendDate"]),
                SendTime = ToNullableString(iDataReader["SendTime"])

            };
            return ret;
        }
    }
}
