
package de.stanek.jjvm.heap;


public class  JJField
{

    JJField (short access_flags, String name, String descriptor
            , JJAttributes jjAttributes)
    {
        this.access_flags = access_flags;
        this.name = name;
        this.descriptor = descriptor;
        this.jjAttributes = jjAttributes;
    }
    final short  access_flags;
    public final String  name;
    final String  descriptor;
    @SuppressWarnings("unused")
    private final JJAttributes jjAttributes;

}
