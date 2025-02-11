
package de.stanek.jjvm.heap;


public class  CPInterfaceMethodRef
	extends CPEntry
{

    CPInterfaceMethodRef (short class_index, short name_and_type_index)
    {
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }
    public final short class_index;
    public final short name_and_type_index;

    public String  toString ()
    {
        return "InterfaceMethodRef #" + class_index + ".#" + name_and_type_index;
    }

}
