
package de.stanek.jjvm.heap;


public class  JJInstance
{

    JJInstance (JJClass c, int position)
    {
        this.c = c;
        this.position = position;
    }
    private final JJClass  c;
    final int  position;

}
