using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MailInbox.DataModel;
using MailInbox.DAL.Helper;
using System.Data;

namespace MailInbox.DAL
{
    public class MailInboxDO : BaseDataObject<long, MailInboxModel>
    {
        public MailInboxDO()
        {
            IsCachable = true;
            mTableName = "MailInbox";
            mPKName = "ID";

        }


        protected override void AddParameters(MailInboxModel pModel, IDBManager idbm, bool pAddPKAsINParam)
        {
            if (pAddPKAsINParam)
            {
                idbm.AddInParameter(mPKName, pModel.ID, SqlDbType.BigInt);
            }
            else
                idbm.AddOutParameter(mPKName, SqlDbType.BigInt);
            idbm.AddInParameter("MessageHeader", pModel.MessageHeader, SqlDbType.NChar);
            idbm.AddInParameter("MessageBody", pModel.MessageBody, SqlDbType.NChar);
            idbm.AddInParameter("MessageUID", pModel.MessageUID, SqlDbType.NChar);
            idbm.AddInParameter("Read", pModel.Read, SqlDbType.Bit);
            idbm.AddInParameter("SenderMail", pModel.SenderMail, SqlDbType.NChar);
            idbm.AddInParameter("receiveDate", pModel.receiveDate, SqlDbType.NChar);
            idbm.AddInParameter("receiveTime", pModel.receiveTime, SqlDbType.NChar);

        }


         protected override MailInboxModel LoadFromReader(System.Data.IDataReader iDataReader)
        {
            MailInboxModel ret = new MailInboxModel()
            {
                ID = ToNullableLong(iDataReader["ID"]),
                MessageHeader = ToNullableString(iDataReader["MessageHeader"]),
                MessageBody = ToNullableString(iDataReader["MessageBody"]),
                MessageUID = ToNullableString(iDataReader["MessageUID"]),
                Read = ToNullableBool(iDataReader["Read"]),
                SenderMail = ToNullableString(iDataReader["SenderMail"]),
                receiveDate = ToNullableString(iDataReader["receiveDate"]),
                receiveTime = ToNullableString(iDataReader["receiveTime"]),
            };
            return ret;
        }
    }
}
