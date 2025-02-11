
package de.stanek.jjvm.heap;


public class  JJInstance
{

    JJInstance (JJClass c, int position)
    {
        this.c = c;
        this.position = position;
    }
    public final JJClass  c;
    final int  position;

    public String  toString ()
    {
        return "Instance" + System. identityHashCode (this);
    }

}
