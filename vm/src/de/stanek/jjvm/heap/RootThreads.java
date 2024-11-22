
package de.stanek.jjvm.heap;

import java.util.HashSet;


class  RootThreads
    extends HashSet <JJThread>
{

    public synchronized boolean  add (JJThread t)
    {
        boolean  added = super. add (t);
        return added;
    }
    public synchronized boolean  remove (JJThread t)
    {
        boolean  removed = super. remove (t);
        return removed;
    }

}
