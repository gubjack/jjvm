
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;

public class  ConstantPool
{

    public final static byte  CONSTANT_Utf8        = 1;
    public final static byte  CONSTANT_Integer     = 3;
    public final static byte  CONSTANT_Class       = 7;
    public final static byte  CONSTANT_MethodRef   = 10;
    public final static byte  CONSTANT_NameAndType = 12;

    ConstantPool (short constant_pool_count)
    {
        tags = new byte [constant_pool_count];
        entries = new CPEntry [constant_pool_count];
    }
    private final byte[]  tags;
    private final CPEntry[]  entries;

    public String  toString ()
    {
        StringBuffer  sb = new StringBuffer ();
        sb. append ("Constant pool:\n");
        for (short  index = 1;  index < tags.length;  ++index)
        {
            CPEntry  entry = entries [index];
            sb. append (index);
            sb. append (" ");
            sb. append (entry);
            sb. append ('\n');
        }
        return sb. toString ();
    }

    public void  set (int index, byte tag, CPEntry entry)
    {
        tags [index] = tag;
        entries [index] = entry;
    }
    public byte  getTag (int index)
    {
        return tags [index];
    }
    public CPEntry  getEntry (int index)
    {
        return entries [index];
    }

    public String  utf8 (int index)
        throws JJvmException
    {
        assertUtf8 (index);
        CPUtf8  cpUtf8 = (CPUtf8) entries [index];
        return cpUtf8. getString ();
    }
    public void  assertUtf8 (int index)
        throws JJvmException
    {
        byte  tag = tags [index];
        if (tag != CONSTANT_Utf8)
            throw new JJvmException ("Utf8 expected at " + index);
    }

    public int  integer (int index)
        throws JJvmException
    {
        byte  tag = tags [index];
        if (tag != ConstantPool.CONSTANT_Integer)
            throw new JJvmException ("Integer expected at " + index);
        CPInteger  cpInteger = (CPInteger) entries [index];
        return cpInteger. value;
    }

    public String  className (int index)
        throws JJvmException
    {
        byte  tag = tags [index];
        if (tag != ConstantPool.CONSTANT_Class)
            throw new JJvmException ("Class expected at " + index);
        CPClass  cpClass = (CPClass) entries [index];
        short  class_name_index = cpClass. name_index;
        return utf8 (class_name_index);
    }

    public CPMethodRef  methodref (int index)
        throws JJvmException
    {
        byte  tag = tags [index];
        if (tag != ConstantPool.CONSTANT_MethodRef)
            throw new JJvmException ("MethodRef expected at " + index);
        CPMethodRef  cpMethodRef = (CPMethodRef) entries [index];
        return cpMethodRef;
    }

    public CPNameAndType  nameandtype (int index)
        throws JJvmException
    {
        byte  tag = tags [index];
        if (tag != ConstantPool.CONSTANT_NameAndType)
            throw new JJvmException ("NameAndType expected at " + index);
        CPNameAndType  cpNameAndType = (CPNameAndType) entries [index];
        return cpNameAndType;
    }

}
