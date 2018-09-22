using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace MailInbox.DataModel
{
    public enum RowState
    {
        Original = 0,
        Modified = 1,
        Added = 2,
        Deleted = 3
    }
   
    public abstract class BaseModel<T>
    {
   
        public RowState State { get; set; }
        public BaseModel()
        {
            State = RowState.Original;
        }
        public abstract T PKValue { get; set; }
    }
}
