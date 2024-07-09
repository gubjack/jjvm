
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJAttributeCode;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJCode;
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
        JJClass  jjClass = loader. load (mainClass);
        JJCode  jjCode;
        JJStackFrame  sf;
        {
            JJMethod  jjMethod;
            {
                int  parenthesis = mainMethod. indexOf ('(');
                String  name = mainMethod. substring (0, parenthesis);
                String  descriptor = mainMethod. substring (parenthesis);
                jjMethod = jjClass. method (name, descriptor);
            }
            JJAttributeCode  ac = jjMethod. attributeCode ();
            jjCode = ac. code();
            sf = heap. createJJStackFrame (ac. max_stack, ac. max_locals);
        }

        engine. execute (jjClass, jjCode, sf);

        return sf. pop ();
    }

}
