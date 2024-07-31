
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJStackFrame;

class  JVM
{

    JVM (String classRoot)
    {
        heap = new Heap ();
        Definer  definer = new Definer (heap);
        loader = new Loader (definer, classRoot);
        engine = new Engine (heap, loader);
    }
    private final Heap  heap;
    private final Loader  loader;
    private final Engine  engine;

    int  execute (String clazz, String name, String descriptor)
        throws JJvmException, IOException
    {
        JJClass  c = loader. load (clazz);
        JJMethod  m = c. method (name, descriptor);
        int  results = Definer. results (descriptor);
        if (results == 0)
            throw new JJvmException ("Main method is expected to return a result");
        JJStackFrame  sf = heap. createJJStackFrame ((short) results, (short) 0);

        engine. invokestatic (c, m, sf);

        return sf. pop ();
    }

}
