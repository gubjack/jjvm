
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

    int  execute (String mainClass, String mainMethod)
        throws JJvmException, IOException
    {
        JJClass  c = loader. load (mainClass);

        JJMethod  m;
        {
            int  parenthesis = mainMethod. indexOf ('(');
            String  name = mainMethod. substring (0, parenthesis);
            String  descriptor = mainMethod. substring (parenthesis);
            m = c. method (name, descriptor);
        }

        JJStackFrame  sf = heap. createJJStackFrame ((short) 1, (short) 0);

        engine. invokestatic (c, m, sf);

        return sf. pop ();
    }

}
