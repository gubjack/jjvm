
package  de.stanek.jjvm.heap;

import de.stanek.jjvm.Diagnose;


public class  JJStackFrame
    implements AutoCloseable
{

    JJStackFrame (Diagnose diag, RootCells rootCells
                    , int max_locals, int max_stack)
    {
        this.diag = diag;
        this.rootCells = rootCells;
        locals = new Cell[max_locals];
        for (int i = 0;  i < locals.length;  ++i)
            locals [i] = new Cell ();
        stack = new Cell[max_stack];
        for (int i = 0;  i < stack.length;  ++i)
            stack [i] = new Cell ();
        if (diag != null)
            diag. out ("new " + this);
    }
    private final Diagnose  diag;
    private final RootCells  rootCells;
    private final Cell[]  locals;
    private final Cell[]  stack;
    private int  pointer = 0;

    static class  Cell
    {
        int i;
        JJInstance o;
        public String  toString ()
        {
            return "Cell" + System. identityHashCode (this);
        }
    }

    public String  toString ()
    {
        return "Frame" + System. identityHashCode (this);
    }

    @Override
    public void  close ()
    {
        if (diag != null)
            diag. out ("clear " + this);
        for (int i = 0;  i < pointer;  ++i)
        {
            Cell  c = stack [i];
            JJInstance  o = c. o;
            if (o != null)
                rootCells. remove (c);
        }
        for (int i = 0;  i < locals.length;  ++i)
        {
            Cell  c = locals [i];
            JJInstance  o = c. o;
            if (o != null)
                rootCells. remove (c);
        }
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
        Cell  c = stack [pointer++];
        c. o = value;
        if (value != null)
            rootCells. add (c);
    }
    public JJInstance  popo (JJThread t)
    {
        Cell  c = stack [--pointer];
        JJInstance  o = c. o;
        if (o != null)
        {
            t. o = o;
            rootCells. remove (c);
        }
        return o;
    }
    public void  seto (int index, JJInstance value)
    {
        Cell  c = locals [index];
        JJInstance  o = c. o;
        if (o == value)
            return;
        if (o != null  &&  value == null)
            rootCells. remove (c);
        c. o = value;
        if (value != null  &&  o == null)
            rootCells. add (c);
    }
    public JJInstance  geto (int index)
    {
        return locals [index]. o;
    }

}
