
package de.stanek.jjvm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.stanek.jjvm.heap.JJCode;
import de.stanek.jjvm.heap.JJField;
import de.stanek.jjvm.heap.JJInstance;
import de.stanek.jjvm.heap.CPFieldRef;
import de.stanek.jjvm.heap.CPMethodRef;
import de.stanek.jjvm.heap.CPNameAndType;
import de.stanek.jjvm.heap.ConstantPool;
import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJAttributeCode;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJStackFrame;
import de.stanek.jjvm.heap.JJThread;


class  Engine
{

    Engine (Diagnose diag, Heap heap, Loader loader)
    {
        this.diag = diag;
        this.heap = heap;
        this.loader = loader;
        thread = heap. createJJThread ();
    }
    private final Diagnose  diag;
    private final Heap  heap;
    private final Loader  loader;
    private final JJThread  thread;

    void  cleanup ()
    {
        heap. freeJJThread (thread);
    }

    void  initialize (JJClass c)
        throws JJvmException, IOException
    {
        if (c.initialized)  // avoid unnecessary locking
            return;
        synchronized (c)
        {
            if (c.initializing) // same thread might see this
                return;
            if (c.initialized)  // different thread might see this
                return;

            if (diag != null)
                diag. out ("initialize " + c);
            c.initializing = true;
// TODO  Clarify circularity situations

            // Initialize super class first
            {
                String  class_name = c. super_name;
                if (class_name == null)
                    ; // no super class for java.lang.Object
                else
                {
                    JJClass  super_c = loader. load (class_name);
                    initialize (super_c);
                    c. super_c = super_c;
                }
            }

            // Initialize interface classes
            for (int  i = 0;  i < c. interfaces_name. length;  ++i)
            {
                String  interface_name = c. interfaces_name [i];
                JJClass  interface_c = loader. load (interface_name);
                initialize (interface_c);
                c. interfaces_c [i] = interface_c;
            }

            try
            {
                JJMethod  m = c. method ("<clinit>", "()V");
                if (m != null)
                {
                    try (JJStackFrame  sf = heap. createJJStackFrame (0, 0))
                    {
                        invokestatic (c, m, sf);
                    }
// TODO  Handle initialization failure
                }
            }
            finally
            {
                c.initializing = false;
            }

            c.initialized = true;
        }
    }

