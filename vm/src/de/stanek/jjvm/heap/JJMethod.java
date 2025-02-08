
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJMethod
{

    private final static short  ACC_NATIVE          = 0x0100;

    JJMethod (short access_flags, String name, JJDescriptor descriptor
            , JJAttributes attributes)
    {
        this.access_flags = access_flags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = attributes;
        this.params = descriptor. params;
        this.results = descriptor. results;
    }
    final short  access_flags;
    public final String  name;
    private final JJDescriptor  descriptor;
    private final JJAttributes attributes;
    public final int  params, results;

    public String  toString ()
    {
        return name + descriptor;
    }

    public boolean  isNative ()
    {
        return  0 != (access_flags & ACC_NATIVE);
    }

    public JJAttributeCode  attributeCode ()
        throws JJvmException
    {
        if (jjAttributeCode == null)
        {
            jjAttributeCode = attributes. attributeCode ();
            if (jjAttributeCode == null)
                throw new JJvmException ("Missing code attribute for " + this);
        }
        return jjAttributeCode;
    }
    private JJAttributeCode  jjAttributeCode;

    boolean  equals (String name, String descriptor)
    {
        return  this.name. equals (name)
                &&  this.descriptor. string. equals (descriptor);
    }

}
