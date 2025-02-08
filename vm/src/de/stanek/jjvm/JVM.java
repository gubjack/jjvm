
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJDescriptor;
import de.stanek.jjvm.heap.JJMethod;

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
        if (diag != null)
            diag. out ("executeMain " + clazz);

        // main method is at the moment 'public static int caluclate()'
        String  name = "calculate";
        String  descriptor = "()I";

        JJClass  c = loader. load (clazz);
        engine. initialize (c);     // this explicitely is required here

        JJMethod  m = c. method (name, descriptor);
        if (m == null)
            throw new JJvmException ("Missing method " + name + descriptor);
        int  results = JJDescriptor. results (descriptor);
        if (results != 1)
            throw new JJvmException (
                                "Main method is expected to return a result");

        return engine. run (c, m);
    }

}
