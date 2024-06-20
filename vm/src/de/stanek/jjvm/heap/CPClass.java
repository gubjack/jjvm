
package de.stanek.jjvm.heap;


public class  CPClass
	extends CPEntry
{

    CPClass (short name_index)
    {
        this.name_index = name_index;
    }
    public final short name_index;

    public String  toString ()
    {
        return "Class #" + name_index;
    }

}
