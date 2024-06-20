
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;

public class  JJMethod
{

    JJMethod (String name, String descriptor, JJAttributes jjAttributes
            , int params, int results)
    {
        this.name = name;
        this.descriptor = descriptor;
        this.jjAttributes = jjAttributes;
        this.params = params;
        this.results = results;
    }
    final String  name;
    final String  descriptor;
    private final JJAttributes jjAttributes;
    public final int  params, results;

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
