
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;

public class  JJClass
{

    JJClass (ConstantPool cp, JJMethods jjMethods)
    {
        this.cp = cp;
        this.jjMethods = jjMethods;
    }
    public final ConstantPool  cp;
    private final JJMethods  jjMethods;
    public JJMethod  method (String name, String descriptor)
        throws JJvmException
    {
        return jjMethods. method (name, descriptor);
    }

}
