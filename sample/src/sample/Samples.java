
package sample;

import jjvm.lang.Console;


@SuppressWarnings("unused")
//@Deprecated
class  Samples
    extends Super
    implements Face, Face2
{

    public int  reckon ()
    {
        return 0;
    }

    public static int  run ()
    {
//        return constants ();
//        return locals ();
//        return maths ();
//        return bits ();
//        return branching ();
//        return fields ();
        return invocations ();
//        return instances ();

//        return Fibunacci.straight(9);
//        return Fibunacci.recursive(9);
    }

    private static int  constants ()
    {
        // iconst_0, ireturn
        return 0;
        // iconst_1, ireturn
//        return 1;
        // iconst_2, ireturn
//        return 2;
        // iconst_3, ireturn
//        return 3;
        // iconst_4, ireturn
//        return 4;
        // iconst_5, ireturn
//        return 5;
        // iconst_m1, ireturn
//        return -1;
        // bipush, ireturn
//        return 6;
        // bipush, ireturn
//        return 6*7;
        // sipush, ireturn
//        return - 128 * 256;        // -32768
        // sipush, ireturn
//        return 128 * 256 - 1;    // 32767
        // ldc, ireturn
//        return 128 * 256;        // 32768
        // ldc, ireturn
//        return - 128 * 256 - 1;    // -32769
        // ldc, ireturn
//        return 128 * 256 * 256 * 256 - 1;   // 2147483647
        // ldc, ireturn
//        return 128 * 256 * 256 * 256;   // -2147483648
        // iconst_0, ireturn
//        return 256 * 256 * 256 * 256;   // 0
    }

    private static int  locals ()
    {
        // ..., istore_0, iload_0, ireturn
        int  i = 0;  return i;
        // ..., istore_1, iload_1, ireturn
//        int  j = 0, i = 0;  return i;
        // ..., istore_2, iload_2, ireturn
//        int  k = 0, j = 0, i = 0;  return i;
        // ..., istore_3, iload_3, ireturn
//        int  l = 0, k = 0, j = 0, i = 0;  return i;
        // ..., istore_4, iload_4, ireturn
//        int  m = 0, l = 0, k = 0, j = 0, i = 0;  return i;
    }

    private static int  maths ()
    {
        // ..., iadd, ireturn
        int  i = 1;  return i + 2;
        // ..., isub, ireturn
//        int  i = 1;  return i - 2;
        // ..., iinc 0 1, ...
//        int  i = 0;  return ++i;
        // ..., iinc 0 2, ...
//        int  i = 0;  i = i + 2;  return i;
        // ..., iinc 0 -22, ...
//        int  i = 0;  i = i - 2;  return i;
        // ..., imul, ireturn
//        int  i = 3;  return i * 2;
        // ..., ineg, ireturn
//        int  i = 3;  return -i;
    }

    private static int  bits ()
    {
        // ..., ishl, ireturn
        int  i = 3;  return i << 2;
        // ..., ishr, ireturn
//        int  i = 12;  return i >> 2;
        // ..., ishr, ireturn
//        int  i = 0x80000000;  return i >> 31;
        // ..., iushr, ireturn
//        int  i = 0x80000000;  return i >>> 31;
        // ..., iand, ireturn
//        int  i = 0xf0;  return i & 0x0f;
        // ..., ior, ireturn
//        int  i = 0xf0;  return i | 0x0f;
        // ..., ixor, ireturn
//        int  i = 0x1e;  return i ^ 0x0f;
    }

    private static int  branching ()
    {
        // ..., ifeq, ...
        int  i = 0;  if (i != 0)  return 0;  return 1;
        // ..., ifne, ...
//        int  i = 0;  if (i == 0)  return 0;  return 1;
        // ..., iflt, ...
//        int  i = 0;  if (i >= 0)  return 0;  return 1;
        // ..., ifge, ...
//        int  i = 0;  if (i < 0)  return 0;  return 1;
        // ..., ifgt, ...
//        int  i = 0;  if (i <= 0)  return 0;  return 1;
        // ..., ifle, ...
//        int  i = 0;  if (i > 0)  return 0;  return 1;
        // ..., if_icmpeq, ...
//        int  i = 0;  if (i != 1)  return 0;  return 1;
        // ..., if_icmpne, ...
//        int  i = 0;  if (i == 1)  return 0;  return 1;
        // ..., if_icmplt, ...
//        int  i = 0;  if (i >= 1)  return 0;  return 1;
        // ..., if_icmpge, ...
//        int  i = 0;  if (i < 1)  return 0;  return 1;
        // ..., if_icmpgt, ...
//        int  i = 0;  if (i <= 1)  return 0;  return 1;
        // ..., if_icmple, ...
//        int  i = 0;  if (i > 1)  return 0;  return 1;
        // ..., goto, ...
//        for (int i = 0;  i < 2;  ++i) {}  return 0;
    }

    private static int  fields ()
    {
        // "<clinit>", putstatic, return, ..., getstatic, ireturn
        return field;
        // ..., getstatic (super class field), ireturn
//        return super_field;
    }
    private static int  field = 42;

    private static int  invocations ()
    {
        // ..., invokestatic, ...
//        return method ();
        // ..., invokestatic (native), ...
//        println(99);  return 0;
        // ..., invokestatic (other class), ...
//        Console.println(88);  return 0;
        // ..., invokevirtual, ...
        return new Samples (). super_reckon ();
    }
    private static int  method ()
    {
        return 0;
    }
    private native static void  println (int value);

    private static int  instances ()
    {
        // new, dup, invokespecial, astore_0, ..., nop, return, ..., aload_0
        Data  d = new Data ();  return 0;
    }

}
