
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

    Engine (Heap heap, Loader loader)
    {
        this.heap = heap;
        this.loader = loader;
        thread = heap. createJJThread ();
    }
    private final Heap  heap;
    private final Loader  loader;
    private final JJThread  thread;

    void  cleanup ()
    {
        heap. freeJJThread (thread);
    }

    private final static byte  nop              = (byte) 0x00;
    private final static byte  iconst_m1        = (byte) 0x02;
    private final static byte  iconst_0         = (byte) 0x03;
    private final static byte  iconst_1         = (byte) 0x04;
    private final static byte  iconst_2         = (byte) 0x05;
    private final static byte  iconst_3         = (byte) 0x06;
    private final static byte  iconst_4         = (byte) 0x07;
    private final static byte  iconst_5         = (byte) 0x08;
    private final static byte  bipush           = (byte) 0x10;
    private final static byte  sipush           = (byte) 0x11;
    private final static byte  ldc              = (byte) 0x12;
    private final static byte  iload            = (byte) 0x15;
    private final static byte  iload_0          = (byte) 0x1a;
    private final static byte  iload_1          = (byte) 0x1b;
    private final static byte  iload_2          = (byte) 0x1c;
    private final static byte  iload_3          = (byte) 0x1d;
    private final static byte  aload_0          = (byte) 0x2a;
    private final static byte  istore           = (byte) 0x36;
    private final static byte  istore_0         = (byte) 0x3b;
    private final static byte  istore_1         = (byte) 0x3c;
    private final static byte  istore_2         = (byte) 0x3d;
    private final static byte  istore_3         = (byte) 0x3e;
    private final static byte  astore_0         = (byte) 0x4b;
    private final static byte  dup              = (byte) 0x59;
    private final static byte  iadd             = (byte) 0x60;
    private final static byte  isub             = (byte) 0x64;
    private final static byte  imul             = (byte) 0x68;
    private final static byte  ineg             = (byte) 0x74;
    private final static byte  ishl             = (byte) 0x78;
    private final static byte  ishr             = (byte) 0x7a;
    private final static byte  iushr            = (byte) 0x7c;
    private final static byte  iand             = (byte) 0x7e;
    private final static byte  ior              = (byte) 0x80;
    private final static byte  ixor             = (byte) 0x82;
    private final static byte  iinc             = (byte) 0x84;
    private final static byte  ifeq             = (byte) 0x99;
    private final static byte  ifne             = (byte) 0x9a;
    private final static byte  iflt             = (byte) 0x9b;
    private final static byte  ifge             = (byte) 0x9c;
    private final static byte  ifgt             = (byte) 0x9d;
    private final static byte  ifle             = (byte) 0x9e;
    private final static byte  if_icmpeq        = (byte) 0x9f;
    private final static byte  if_icmpne        = (byte) 0xa0;
    private final static byte  if_icmplt        = (byte) 0xa1;
    private final static byte  if_icmpge        = (byte) 0xa2;
    private final static byte  if_icmpgt        = (byte) 0xa3;
    private final static byte  if_icmple        = (byte) 0xa4;
    private final static byte  goto_            = (byte) 0xa7;
    private final static byte  ireturn          = (byte) 0xac;
    private final static byte  return_          = (byte) 0xb1;
    private final static byte  getstatic        = (byte) 0xb2;
    private final static byte  putstatic        = (byte) 0xb3;
    private final static byte  invokespecial    = (byte) 0xb7;
    private final static byte  invokestatic     = (byte) 0xb8;
    private final static byte  new_             = (byte) 0xbb;

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

            try
            {
                JJMethod  m = c. method ("<clinit>", "()V");
                if (m != null)
                {
                    JJStackFrame  sf = heap. createJJStackFrame (0, 0);
                    invokestatic (c, m, sf);
                    sf. clear ();
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

    void  invokestatic (JJClass jjClass, JJMethod m, JJStackFrame sfLast)
        throws JJvmException, IOException
    {
        JJCode  code;
        JJStackFrame  sf;
        {
            JJAttributeCode  ac = m. attributeCode ();
            code = ac. code();
            sf = heap. createJJStackFrame (ac.max_stack, ac.max_locals);
        }

        // Feed locals
        for (int  i = m.params - 1;  i >= 0;  --i)
            sf. set (i, sfLast. pop ());

        execute (jjClass, code, sf);

        // Retrieve result
        if (m.results == 1)
            sfLast. push (sf. pop ());

        sf. clear ();
    }
    private void  execute (JJClass jjClass, JJCode code, JJStackFrame sf)
        throws JJvmException, IOException
    {
        ConstantPool  cp = jjClass. cp;

        for (int counter = 0;  ;  ++counter)
        {
            byte  cmd = code. peek (counter);
            switch (cmd)
            {
                case nop:
                {
                    break;
                }
                case iconst_m1:
                {
                    sf. push (-1);
                    break;
                }
                case iconst_0:
                {
                    sf. push (0);
                    break;
                }
                case iconst_1:
                {
                    sf. push (1);
                    break;
                }
                case iconst_2:
                {
                    sf. push (2);
                    break;
                }
                case iconst_3:
                {
                    sf. push (3);
                    break;
                }
                case iconst_4:
                {
                    sf. push (4);
                    break;
                }
                case iconst_5:
                {
                    sf. push (5);
                    break;
                }
                case bipush:
                {
                    ++counter;
                    byte  value = code. peek (counter);
                    sf. push (value);
                    break;
                }
                case sipush:
                {
                    short  value = code. nextShort (counter);
                    counter += 2;
                    sf. push (value);
                    break;
                }
                case ldc:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = cp. integer (index);
                    sf. push (value);
                    break;
                }
                case iload:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = sf. get (index);
                    sf. push (value);
                    break;
                }
                case iload_0:
                {
                    int  value = sf. get (0);
                    sf. push (value);
                    break;
                }
                case iload_1:
                {
                    int  value = sf. get (1);
                    sf. push (value);
                    break;
                }
                case iload_2:
                {
                    int  value = sf. get (2);
                    sf. push (value);
                    break;
                }
                case iload_3:
                {
                    int  value = sf. get (3);
                    sf. push (value);
                    break;
                }
                case aload_0:
                {
                    JJInstance  o = sf. geto(0);
                    sf. pusho (o);
                    break;
                }
                case istore:
                {
                    ++counter;
                    byte  index = code. peek (counter);
                    int  value = sf. pop ();
                    sf. set (index, value);
                    break;
                }
                case istore_0:
                {
                    int  value = sf. pop ();
                    sf. set (0, value);
                    break;
                }
                case istore_1:
                {
                    int  value = sf. pop ();
                    sf. set (1, value);
                    break;
                }
                case istore_2:
                {
                    int  value = sf. pop ();
                    sf. set (2, value);
                    break;
                }
                case istore_3:
                {
                    int  value = sf. pop ();
                    sf. set (3, value);
                    break;
                }
                case astore_0:
                {
                    JJInstance  o = sf. popo (thread);
                    sf. seto (0, o);
                    thread.o = null;
                    break;
                }
                case dup:
                {
                    sf. dup ();
                    break;
                }
                case iadd:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 + value2;
                    sf. push (result);
                    break;
                }
                case isub:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 - value2;
                    sf. push (result);
                    break;
                }
                case imul:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 * value2;
                    sf. push (result);
                    break;
                }
                case ineg:
                {
                    int  value = sf. pop ();
                    int  result = -value;
                    sf. push (result);
                    break;
                }
                case ishl:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 << value2;
                    sf. push (result);
                    break;
                }
                case ishr:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 >> value2;
                    sf. push (result);
                    break;
                }
                case iushr:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 >>> value2;
                    sf. push (result);
                    break;
                }
                case iand:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 & value2;
                    sf. push (result);
                    break;
                }
                case ior:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 | value2;
                    sf. push (result);
                    break;
                }
                case ixor:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    int  result = value1 ^ value2;
                    sf. push (result);
                    break;
                }
                case iinc:
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
                case ifeq:
                {
                    int  value = sf. pop ();
                    if (value == 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case ifne:
                {
                    int  value = sf. pop ();
                    if (value != 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case iflt:
                {
                    int  value = sf. pop ();
                    if (value < 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case ifge:
                {
                    int  value = sf. pop ();
                    if (value >= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case ifgt:
                {
                    int  value = sf. pop ();
                    if (value > 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case ifle:
                {
                    int  value = sf. pop ();
                    if (value <= 0)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmpeq:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 == value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmpne:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 != value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmplt:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 < value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmpge:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 >= value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmpgt:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 > value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case if_icmple:
                {
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 <= value2)
                        counter = code. branch (counter);
                    else
                        counter += 2;
                    break;
                }
                case goto_:
                {
                    counter = code. branch (counter);
                    break;
                }
                case ireturn:
                {
                    return;
                }
                case return_:
                {
                    return;
                }
                case getstatic:
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
                case putstatic:
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
                case invokespecial:
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
                case invokestatic:
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
                case new_:
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

    void  invokespecial (JJClass jjClass, JJMethod m, JJStackFrame sfLast)
        throws JJvmException, IOException
    {
        JJCode  code;
        JJStackFrame  sf;
        {
            JJAttributeCode  ac = m. attributeCode ();
            code = ac. code();
            sf = heap. createJJStackFrame (ac.max_stack, ac.max_locals);
        }

        // Feed locals
        {
            sf. seto (0, sfLast. popo (thread));    // this
            thread. o = null;
        }
        for (int  i = m.params - 1;  i >= 0;  --i)
            sf. set (i + 1, sfLast. pop ());

        execute (jjClass, code, sf);

        // Retrieve result
        if (m.results == 1)
            sfLast. push (sf. pop ());

        sf. clear ();
    }

}
