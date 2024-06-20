
package de.stanek.jjvm.heap;


public class  CPUtf8
	extends CPEntry
{

    CPUtf8 (short length, String value)
    {
        this.length = length;
        this.value = value;
    }
    final short  length;
    final String  value;

    public String  toString ()
    {
        return "Utf8 " + value;
    }

    public String  getString ()
    {
        return value;
    }

}
