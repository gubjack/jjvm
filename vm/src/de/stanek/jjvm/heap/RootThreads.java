
package de.stanek.jjvm.heap;

import java.util.HashSet;

import de.stanek.jjvm.Diagnose;


class  RootThreads
    extends HashSet <JJThread>
{

    RootThreads (Diagnose diag)
    {
        this.diag = diag;
    }
    private final Diagnose  diag;

    public synchronized boolean  add (JJThread t)
    {
        boolean  added = super. add (t);
        if (diag != null)
            diag. out ("RootThreads " + this);
        return added;
    }
    public synchronized boolean  remove (JJThread t)
    {
        boolean  removed = super. remove (t);
        if (diag != null)
            diag. out ("RootThreads " + this);
        return removed;
    }

}
