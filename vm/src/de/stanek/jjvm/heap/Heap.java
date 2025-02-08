
package de.stanek.jjvm.heap;

import de.stanek.jjvm.Diagnose;
import de.stanek.jjvm.JJvmException;


public class  Heap
{

    public  Heap (Diagnose diag)
    {
        this.diag = diag;
        rootThreads = new RootThreads (diag);
        rootCells = new RootCells (diag);
        new Collector (). start ();
    }
    private final Diagnose  diag;
    private final RootThreads  rootThreads;
    private final RootCells  rootCells;

    private final static int  INSTANCES = 10;
    private JJInstance[]  instances = new JJInstance [INSTANCES];
    private JJInstance[]  instances2 = new JJInstance [INSTANCES];

    private class  Collector
        extends Thread
    {

        Collector ()
        {
            setDaemon (true);
        }

        public void  run ()
        {
            try
            {
                for (;;)
                {
                    Thread.sleep (1000);
                    collect ();
                }
            }
            catch (InterruptedException e)
            {
                e. printStackTrace ();
                System. exit (1);
            }
        }

    }
    private synchronized void  collect ()
    {
        if (diag != null)
            diag. out ("GC");
        synchronized (rootThreads)
        {
            for (JJThread  t: rootThreads)
            {
                JJInstance  o = t.o;
                if (o == null)
                    continue;
                instances2 [o.position] = o;
                if (diag != null)
                    diag. out ("GC " + t + " " + o);
            }
        }
        synchronized (rootCells)
        {
            for (JJStackFrame.Cell  c: rootCells)
            {
                JJInstance  o = c.o;
                instances2 [o.position] = o;
                if (diag != null)
                    diag. out ("GC " + c + " " + o);
            }
        }

        JJInstance[]  instances3 = instances;
        instances = instances2;
        instances2 = instances3;

        // free remaining
// TODO  finalization etc.
        for (int  i = 0;  i < instances2.length;  ++i)
            instances2 [i] = null;
    }


    // +++ class data +++

    // constant pool
    public ConstantPool  createConstantPool (short constant_pool_count)
    {
        return new ConstantPool (constant_pool_count);
    }
    public CPUtf8  createCPUtf8 (short length, byte[] bytes)
    {
        String  stringOnHeap = new String (bytes);
        return new CPUtf8 (length, stringOnHeap);
    }
    public CPInteger  createCPInteger (int value)
    {
        return new CPInteger (value);
    }
    public CPClass  createCPClass (short name_index)
    {
        return new CPClass (name_index);
    }
    public CPFieldRef  createCPFieldRef (short class_index
                                        , short name_and_type_index)
    {
        return new CPFieldRef (class_index, name_and_type_index);
    }
    public CPMethodRef  createCPMethodRef (short class_index
                                            , short name_and_type_index)
    {
        return new CPMethodRef (class_index, name_and_type_index);
    }
    public CPNameAndType  createCPNameAndType (short name_index
                                                , short descriptor_index)
    {
        return new CPNameAndType (name_index, descriptor_index);
    }

    // attributes
    public JJAttributes  createJJAttributes (short attributes_count)
    {
        return new JJAttributes (attributes_count);
    }
    public JJAttributeCode  createJJAttributeCode (String name, short max_stack
                                                    , short max_locals
                                                    , byte[] code)
    {
        byte[]  codeOnHeap = new byte [code.length];
        System.arraycopy(code, 0, codeOnHeap, 0, code.length);
        JJCode  jjCode = new JJCode (codeOnHeap);
        return new JJAttributeCode (name, max_stack, max_locals, jjCode);
    }

    // fields
    public JJFields  createJJFields (short fields_count)
    {
        return new JJFields (fields_count);
    }
    public JJField  createJJField (short access_flags
                                    , String name, String descriptor
                                    , JJAttributes attributes)
    {
        return new JJField (access_flags, name, descriptor, attributes);
    }

    // methods
    public JJMethods  createJJMethods (short methods_count)
    {
        return new JJMethods (methods_count);
    }
    public JJMethod  createJJMethod (short access_flags
                                    , String name, String descriptor
                                    , JJAttributes attributes
                                    , int params, int results)
    {
        return new JJMethod (access_flags, name, descriptor, attributes
                            , params, results);
    }

    // class
    public JJClass  createJJClass (ConstantPool cp, short this_class
                                    , short super_class, short[] interfaces
                                    , JJMethods methods, JJFields fields
                                    , JJAttributes attributes)
        throws JJvmException
    {
        return new JJClass (cp, this_class, super_class
                            , interfaces, methods, fields, attributes);
    }


    // +++ runtime data +++

    // engine/thread
    public JJThread  createJJThread ()
    {
        JJThread  t = new JJThread ();
        rootThreads. add (t);
        return t;
    }
    public void  freeJJThread (JJThread t)
    {
        rootThreads. remove (t);
    }

    // stack frame
    public JJStackFrame  createJJStackFrame (int max_locals, int max_stack)
    {
        return new JJStackFrame (diag, rootCells, max_locals, max_stack);
    }

    // instance
    public synchronized JJInstance  createJJInstance (JJClass c, JJThread t)
        throws JJvmException
    {
        int  position = -1;
        for (int  i = 0;  i < INSTANCES;  ++i)
            if (instances[i] == null)
            {
                position = i;
                break;
            }
        if (position == -1)
            throw new JJvmException ("No free position");
        JJInstance  instance = new JJInstance (c, position);
        instances [position] = instance;
        t. o = instance;
        return instance;
    }

}
