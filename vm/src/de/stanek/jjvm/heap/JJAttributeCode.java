
package de.stanek.jjvm.heap;


public class  JJAttributeCode
    extends JJAttribute
{

    JJAttributeCode (String name, short max_stack, short max_locals
                    , JJCode jjCode)
    {
        super (name);
        this.max_stack = max_stack;
        this.max_locals = max_locals;
        this.jjCode = jjCode;
    }
    public final short max_stack;
    public final short max_locals;
    private final JJCode  jjCode;

    public String  toString ()
    {
        return "Code " + max_stack + " " + max_locals;
    }

    public JJCode  code ()
    {
        return jjCode;
    }

}
