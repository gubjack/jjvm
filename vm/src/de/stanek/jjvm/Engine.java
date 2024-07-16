
package de.stanek.jjvm;

import java.io.IOException;

import de.stanek.jjvm.heap.JJCode;
import de.stanek.jjvm.heap.CPMethodRef;
import de.stanek.jjvm.heap.CPNameAndType;
import de.stanek.jjvm.heap.ConstantPool;
import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJAttributeCode;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethod;
import de.stanek.jjvm.heap.JJStackFrame;


class  Engine
{

    Engine (Heap heap, Loader loader)
    {
        this.heap = heap;
        this.loader = loader;
    }
    private final Heap  heap;
    private final Loader  loader;

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
    private final static byte  istore           = (byte) 0x36;
    private final static byte  istore_0         = (byte) 0x3b;
    private final static byte  istore_1         = (byte) 0x3c;
    private final static byte  istore_2         = (byte) 0x3d;
    private final static byte  istore_3         = (byte) 0x3e;
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
    private final static byte  if_icmpeq        = (byte) 0x9f;
    private final static byte  if_icmpne        = (byte) 0xa0;
    private final static byte  if_icmplt        = (byte) 0xa1;
    private final static byte  if_icmple        = (byte) 0xa4;
    private final static byte  goto_            = (byte) 0xa7;
    private final static byte  ireturn          = (byte) 0xac;
    private final static byte  invokestatic     = (byte) 0xb8;

    void  invokestatic (JJClass jjClass, JJCode code, JJStackFrame sf)
        throws JJvmException, IOException
    {
        ConstantPool  cp = jjClass. cp;

        for (int counter = 0;  ;  ++counter)
        {
            byte  cmd = code. peek (counter);
            switch (cmd)
            {
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
                    ++counter;
                    byte  byte1 = code. peek (counter);
                    ++counter;
                    byte  byte2 = code. peek (counter);
                    int  value = (byte1 << 8) | (0xFF & byte2);
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
                case if_icmplt:
                {
                    int  if_counter = counter;
                    ++counter;
                    byte  branchbyte1 = code. peek (counter);
                    ++counter;
                    byte  branchbyte2 = code. peek (counter);
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 < value2)
                    {
                        int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
                        int  target = if_counter + offset;
                        counter = --target;
                    }
                    break;
                }
                case if_icmple:
                {
                    int  if_counter = counter;
                    ++counter;
                    byte  branchbyte1 = code. peek (counter);
                    ++counter;
                    byte  branchbyte2 = code. peek (counter);
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 <= value2)
                    {
                        int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
                        int  target = if_counter + offset;
                        counter = --target;
                    }
                    break;
                }
                case goto_:
                {
                    int  goto_counter = counter;
                    ++counter;
                    byte  branchbyte1 = code. peek (counter);
                    ++counter;
                    byte  branchbyte2 = code. peek (counter);
                    int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
                    int  target = goto_counter + offset;
                    counter = --target;
                    break;
                }
                case ireturn:
                {
                    return;
                }
                case invokestatic:
                {
                    JJClass  c;
                    JJMethod  m;
                    {
                        CPMethodRef  mr;
                        {
                            ++counter;
                            byte  indexbyte1 = code. peek (counter);
                            ++counter;
                            byte  indexbyte2 = code. peek (counter);
                            int  index = (indexbyte1 << 8) | (0xFF & indexbyte2);
                            mr = cp. methodref (index);
                        }
                        {
                            String  class_name = cp. className (mr.class_index);
                            c = loader. load (class_name);
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
                        }
                    }

                    JJCode  code2;
                    JJStackFrame  sf2;
                    {
                        JJAttributeCode  ac = m. attributeCode ();
                        code2 = ac. code();
                        sf2 = heap. createJJStackFrame (
                                                ac.max_stack, ac.max_locals);
                    }

                    for (int  i = m.params - 1;  i >= 0;  --i)
                        sf2. set (i, sf. pop ());

                    invokestatic (c, code2, sf2);

                    if (m.results == 1)
                        sf. push (sf2. pop ());
                    break;
                }
                case if_icmpeq:
                {
                    int  if_counter = counter;
                    ++counter;
                    byte  branchbyte1 = code. peek (counter);
                    ++counter;
                    byte  branchbyte2 = code. peek (counter);
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 == value2)
                    {
                        int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
                        int  target = if_counter + offset;
                        counter = --target;
                    }
                    break;
                }
                case if_icmpne:
                {
                    int  if_counter = counter;
                    ++counter;
                    byte  branchbyte1 = code. peek (counter);
                    ++counter;
                    byte  branchbyte2 = code. peek (counter);
                    int  value2 = sf. pop ();
                    int  value1 = sf. pop ();
                    if (value1 != value2)
                    {
                        int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
                        int  target = if_counter + offset;
                        counter = --target;
                    }
                    break;
                }
                default:
                    throw new JJvmException (
                            "Unknown code 0x" + Integer.toHexString(cmd & 0xff)
                                        + " at " + counter);
            }
        }
    }

}
