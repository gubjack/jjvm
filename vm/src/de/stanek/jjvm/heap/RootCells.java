
package de.stanek.jjvm.heap;

import java.util.HashSet;


class  RootCells
    extends HashSet <JJStackFrame.Cell>
{

    public synchronized boolean  add (JJStackFrame.Cell c)
    {
        boolean  added = super. add (c);
        return added;
    }
    public synchronized boolean  remove (JJStackFrame.Cell c)
    {
        boolean  removed = super. remove (c);
        return removed;
    }

}
