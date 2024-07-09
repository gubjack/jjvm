
package de.stanek.jjvm.heap;


public class  CPInteger
	extends CPEntry
{

    CPInteger (int value)
    {
        this.value = value;
    }
    public final int  value;

    public String  toString ()
    {
        return "Integer " + value;
    }

}
