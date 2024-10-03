
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJClass
{

    JJClass (ConstantPool cp, short this_class, short super_class
            , JJMethods jjMethods, JJFields fields)
        throws JJvmException
    {
        this.cp = cp;
        name = cp. className (this_class);
        // super_class == 0  for java.lang.Object
        super_name =  super_class == 0  ?  null  :  cp. className (super_class);
        this.jjMethods = jjMethods;
        this.fields = fields;
    }
    public final ConstantPool  cp;
    public final String  name;
    public final String  super_name;
    private final JJMethods  jjMethods;
    private final JJFields  fields;
    public boolean  initialized, initializing;
    public JJClass  super_c;

    public JJField  field (String name, String descriptor)
        throws JJvmException
    {
        JJField  f= fields. field (name, descriptor);
        if (f == null)
        {
            if (super_c != null)
                f = super_c. field (name, descriptor);
            if (f == null)
                throw new JJvmException ("Missing field " + name + descriptor);
        }
        return f;
    }

    public JJMethod  method (String name, String descriptor)
        throws JJvmException
    {
        return jjMethods. method (name, descriptor);
    }

}
