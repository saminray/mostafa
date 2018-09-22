using System;
using System.Collections.Generic;
using System.Xml.Serialization;
using System.Data;
using MailInbox.DataModel;
using System.Web;
using System.Runtime;

namespace MailInbox.DAL.Helper
{
    public abstract class BaseDataObject<PK_T, MODEL_T> where MODEL_T : BaseModel<PK_T>
    {
        //protected static HttpRuntime mRuntimeForCache;
        //protected static HttpRuntime RuntimeForCache
        //{
        //    get
        //    {
        //        if (mRuntimeForCache == null)
        //            mRuntimeForCache = new HttpRuntime();
        //        return mRuntimeForCache;
        //    }
        //}
        //private static HttpRuntime mHttpRuntime;
        //static BaseDataObject()
        //{
        //    mHttpRuntime = new HttpRuntime();
        //}
        //protected static System.Web.Caching.Cache MyCache
        //{
        //    get
        //    {
        //        return HttpRuntime.Cache;
        //    }
        //}
        //public static System.Web.Caching.Cache MySecondCache
        //{
        //    get
        //    {
        //        return HttpRuntime.Cache;
        //    }
        //}
        protected static string CacheDataKey
        {
            get
            {
                return typeof(MODEL_T).FullName;
            }
        }
        //public static string SecondCacheDataKey
        //{
        //    get;
        //    set;
        //}
//        public  Dictionary<PK_T, MODEL_T> CachedData
//        {
//            get
//            {
//                if (!IsCachable)
//                    return null;
                
//                if(MyCache.Get(CacheDataKey)==null)
//                //if (MyCache[CacheDataKey] == null)
//                {
//                    Dictionary<PK_T, MODEL_T> forInsert=new Dictionary<PK_T,MODEL_T>();
//                    List<MODEL_T> ret = SelectAll();
//                    foreach (MODEL_T item in ret)
//                        forInsert.Add(item.PKValue, item);
//                    MyCache.Insert(CacheDataKey, forInsert);
//                }
//                return MyCache[CacheDataKey] as Dictionary<PK_T, MODEL_T>;
//            }
//            set
//            {
//                if (value == null)
//                    MyCache.Remove(CacheDataKey);
//                else
//                    MyCache[CacheDataKey] = value;
////                MyCache[CacheDataKey] = value;
//            }
//        }
        //public Dictionary<PK_T, MODEL_T> SecondCachedData
        //{
        //    get
        //    {
        //        if (!IsCachable)
        //            return null;

        //        if (MySecondCache.Get(SecondCacheDataKey) == null)
        //        //if (MyCache[CacheDataKey] == null)
        //        {
        //            Dictionary<PK_T, MODEL_T> forInsert = new Dictionary<PK_T, MODEL_T>();
        //            List<MODEL_T> ret = SelectAll();
        //            foreach (MODEL_T item in ret)
        //                forInsert.Add(item.PKValue, item);
        //            MySecondCache.Insert(SecondCacheDataKey, forInsert);
        //        }
        //        return MySecondCache[SecondCacheDataKey] as Dictionary<PK_T, MODEL_T>;
        //    }
        //    set
        //    {
        //        if (value == null)
        //            MySecondCache.Remove(SecondCacheDataKey);
        //        else
        //            MySecondCache[SecondCacheDataKey] = value;
        //        //                MyCache[CacheDataKey] = value;
        //    }
        //}

        protected bool IsCachable { get; set; }

        protected DBManager GetNewDBManager()
        {
            return new DBManager();
            mSchema = "dbo";

        }
        [XmlIgnoreAttribute]
        protected string FullTableName
        {
            get
            {
                return mSchema + "." + mTableName;
            }
        }
       
        public bool DeepLoad { get; set; }
        protected string mSchema;
        protected string mTableName;
        protected string mPKName;
        protected abstract void AddParameters(MODEL_T pModel, IDBManager idbm, bool pAddPKAsINParam);
        protected abstract MODEL_T LoadFromReader(IDataReader iDataReader);
        protected string pkgName;
        protected string GetSPName(string pActionName)
        {
            return mSchema + ".MailInbox_" + mTableName + "_" + pActionName.ToLower();
        }

        public List<MODEL_T> SelectAll()
        {
            DeepLoad = true;
            List<MODEL_T> ret = new List<MODEL_T>();
            IDBManager idbm = new DBManager();
            try
            {
                idbm.Open();
                idbm.ExecuteReader(CommandType.StoredProcedure, GetSPName("SelectAll"));
                while (idbm.DataReader.Read())
                    ret.Add(LoadFromReader(idbm.DataReader));
            }
            catch (Exception exp)
            {
                throw exp;
            }
            finally
            {
                idbm.Dispose();
           
            }
            return ret;
        }
        public MODEL_T SelectOne(string pUIDValue)
        {
            DeepLoad = true;
            MODEL_T ret = null;
            IDBManager idbm = new DBManager();
            try
            {
                idbm.Open();
                idbm.AddInParameter("UID", pUIDValue);
                idbm.ExecuteReader(CommandType.StoredProcedure, GetSPName("SelectOne"));
                if (idbm.DataReader.Read())
                    ret = LoadFromReader(idbm.DataReader);
            }
            catch (Exception exp)
            {
                throw exp;
            }
            finally
            {
                idbm.Dispose();
              
            }
            return ret;

        }
        public List<MODEL_T> Search(MODEL_T pSearchObject)
        {
            return Search(pSearchObject, null);
        }
        public List<MODEL_T> Search(MODEL_T pSearchObject, Dictionary<string, object> pExtraParams)
        {
            DeepLoad = true;
            List<MODEL_T> ret = new List<MODEL_T>();
            IDBManager idbm = new DBManager();
            try
            {
                idbm.Open();

                AddParameters(pSearchObject, idbm, true);
                if (pExtraParams != null)
                    foreach (string pname in pExtraParams.Keys)
                        idbm.AddInParameter(pname, pExtraParams[pname]);
                idbm.ExecuteReader(CommandType.StoredProcedure, GetSPName("Search"));
                while (idbm.DataReader.Read())
                    ret.Add(LoadFromReader(idbm.DataReader));
            }
            catch (Exception exp)
            {
                throw exp;
            }
            finally
            {
                idbm.Dispose();
            }
            return ret;
        }
        public PK_T Insert(MODEL_T pModel, bool pPKIsIdentity)
        {
            return Insert(pModel, pPKIsIdentity, false);
        }
        public PK_T Insert(MODEL_T pModel)
        {
            return Insert(pModel, true, false);
        }
        public int Sync(List<MODEL_T> pDirtyList)
        {
            int modifCount = 0;
            List<MODEL_T> removed = new List<MODEL_T>();
            foreach (MODEL_T item in pDirtyList)
            {
                switch (item.State)
                {
                    case RowState.Original:
                        break;
                    case RowState.Modified:
                        if (Update(item) > 0)
                            modifCount++;
                        break;
                    case RowState.Added:
                        item.PKValue = Insert(item);
                        modifCount++;
                        break;
                    case RowState.Deleted:
                        if (De