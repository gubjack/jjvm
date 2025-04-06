
package de.stanek.jjvm.heap;


public class  JJDescriptor
{

    JJDescriptor (String string)
    {
        this.string = string;
        params = params ();
        results = results ();
    }
    final String  string;
    public final int  params, results;

    public String  toString ()
    {
        return string;
    }

    private int  params ()
    {
        int  params = 0;
        for (int  i = 1;  i < string.length ();  ++i)
        {
            char  c = string. charAt (i);
            if (c == ')')
                break;
            ++params;
        }
        return params;
    }
    private int  results ()
    {
        return  string. charAt (string. length () - 1) == 'I'
                ?  1  :  0;
    }

}