    int  run (JJClass c, JJMethod m)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("run " + c + "#" + m);
        try (JJStackFrame  sf = heap. createJJStackFrame (0, 1))
        {
            invokestatic (c, m, sf);

            return sf. pop ();
        }
    }

    private void  execute (JJClass jjClass, JJCode code, JJStackFrame sf)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("execute " + jjClass + " " + sf);
        ConstantPool  cp = jjClass. cp;

        for (int counter = 0;  ;  ++counter)
        {
            byte  cmd = code. peek (counter);
            if (diag != null)
            {
                diag. sleep ();
                diag. out ("execute " + sf + " " + Commands. cmdToString (cmd));
            }
            switch (cmd)
            {
                case Commands.nop:
                {
                    break;
                }
                case Commands.iconst_m1:
                {
                    sf. push (-1);
                    break;
                }
                case Commands.iconst_0:
                {
                    sf. push (0);
                    break;
                }
                case Commands.iconst_1:
                {
                    sf. push (1);
                    break;
                }
                case Commands.iconst_2:
                {
                    sf. push (2);
                    break;
                }
                case Commands.iconst_3:
                {
                    sf. push (3);
                    break;
                }
                case Commands.iconst_4:
                {
                    sf. push (4);
                    break;
                }
                case Commands.iconst_5:
                {
                    sf. push (5);
                    break;
                }
                case Commands.bipush:
                {
                    ++counter;
                    byte  value = code. peek (counter);
                    sf. push (value);
                    break;
                }
                case Commands.sipush:
                {
                    short  value = code. nextShort (counter);
                    counter += 2;
                    sf. push (value);
                    break;
                }
                case Commands.ldc:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = cp. integer (index);
                    sf. push (value);
                    break;
                }
                case Commands.iload:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = sf. get (index);
                    sf. push (value);
                    break;
                }
                case Commands.iload_0:
                {
                    int  value = sf. get (0);
                    sf. push (value);
                    break;
                }
                case Commands.iload_1:
                {
                    int  value = sf. get (1);
                    sf. push (value);
                    break;
                }
                case Commands.iload_2:
                {
                    int  value = sf. get (2);
                    sf. push (value);
                    break;
                }
                case Commands.iload_3:
                {
                    int  value = sf. get (3);
                    sf. push (value);
                    break;
                }
                case Commands.aload_0:
                {
                    JJInstance  o = sf. geto(0);
                    sf. pusho (o);
                    break;
                }
                case Commands.istore:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = sf. pop ();
                    sf. set (index, value);
                    break;
                }
                case Commands.istore_0:
                {
                    int  value = sf. pop ();
                    sf. set (0, value);
                    break;
                }
                case Commands.istore_1:
                {
                    int  value = sf. pop ();
                    sf. set (1, value);
                    break;
                }
                case Commands.istore_2:
                {
                    int  value = sf. pop ();
                    sf. set (2, value);
                    break;
                }
                case Commands.istore_3:
                {
                    int  value = sf. pop ();
                    sf. set (3, value);
                    break;
                }
                case Commands.astore_0:
                {
                    JJInstance  o = sf. popo (thread);
                    sf. seto (0, o);
                    thread.o = null;
                    break;
                }
                case Commands.dup:
                {
                    sf. dup ();
                    break;
                }
                case Commands.iadd:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 + value2;
                    sf. push (result);
                    break;
                }
                case Commands.isub:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 - value2;
                    sf. push (result);
                    break;
                }
                case Commands.imul:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 * value2;
                    sf. push (result);
                    break;
                }
                case Commands.ineg:
                {
                    int  value = sf. pop ();
                    int  result = -value;
                    sf. push (result);
                    break;
                }
                case Commands.ishl:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 << value2;
                    sf. push (result);
                    break;
                }
                case Commands.ishr:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 >> value2;
                    sf. push (result);
                    break;
                }
                case Commands.iushr:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 >>> value2;
                    sf. push (result);
                    break;
                }
                case Commands.iand:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 & value2;
                    sf. push (result);
                    break;
                }
                case Commands.ior:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 | value2;
                    sf. push (result);
                    break;
                }
                case Commands.ixor:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 ^ value2;
                    sf. push (result);
                    break;
                }
                case Commands.iinc:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    ++counter;
                    byte  const_ = code. peek (counter);
                    int  value = sf. get (index);
                    value += const_;
                    sf. set (index, value);
                    break;
                }
                case Commands.ifeq:
                {
                    int  value = sf. pop ();
                    if (value == 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifne:
                {
                    int  value = sf. pop ();
                    if (value != 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.iflt:
                {
                    int  value = sf. pop ();
                    if (value < 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifge:
                {
                    int  value = sf. pop ();
                    if (value >= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifgt:
                {
                    int  value = sf. pop ();
                    if (value > 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifle:
                {
                    int  value = sf. pop ();
                    if (value <= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpeq:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 == value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpne:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 != value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmplt:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 < value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpge:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 >= value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpgt:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 > value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmple:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 <= value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.goto_:
                {
                    counter = code. branch (counter);
                    break;
                }
                case Commands.ireturn:
                {
                    return;
                }
                case Commands.return_:
                {
                    return;
                }
                case Commands.getstatic:
                {
                    JJClass  c;
                    JJField  f;
                    {
                        CPFieldRef  fr;
                        {
                            short  index = code. nextShort (counter);
                            counter += 2;
                            fr = cp. fieldref (index);
                        }
                        {
                            String  class_name = cp. className (fr.class_index);
                            c = loader. load (class_name);
                            initialize (c);
                        }
                        {
                            String  name;
                            String  descriptor;
                            {
                                CPNameAndType  nt = cp. nameandtype (
                                                    fr.name_and_type_index);
                                name = cp. utf8 (nt.name_index);
                                descriptor = cp. utf8 (nt.descriptor_index);
                            }
                            f = c. field (name, descriptor);
                        }
                    }
                    sf. push (f.value);
                    break;
                }
                case Commands.putstatic:
                {
                    JJClass  c;
                    JJField  f;
                    {
                        CPFieldRef  fr;
                        {
                            short  index = code. nextShort (counter);
                            counter += 2;
                            fr = cp. fieldref (index);
                        }
                        {
                            String  class_name = cp. className (fr.class_index);
                            c = loader. load (class_name);
                            initialize (c);
                        }
                        {
                            String  name;
                            String  descriptor;
                            {
                                CPNameAndType  nt = cp. nameandtype (
                                                    fr.name_and_type_index);
                                name = cp. utf8 (nt.name_index);
                                descriptor = cp. utf8 (nt.descriptor_index);
                            }
                            f = c. field (name, descriptor);
                        }
                    }
                    f. value = sf. pop ();
                    break;
                }
                case Commands.invokespecial:
                {
                    JJClass  c;
                    JJMethod  m;
                    {
                        CPMethodRef  mr;
                        {
                            short  index = code. nextShort (counter);
                            counter += 2;
                            mr = cp. methodref (index);
                        }
                        {
                            String  class_name = cp. className (mr.class_index);
                            c = loader. load (class_name);
                            initialize (c);
                        }
                        {
                            String  name;
                            String  descriptor;
                            {
                                CPNameAndType  nt = cp. nameandtype (
                                                    mr.name_and_type_index);
                                name = cp. utf8 (nt.name_index);
                                descriptor = cp. utf8 (nt.descriptor_index);
                            }
                            m = c. method (name, descriptor);
                            if (m == null)
                                throw new JJvmException ("Missing method "
                                                        + name + descriptor);
                        }
                    }

                    if (m. isNative ())
                        throw new JJvmException (
                                "invokespecial_native not implemented");
                    else
                        invokespecial (c, m, sf);
                    break;
                }
                case Commands.invokestatic:
                {
                    JJClass  c;
                    JJMethod  m;
                    {
                        CPMethodRef  mr;
                        {
                            short  index = code. nextShort (counter);
                            counter += 2;
                            mr = cp. methodref (index);
                        }
                        {
                            String  class_name = cp. className (mr.class_index);
                            c = loader. load (class_name);
                            initialize (c);
                        }
                        {
                            String  name;
                            String  descriptor;
                            {
                                CPNameAndType  nt = cp. nameandtype (
                                                    mr.name_and_type_index);
                                name = cp. utf8 (nt.name_index);
                                descriptor = cp. utf8 (nt.descriptor_index);
                            }
                            m = c. method (name, descriptor);
                            if (m == null)
                                throw new JJvmException ("Missing method "
                                                        + name + descriptor);
                        }
                    }

                    if (m. isNative ())
                        invokestatic_native (c, m, sf);
                    else
                        invokestatic (c, m, sf);
                    break;
                }
                case Commands.new_:
                {
                    JJClass  c;
                    {
                        short  index = code. nextShort (counter);
                        counter += 2;
                        String  class_name = cp. className (index);
                        c = loader. load (class_name);
                        initialize (c);
                    }
                    JJInstance  instance = heap. createJJInstance (c, thread);
                    sf. pusho (instance);
                    thread.o = null;
                    break;
                }
                default:
                    throw new JJvmException (
                            "Unknown code 0x" + Integer.toHexString(cmd & 0xff)
                                        + " at " + counter);
            }
        }
    }

    private void  invokestatic (JJClass c, JJMethod m, JJStackFrame sfLast)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("invokestatic " + c + "#" + m);
        JJAttributeCode  ac = m. attributeCode ();
        JJCode  code = ac. code();
        try (JJStackFrame  sf = heap. createJJStackFrame (ac.max_locals
                                                            , ac.max_stack))
        {
            // Feed locals
            for (int  i = m.params - 1;  i >= 0;  --i)
                sf. set (i, sfLast. pop ());

            execute (c, code, sf);

            // Retrieve result
            if (m.results == 1)
                sfLast. push (sf. pop ());
        }
    }

    private void  invokestatic_native (JJClass c, JJMethod m
                                        , JJStackFrame sfLast)
        throws JJvmException
    {
        Object  result;
        {
            Method  method;
            {
                Class<?> clazz;
                {
                    String  name = "de.stanek.jjvm.natives."
                                    + c. name. replace ('/', '.');
                    try
                    {
                        clazz = Class. forName (name);
                    }
                    catch (ClassNotFoundException e){
                        throw new JJvmException (e);
                    }
                }

                Class<?>[]  types = new Class<?> [m. params];
                for (int  i = m. params - 1;  i >= 0;  --i)
                    types [i] = int.class;

                try
                {
                    method = clazz. getMethod (m. name, types);
                }
                catch (NoSuchMethodException | SecurityException e)
                {
                    throw new JJvmException (e);
                }
            }

            // Feed params
            Object[]  values = new Integer [m. params];
            for (int  i = m. params - 1;  i >= 0;  --i)
                values [i] = sfLast. pop ();

            try {
                result = method. invoke (null, values);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new JJvmException (e);
            }
        }

        // Retrieve result
        if (m.results == 1)
            sfLast. push ((Integer) result);
    }

    private void  invokespecial (JJClass c, JJMethod m, JJStackFrame sfLast)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("invokespecial " + c + "#" + m + " " + sfLast);
        JJAttributeCode  ac = m. attributeCode ();
        JJCode  code = ac. code();
        try (JJStackFrame  sf = heap. createJJStackFrame (ac.max_locals
                                                            , ac.max_stack))
        {
            // Feed locals
            {
                sf. seto (0, sfLast. popo (thread));    // this
                thread. o = null;
            }
            for (int  i = m.params - 1;  i >= 0;  --i)
                sf. set (i + 1, sfLast. pop ());

            execute (c, code, sf);

            // Retrieve result
            if (m.results == 1)
                sfLast. push (sf. pop ());
        }
    }

}
