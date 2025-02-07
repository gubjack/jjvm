
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJMethod
{

    private final static short  ACC_NATIVE          = 0x0100;

    JJMethod (short access_flags, String name, String descriptor
            , JJAttributes jjAttributes, int params, int results)
    {
        this.access_flags = access_flags;
        this.name = name;
        this.descriptor = descriptor;
        this.jjAttributes = jjAttributes;
        this.params = params;
        this.results = results;
    }
    final short  access_flags;
    public final String  name;
    final String  descriptor;
    private final JJAttributes jjAttributes;
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
            jjAttributeCode = jjAttributes. attributeCode ();
            if (jjAttributeCode == null)
                throw new JJvmException (
                        "Missing code attribute for " + name + descriptor);
        }
        return jjAttributeCode;
    }
    private JJAttributeCode  jjAttributeCode;

}
