
package de.stanek.jjvm.heap;


public class  CPMethodRef
	extends CPEntry
{

    CPMethodRef (short class_index, short name_and_type_index)
    {
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }
    public final short class_index;
    public final short name_and_type_index;

    public String  toString ()
    {
        return "Methodref #" + class_index + ".#" + name_and_type_index;
    }

}
