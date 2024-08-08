
package de.stanek.jjvm.heap;


public class  CPFieldRef
	extends CPEntry
{

    CPFieldRef (short class_index, short name_and_type_index)
    {
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }
    public final short class_index;
    public final short name_and_type_index;

    public String  toString ()
    {
        return "Fieldref #" + class_index + ".#" + name_and_type_index;
    }

}
