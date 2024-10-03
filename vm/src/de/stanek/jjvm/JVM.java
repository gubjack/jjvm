
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJStackFrame;

class  JVM
{

    JVM (String appClasses)
    {
        heap = new Heap ();
        Definer  definer = new Definer (heap);
        loader = new Loader (definer, appClasses);
        engine = new Engine (heap, loader);
    }
    private final Heap  heap;
    private final Loader  loader;
    private final Engine  engine;

    int  executeMain (String clazz)
        throws JJvmException, IOException
    {
        // main method is at the moment 'public static int caluclate()'
        String  name = "calculate";
        String  descriptor = "()I";

        JJClass  c = loader. load (clazz);
        engine. initialize (c);     // this explicitely is required here

        JJMethod  m = c. method (name, descriptor);
        if (m == null)
            throw new JJvmException ("Missing method " + name + descriptor);
        int  results = Definer. results (descriptor);
        if (results == 0)
            throw new JJvmException ("Main method is expected to return a result");
        JJStackFrame  sf = heap. createJJStackFrame (results, 0);

        engine. invokestatic (c, m, sf);

        return sf. pop ();
    }

}
