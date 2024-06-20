
package de.stanek.jjvm.heap;



public class  JJAttributes
{

    JJAttributes (short attributes_count)
    {
        attributes = new JJAttribute [attributes_count];
    }
    private final JJAttribute[]  attributes;

    public String  toString ()
    {
        StringBuffer  sb = new StringBuffer ();
        sb. append ("Methods:\n");
        for (short  index = 1;  index < attributes.length;  ++index)
        {
            JJAttribute  entry = attributes [index];
            sb. append (index);
            sb. append (" ");
            sb. append (entry);
            sb. append ('\n');
        }
        return sb. toString ();
    }

    public void  set (int index, JJAttribute method)
    {
        attributes [index] = method;
    }

    public JJAttributeCode  attributeCode ()
    {
        for (JJAttribute attribute: attributes)
            if (attribute. getName (). equals (JJAttribute.CODE))
                return ((JJAttributeCode) attribute);
        return null;
    }

}
