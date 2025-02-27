
package de.stanek.jjvm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.stanek.jjvm.heap.JJCode;
import de.stanek.jjvm.heap.JJField;
import de.stanek.jjvm.heap.JJInstance;
import de.stanek.jjvm.heap.CPFieldRef;
import de.stanek.jjvm.heap.CPInterfaceMethodRef;
import de.stanek.jjvm.heap.CPMethodRef;
import de.stanek.jjvm.heap.CPNameAndType;
import de.stanek.jjvm.heap.ConstantPool;
import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJAttributeCode;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJFrame;
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
                    JJAttributeCode  ac = m. attributeCode ();
                    try (JJFrame  frame = heap. createJJFrame (ac.max_locals
                                                                , ac.max_stack))
                    {
                        invoke (c, m, frame);
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

    void  run (JJClass c, JJMethod m)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("run " + c + "#" + m);
        JJAttributeCode  ac = m. attributeCode ();
        try (JJFrame  frame = heap. createJJFrame (ac.max_locals, ac.max_stack))
        {
            invoke (c, m, frame);
        }
    }

    private void  execute (JJClass jjClass, JJCode code, JJFrame frame)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("execute " + jjClass + " " + frame);
        ConstantPool  cp = jjClass. cp;

        for (int counter = 0;  ;  ++counter)
        {
            byte  cmd = code. peek (counter);
            if (diag != null)
            {
                diag. sleep ();
                diag. out ("execute " + frame
                                        + " " + Commands. cmdToString (cmd));
            }
            switch (cmd)
            {
                case Commands.nop:
                {
                    break;
                }
                case Commands.iconst_m1:
                {
                    frame. pushi (-1);
                    break;
                }
                case Commands.iconst_0:
                {
                    frame. pushi (0);
                    break;
                }
                case Commands.iconst_1:
                {
                    frame. pushi (1);
                    break;
                }
                case Commands.iconst_2:
                {
                    frame. pushi (2);
                    break;
                }
                case Commands.iconst_3:
                {
                    frame. pushi (3);
                    break;
                }
                case Commands.iconst_4:
                {
                    frame. pushi (4);
                    break;
                }
                case Commands.iconst_5:
                {
                    frame. pushi (5);
                    break;
                }
                case Commands.bipush:
                {
                    ++counter;
                    byte  value = code. peek (counter);
                    frame. pushi (value);
                    break;
                }
                case Commands.sipush:
                {
                    short  value = code. nextShort (counter);
                    counter += 2;
                    frame. pushi (value);
                    break;
                }
                case Commands.ldc:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = cp. integer (index);
                    frame. pushi (value);
                    break;
                }
                case Commands.iload:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = frame. geti (index);
                    frame. pushi (value);
                    break;
                }
                case Commands.iload_0:
                {
                    int  value = frame. geti (0);
                    frame. pushi (value);
                    break;
                }
                case Commands.iload_1:
                {
                    int  value = frame. geti (1);
                    frame. pushi (value);
                    break;
                }
                case Commands.iload_2:
                {
                    int  value = frame. geti (2);
                    frame. pushi (value);
                    break;
                }
                case Commands.iload_3:
                {
                    int  value = frame. geti (3);
                    frame. pushi (value);
                    break;
                }
                case Commands.aload_0:
                {
                    JJInstance  o = frame. geto(0);
                    frame. pusho (o);
                    break;
                }
                case Commands.istore:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = frame. popi ();
                    frame. seti (index, value);
                    break;
                }
                case Commands.istore_0:
                {
                    int  value = frame. popi ();
                    frame. seti (0, value);
                    break;
                }
                case Commands.istore_1:
                {
                    int  value = frame. popi ();
                    frame. seti (1, value);
                    break;
                }
                case Commands.istore_2:
                {
                    int  value = frame. popi ();
                    frame. seti (2, value);
                    break;
                }
                case Commands.istore_3:
                {
                    int  value = frame. popi ();
                    frame. seti (3, value);
                    break;
                }
                case Commands.astore_0:
                {
                    JJInstance  o = frame. popo (thread);
                    frame. seto (0, o, thread);
                    break;
                }
                case Commands.pop:
                {
                    frame. pop (thread);
                    break;
                }
                case Commands.dup:
                {
                    frame. dup ();
                    break;
                }
                case Commands.iadd:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 + value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.isub:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 - value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.imul:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 * value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.ineg:
                {
                    int  value = frame. popi ();
                    int  result = -value;
                    frame. pushi (result);
                    break;
                }
                case Commands.ishl:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 << value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.ishr:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 >> value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.iushr:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 >>> value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.iand:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 & value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.ior:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 | value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.ixor:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    int  result = value1 ^ value2;
                    frame. pushi (result);
                    break;
                }
                case Commands.iinc:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    ++counter;
                    byte  const_ = code. peek (counter);
                    int  value = frame. geti (index);
                    value += const_;
                    frame. seti (index, value);
                    break;
                }
                case Commands.ifeq:
                {
                    int  value = frame. popi ();
                    if (value == 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifne:
                {
                    int  value = frame. popi ();
                    if (value != 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.iflt:
                {
                    int  value = frame. popi ();
                    if (value < 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifge:
                {
                    int  value = frame. popi ();
                    if (value >= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifgt:
                {
                    int  value = frame. popi ();
                    if (value > 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.ifle:
                {
                    int  value = frame. popi ();
                    if (value <= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpeq:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    if (value1 == value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpne:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    if (value1 != value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmplt:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    if (value1 < value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpge:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    if (value1 >= value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmpgt:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
                    if (value1 > value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case Commands.if_icmple:
                {
                    int  value2 = frame. popi ();
                    int  value1 = frame. popi ();
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
                    frame. pushi (f.value);
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
                    f. value = frame. popi ();
                    break;
                }
                case Commands.invokevirtual:
                {
                    JJInstance  o = frame. popo (thread);
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
                            m = c. method_virtual (name, descriptor);
                            if (m == null)
                                throw new JJvmException ("Missing method "
                                                        + name + descriptor);
                        }
                    }

                    if (m. isNative ())
                        throw new JJvmException (
                                "invokevirtual_native not implemented");
                    else
                        invoke (c, o, m, frame);
                    break;
                }
                case Commands.invokespecial:
                {
                    JJInstance  o = frame. popo (thread);
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
                        invoke (c, o, m, frame);
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
                        invoke_native (c, m, frame);
                    else
                        invoke (c, m, frame);
                    break;
                }
                case Commands.invokeinterface:
                {
                    JJInstance  o = frame. popo (thread);
                    JJMethod  m;
                    {
                        CPInterfaceMethodRef  mr;
                        {
                            short  index = code. nextShort (counter);
                            counter += 2;
                            mr = cp. interfacemethodref (index);
                        }
                        {
                            @SuppressWarnings("unused")
                            short  count = code. nextShort (counter);
                            counter += 2;
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
                            JJClass  c = o.c;
                            m = c. method_virtual (name, descriptor);
                            if (m == null)
                                throw new JJvmException ("Missing method "
                                                        + name + descriptor);
                        }
                    }

                    if (m. isNative ())
                        throw new JJvmException (
                                "invokeinterface_native not implemented");
                    else
                        invoke (o.c, o, m, frame);
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
                    frame. pusho (instance, thread);
                    break;
                }
                default:
                    throw new JJvmException (
                            "Unknown code 0x" + Integer.toHexString(cmd & 0xff)
                                        + " at " + counter);
            }
        }
    }

    private void  invoke (JJClass c, JJInstance o, JJMethod m
                            , JJFrame frameLast)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("invoke " + c + "#" + o + "." + m + " " + frameLast);

        JJAttributeCode  ac = m. attributeCode ();
        try (JJFrame  frame = heap. createJJFrame (ac.max_locals, ac.max_stack))
        {
            // Feed locals
            frame. seto (0, o, thread);    // this
            for (int  i = m.params - 1;  i >= 0;  --i)
                frame. seti (i + 1, frameLast. popi ());

            // Invoke the method
            {
                JJCode  code = ac. code();
                execute (c, code, frame);
            }

            // Retrieve result
            if (m.results == 1)
                frameLast. pushi (frame. popi ());
        }
    }

    private void  invoke (JJClass c, JJMethod m, JJFrame frameLast)
        throws JJvmException, IOException
    {
        if (diag != null)
            diag. out ("invoke " + c + "." + m + " " + frameLast);

        JJAttributeCode  ac = m. attributeCode ();
        try (JJFrame  frame = heap. createJJFrame (ac.max_locals, ac.max_stack))
        {
            // Feed locals
            for (int  i = m.params - 1;  i >= 0;  --i)
                frame. seti (i, frameLast. popi ());

            // Invoke the method
            {
                JJCode  code = ac. code();
                execute (c, code, frame);
            }

            // Retrieve result
            if (m.results == 1)
                frameLast. pushi (frame. popi ());
        }
    }

    private void  invoke_native (JJClass c, JJMethod m, JJFrame frameLast)
        throws JJvmException
    {
        if (diag != null)
            diag. out ("invoke_native " + c + "#" + m + " " + frameLast);

        Object  result;
        {
            Method  method;
            {
                // Load native class code
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

                // Prepare parameter types (actually the descriptor)
                Class<?>[]  types = new Class<?> [m. params];
                for (int  i = m. params - 1;  i >= 0;  --i)
                    types [i] = int.class;

                // Provide access to native method+descriptor
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
                values [i] = frameLast. popi ();

            // Call native method
            try
            {
                result = method. invoke (null, values);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new JJvmException (e);
            }
        }

        // Retrieve result
        if (m.results == 1)
            frameLast. pushi ((Integer) result);
    }

}
