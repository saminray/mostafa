using System;
using System.Data;
using System.Data.Odbc;
using System.Data.SqlClient;
using System.Data.OleDb;
using System.Collections.Generic;

namespace MailInbox.DAL.Helper
{
   public interface IDBManager
  {
   
    string ConnectionString
    {
      get;
      set;
    }
 
    SqlConnection Connection
    {
      get;
    }
    SqlTransaction Transaction
    {
      get;
    }
 
    SqlDataReader DataReader
    {
      get;
    }
    SqlCommand Command
    {
      get;
    }

    List<SqlParameter> Parameters
    {
        get;
        set;
    }
 
    void Open();
    void BeginTransaction();
    void CommitTransaction();
    void AddInParameter(string paramName, object objValue);
    void AddInParameter(string paramName, object objValue,SqlDbType pType);

    void AddOutParameter(string paramName, SqlDbType pType);
    DataSet ExecuteDataSet(CommandType commandType, string commandText);
    SqlDataReader ExecuteReader(CommandType pCT, string pCommandText);
    object ExecuteScalar(CommandType commandType, string commandText);
    int ExecuteNonQuery(CommandType commandType,string commandText);
    void CloseReader();
    void Close();
    void Dispose();
   }
}
