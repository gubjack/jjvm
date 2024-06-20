
package  de.stanek.jjvm.heap;


public class  JJStackFrame
{

    JJStackFrame (int[] locals, int[] stack)
    {
        this.locals = locals;
        this.stack = stack;
    }
    private final int[]  locals;
    private final int[]  stack;
    private int  pointer = 0;

    public void  push (int value)
    {
        stack [pointer++] = value;
    }
    public int  pop ()
    {
        return stack [--pointer];
    }

    public void  set (int index, int value)
    {
        locals [index] = value;
    }
    public int  get (int index)
    {
        return locals [index];
    }

}
