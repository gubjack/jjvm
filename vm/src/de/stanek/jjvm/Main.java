
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.Heap;


public class  Main
{

    public static void  main (String[] args)
        throws IOException, JJvmException
    {
        String  classRoot = "../sample/bin";
        String  mainClass = "sample/Simple";
        String  mainMethod = "calculate()I";

        Heap  heap = new Heap ();
        Definer  definer = new Definer (heap);
        Loader  loader = new Loader (definer, classRoot);
        Engine  engine = new Engine (heap, loader);

        int  result = engine. execute (mainClass, mainMethod);

        System.out.println (result);
    }

}
