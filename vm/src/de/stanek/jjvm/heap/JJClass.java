
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;

public class  JJClass
{

    JJClass (ConstantPool cp, short this_class, JJMethods jjMethods)
        throws JJvmException
    {
        this.cp = cp;
        name = cp. className (this_class);
        this.jjMethods = jjMethods;
    }
    public final ConstantPool  cp;
    public final String  name;
    private final JJMethods  jjMethods;

    public JJMethod  method (String name, String descriptor)
        throws JJvmException
    {
        return jjMethods. method (name, descriptor);
    }

}
