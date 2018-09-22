using System.Data;
using System.Data.SqlClient;
using System;
using System.Configuration ;
using System.Collections.Generic;


namespace MailInbox.DAL.Helper
{
    public sealed class DBManager : IDBManager, IDisposable
    {
        private SqlConnection idbConnection;
        private SqlDataReader idataReader;
        private SqlCommand idbCommand;
        private SqlTransaction idbTransaction = null;
        private List<SqlParameter> idbParameters = null;
        private string strConnection;

        public DBManager()
        {
            this.strConnection = ConfigurationSettings.AppSettings["ConnectionString"];
        }
        public DBManager(string connectionString)
        {
            this.strConnection = connectionString;
        }

        public SqlConnection Connection
        {
            get
            {
                return idbConnection;
            }
        }

        public SqlDataReader DataReader
        {
            get
            {
                return idataReader;
            }
            set
            {
                idataReader = value;
            }
        }



        public string ConnectionString
        {
            get
            {
                return strConnection;
            }
            set
            {
                strConnection = value;
            }
        }

        public SqlCommand Command
        {
            get
            {
                return idbCommand;
            }
        }

        public SqlTransaction Transaction
        {
            get
            {
                return idbTransaction;
            }
        }


        public void Open()
        {
            idbConnection = new SqlConnection();
            idbConnection.ConnectionString = this.ConnectionString;
            if (idbConnection.State != ConnectionState.Open)
                idbConnection.Open();
            this.idbCommand = new SqlCommand();
            this.idbCommand.CommandTimeout = 0;
        }

        public void Close()
        {
            if (idbConnection.State != ConnectionState.Closed)
                idbConnection.Close();
        }

        public void Dispose()
        {
            GC.SuppressFinalize(this);
            this.Close();
            if (this.DataReader != null)
                this.DataReader.Dispose();
            this.idbCommand = null;
            this.idbTransaction = null;
            //this.idbConnection = null;
        }

        public void CreateParameters()
        {
            idbParameters = new List<SqlParameter>();
        }


        public void BeginTransaction()
        {
            if (this.idbTransaction == null)
                idbTransaction = idbConnection.BeginTransaction();
            this.idbCommand.Transaction = idbTransaction;
        }

        public void CommitTransaction()
        {
            if (this.idbTransaction != null)
                this.idbTransaction.Commit();
            idbTransaction = null;
        }

        public SqlDataReader ExecuteReader(CommandType commandType, string
          commandText)
        {
            this.idbCommand = new SqlCommand();
            this.idbCommand.CommandTimeout = 0;
            idbCommand.Connection = this.Connection;
            PrepareCommand(idbCommand, this.Connection, this.Transaction,
             commandType,
              commandText, this.Parameters);
            this.DataReader = idbCommand.ExecuteReader();
            idbCommand.Parameters.Clear();
            return this.DataReader;
        }

        public void CloseReader()
        {
            if (this.DataReader != null)
                this.DataReader.Close();
        }

        private void AttachParameters(SqlCommand command,
          List<SqlParameter> commandParameters)
        {
            foreach (SqlParameter idbParameter in commandParameters)
            {
                if ((idbParameter.Direction == ParameterDirection.InputOutput || idbParameter.Direction == ParameterDirection.Input) && (idbParameter.Value == null))
                {
                    idbParameter.Value = DBNull.Value;
                }
                command.Parameters.Add(idbParameter);
            }
        }

        private void PrepareCommand(SqlCommand command, SqlConnection
          connection,
          SqlTransaction transaction, CommandType commandType, string
          commandText,
          List<SqlParameter> commandParameters)
        {
            command.Connection = connection;
            command.CommandText = commandText;
            command.CommandType = commandType;

            if (transaction != null)
            {
                command.Transaction = transaction;
            }

            if (commandParameters != null && commandParameters.Count > 0)
            {
                AttachParameters(command, commandParameters);
            }
        }

        public int ExecuteNonQuery(CommandType commandType, string
        commandText)
        {
            this.idbCommand = new SqlCommand();
            this.idbCommand.CommandTimeout = 0;
            PrepareCommand(idbCommand, this.Connection, this.Transaction,
            commandType, commandText, this.Parameters);
            int returnValue = idbCommand.ExecuteNonQuery();
            idbCommand.Parameters.Clear();
            return returnValue;
        }

        public object ExecuteScalar(CommandType commandType, string
          commandText)
        {
            this.idbCommand = new SqlCommand();
            this.idbCommand.CommandTimeout = 0;
            PrepareCommand(idbCommand, this.Connection, this.Transaction,
            commandType,
              commandText, this.Parameters);
            object returnValue = idbCommand.ExecuteScalar();
            idbCommand.Parameters.Clear();
            return returnValue;
        }

        public DataSet ExecuteDataSet(CommandType commandType, string
         commandText)
        {
            this.idbCommand = new SqlCommand();
            this.idbCommand.CommandTimeout = 0;
            PrepareCommand(idbCommand, this.Connection, this.Transaction,
           commandType,
              commandText, this.Parameters);
            SqlDataAdapter dataAdapter = new SqlDataAdapter();
            dataAdapter.SelectCommand = idbCommand;
            DataSet dataSet = new DataSet();
            dataAdapter.Fill(dataSet);
            idbCommand.Parameters.Clear();
            return dataSet;
        }

        #region IDBManager Members


        public List<SqlParameter> Parameters
        {
            get
            {
                if (idbParameters == null)
                    idbParameters = new List<SqlParameter>();
                return idbParameters;
            }
            set
            {
                idbParameters = new List<SqlParameter>();
            }
        }

        public void AddInParameter(string paramName, object objValue)
        {
            SqlParameter op = new SqlParameter(paramName, objValue);
            op.Direction = ParameterDirection.Input;
            Parameters.Add(op);
        }

        public void AddInParameter(string paramName, object objValue, SqlDbType pType)
        {
            SqlParameter op = new SqlParameter(paramName, pType);
            op.Direction = ParameterDirection.Input;
            op.Value = objValue;
            Parameters.Add(op);
        }


        public void AddOutParameter(string paramName, SqlDbType pType)
        {
            SqlParameter op = new SqlParameter(paramName, pType);
            op.Direction = ParameterDirection.Output;
            Parameters.Add(op);
        }

        #endregion
    }
}
