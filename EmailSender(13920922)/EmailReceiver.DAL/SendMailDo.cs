using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MailInbox.DataModel;
using MailInbox.DAL.Helper;
using System.Data;

namespace MailInbox.DAL
{
    public class SendMailDO : BaseDataObject<byte, SendMailModel>
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
                idbm.AddInParameter(mPKName, pModel.ID, SqlDbType.TinyInt);
            }
            else
            idbm.AddOutParameter(mPKName, SqlDbType.TinyInt);
            idbm.AddInParameter("CustomerID", pModel.CustomerID, SqlDbType.NChar);
            idbm.AddInParameter("CustomerEmailAddress", pModel.CustomerEmailAddress, SqlDbType.Text);
            idbm.AddInParameter("DepositNumber", pModel.DepositNumber, SqlDbType.NChar);
            idbm.AddInParameter("EmailText", pModel.EmailText, SqlDbType.Text);
            idbm.AddInParameter("EmailType", pModel.EmailType, SqlDbType.Bit);
            idbm.AddInParameter("Subject", pModel.Subject, SqlDbType.NChar);
            idbm.AddInParameter("mailstatus", pModel.Subject, SqlDbType.Bit);
            idbm.AddInParameter("Error", pModel.Subject, SqlDbType.Text);




        }


        protected override SendMailModel LoadFromReader(System.Data.IDataReader iDataReader)
        {
            SendMailModel ret = new SendMailModel()
            {
                ID = ToNullableByte(iDataReader["ID"]),
                CustomerID = ToNullableLong(iDataReader["CustomerID"]),
                CustomerEmailAddress = ToNullableString(iDataReader["CustomerEmailAddress"]),
                DepositNumber = ToNullableString(iDataReader["DepositNumber"]),
                EmailType = ToNullableByte(iDataReader["EmailType"]),
                EmailText = ToNullableString(iDataReader["EmailText"]),
                Subject = ToNullableString(iDataReader["Subject"]),
                mailstatus = ToNullableByte(iDataReader["mailstatus"]),
                Error = ToNullableString(iDataReader["Error"]),


            };
            return ret;
        }
    }
}
