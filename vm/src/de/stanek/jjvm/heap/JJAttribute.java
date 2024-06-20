
package de.stanek.jjvm.heap;


public abstract class  JJAttribute
{

    public final static String  CODE = "Code";

    JJAttribute (String name)
    {
        this.name = name;
    }
    private final String  name;
    public String  getName ()
    {
        return name;
    }

}
