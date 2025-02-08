
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJMethods
{

    JJMethods (short methods_count)
    {
        methods = new JJMethod [methods_count];
    }
    private final JJMethod[]  methods;

    public String  toString ()
    {
        StringBuffer  sb = new StringBuffer ();
        sb. append ("Methods:\n");
        for (short  index = 1;  index < methods.length;  ++index)
        {
            JJMethod  entry = methods [index];
            sb. append (index);
            sb. append (" ");
            sb. append (entry);
            sb. append ('\n');
        }
        return sb. toString ();
    }

    public void  set (int index, JJMethod method)
    {
        methods [index] = method;
    }

    public JJMethod  method (String name, String descriptor)
        throws JJvmException
    {
        for (JJMethod method: methods)
        {
            if (    method. name. equals (name)
                &&  method. descriptor. string. equals (descriptor))
                return method;
        }
        return null;
    }

}
