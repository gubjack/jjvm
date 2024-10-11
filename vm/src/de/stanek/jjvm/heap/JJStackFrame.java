
package  de.stanek.jjvm.heap;


public class  JJStackFrame
{

    JJStackFrame (int max_stack, int max_locals)
    {
        locals = new Cell[max_locals];
        for (int i = 0;  i < locals.length;  ++i)
            locals [i] = new Cell ();
        stack = new Cell[max_stack];
        for (int i = 0;  i < stack.length;  ++i)
            stack [i] = new Cell ();
    }
    private final Cell[]  locals;
    private final Cell[]  stack;
    private int  pointer = 0;

    private static class  Cell
    {
        int i;
        JJInstance o;
    }

    public void  dup ()
    {
        Cell  c = stack [pointer - 1];
        if (c.o != null)
            pusho (c.o);
        else
            push (c.i);
    }

    public void  push (int value)
    {
        Cell  c = stack [pointer++];
        c. i = value;
        c. o = null;    // indicates the cell to be an integer value
    }
    public int  pop ()
    {
        return stack [--pointer]. i;
    }
    public void  set (int index, int value)
    {
        locals [index]. i = value;
    }
    public int  get (int index)
    {
        return locals [index]. i;
    }

    public void  pusho (JJInstance value)
    {
        stack [pointer++]. o = value;
    }
    public JJInstance  popo ()
    {
        return stack [--pointer]. o;
    }
    public void  seto (int index, JJInstance value)
    {
        locals [index]. o = value;
    }
    public JJInstance  geto (int index)
    {
        return locals [index]. o;
    }

}
