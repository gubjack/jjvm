
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJClass
{

    JJClass (ConstantPool cp, short this_class, JJMethods jjMethods
            , JJFields fields)
        throws JJvmException
    {
        this.cp = cp;
        name = cp. className (this_class);
        this.jjMethods = jjMethods;
        this.fields = fields;
    }
    public final ConstantPool  cp;
    public final String  name;
    private final JJMethods  jjMethods;
    private final JJFields  fields;
    public boolean  initialized, initializing;

    public JJField  field (String name, String descriptor)
        throws JJvmException
    {
        return fields. field (name, descriptor);
    }

    public JJMethod  method (String name, String descriptor)
        throws JJvmException
    {
        return jjMethods. method (name, descriptor);
    }

}
