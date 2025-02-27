
package de.stanek.jjvm.heap;

import java.util.HashSet;

import de.stanek.jjvm.Diagnose;


class  RootCells
    extends HashSet <JJFrame.Cell>
{

    RootCells (Diagnose diag)
    {
        this.diag = diag;
    }
    private final Diagnose  diag;

    public synchronized boolean  add (JJFrame.Cell c)
    {
        boolean  added = super. add (c);
        if (diag != null)
            diag. out ("RootCells " + this);
        return added;
    }
    public synchronized boolean  remove (JJFrame.Cell c)
    {
        boolean  removed = super. remove (c);
        if (diag != null)
            diag. out ("RootCells " + this);
        return removed;
    }

}
