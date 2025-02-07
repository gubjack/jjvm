
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJStackFrame;

class  JVM
{

    JVM (Diagnose diag, String bootClasses, String appClasses)
    {
        this.diag = diag;
        heap = new Heap (diag);
        Definer  definer = new Definer (heap);
        loader = new Loader (definer, bootClasses, appClasses);
        engine = new Engine (diag, heap, loader);
    }
    private final Diagnose  diag;
    private final Heap  heap;
    private final Loader  loader;
    private final Engine  engine;

    void  cleanup ()
    {
        engine. cleanup ();
    }

    int  executeMain (String clazz)
        throws JJvmException, IOException
    {
        // main method is at the moment 'public static int caluclate()'
        String  name = "calculate";
        String  descriptor = "()I";
        if (diag != null)
            diag. out ("executeMain " + clazz + "#" + name + descriptor);

        JJClass  c = loader. load (clazz);
        engine. initialize (c);     // this explicitely is required here

        JJMethod  m = c. method (name, descriptor);
        if (m == null)
            throw new JJvmException ("Missing method " + name + descriptor);
        int  results = Definer. results (descriptor);
        if (results != 1)
            throw new JJvmException (
                                "Main method is expected to return a result");

        return engine. run (c, m);
    }

}
