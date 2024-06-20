
package de.stanek.jjvm.heap;


public class  CPNameAndType
	extends CPEntry
{

    CPNameAndType (short name_index, short descriptor_index)
    {
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }
    public final short name_index;
    public final short descriptor_index;

    public String  toString ()
    {
        return "NameAndType #" + name_index + ":#" + descriptor_index;
    }

}
