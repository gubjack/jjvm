
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;


public class  JJFields
{

    JJFields (short fields_count)
    {
        fields = new JJField [fields_count];
    }
    private final JJField[]  fields;

    public String  toString ()
    {
        StringBuffer  sb = new StringBuffer ();
        sb. append ("Fields:\n");
        for (short  index = 1;  index < fields.length;  ++index)
        {
            JJField  entry = fields [index];
            sb. append (index);
            sb. append (" ");
            sb. append (entry);
            sb. append ('\n');
        }
        return sb. toString ();
    }

    public void  set (int index, JJField field)
    {
        fields [index] = field;
    }

    public JJField  field (String name, String descriptor)
        throws JJvmException
    {
        for (JJField field: fields)
        {
            if (    field. name. equals (name)
                &&  field. descriptor. equals (descriptor))
                return field;
        }
        return null;
    }

}
