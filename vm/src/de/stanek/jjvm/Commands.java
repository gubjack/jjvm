
package de.stanek.jjvm;


class  Commands
{

    final static byte  nop              = (byte) 0x00;
    final static byte  iconst_m1        = (byte) 0x02;
    final static byte  iconst_0         = (byte) 0x03;
    final static byte  iconst_1         = (byte) 0x04;
    final static byte  iconst_2         = (byte) 0x05;
    final static byte  iconst_3         = (byte) 0x06;
    final static byte  iconst_4         = (byte) 0x07;
    final static byte  iconst_5         = (byte) 0x08;
    final static byte  bipush           = (byte) 0x10;
    final static byte  sipush           = (byte) 0x11;
    final static byte  ldc              = (byte) 0x12;
    final static byte  iload            = (byte) 0x15;
    final static byte  iload_0          = (byte) 0x1a;
    final static byte  iload_1          = (byte) 0x1b;
    final static byte  iload_2          = (byte) 0x1c;
    final static byte  iload_3          = (byte) 0x1d;
    final static byte  aload_0          = (byte) 0x2a;
    final static byte  istore           = (byte) 0x36;
    final static byte  istore_0         = (byte) 0x3b;
    final static byte  istore_1         = (byte) 0x3c;
    final static byte  istore_2         = (byte) 0x3d;
    final static byte  istore_3         = (byte) 0x3e;
    final static byte  astore_0         = (byte) 0x4b;
    final static byte  dup              = (byte) 0x59;
    final static byte  iadd             = (byte) 0x60;
    final static byte  isub             = (byte) 0x64;
    final static byte  imul             = (byte) 0x68;
    final static byte  ineg             = (byte) 0x74;
    final static byte  ishl             = (byte) 0x78;
    final static byte  ishr             = (byte) 0x7a;
    final static byte  iushr            = (byte) 0x7c;
    final static byte  iand             = (byte) 0x7e;
    final static byte  ior              = (byte) 0x80;
    final static byte  ixor             = (byte) 0x82;
    final static byte  iinc             = (byte) 0x84;
    final static byte  ifeq             = (byte) 0x99;
    final static byte  ifne             = (byte) 0x9a;
    final static byte  iflt             = (byte) 0x9b;
    final static byte  ifge             = (byte) 0x9c;
    final static byte  ifgt             = (byte) 0x9d;
    final static byte  ifle             = (byte) 0x9e;
    final static byte  if_icmpeq        = (byte) 0x9f;
    final static byte  if_icmpne        = (byte) 0xa0;
    final static byte  if_icmplt        = (byte) 0xa1;
    final static byte  if_icmpge        = (byte) 0xa2;
    final static byte  if_icmpgt        = (byte) 0xa3;
    final static byte  if_icmple        = (byte) 0xa4;
    final static byte  goto_            = (byte) 0xa7;
    final static byte  ireturn          = (byte) 0xac;
    final static byte  return_          = (byte) 0xb1;
    final static byte  getstatic        = (byte) 0xb2;
    final static byte  putstatic        = (byte) 0xb3;
    final static byte  invokevirtual    = (byte) 0xb6;
    final static byte  invokespecial    = (byte) 0xb7;
    final static byte  invokestatic     = (byte) 0xb8;
    final static byte  new_             = (byte) 0xbb;

    static String  cmdToString (byte cmd)
    {
        switch (cmd)
        {
            case nop:               return "nop";
            case iconst_m1:         return "iconst_m1";
            case iconst_0:          return "iconst_0";
            case iconst_1:          return "iconst_1";
            case iconst_2:          return "iconst_2";
            case iconst_3:          return "iconst_3";
            case iconst_4:          return "iconst_4";
            case iconst_5:          return "iconst_5";
            case bipush:            return "bipush";
            case sipush:            return "sipush";
            case ldc:               return "ldc";
            case iload:             return "iload";
            case iload_0:           return "iload_0";
            case iload_1:           return "iload_1";
            case iload_2:           return "iload_2";
            case iload_3:           return "iload_3";
            case aload_0:           return "aload_0";
            case istore:            return "istore";
            case istore_0:          return "istore_0";
            case istore_1:          return "istore_1";
            case istore_2:          return "istore_2";
            case istore_3:          return "istore_3";
            case astore_0:          return "astore_0";
            case dup:               return "dup";
            case iadd:              return "iadd";
            case isub:              return "isub";
            case imul:              return "imul";
            case ineg:              return "ineg";
            case ishl:              return "ishl";
            case ishr:              return "ishr";
            case iushr:             return "iushr";
            case iand:              return "iand";
            case ior:               return "ior";
            case ixor:              return "ixor";
            case iinc:              return "iinc";
            case ifeq:              return "ifeq";
            case ifne:              return "ifne";
            case iflt:              return "iflt";
            case ifge:              return "ifge";
            case ifgt:              return "ifgt";
            case ifle:              return "ifle";
            case if_icmpeq:         return "if_icmpeq";
            case if_icmpne:         return "if_icmpne";
            case if_icmplt:         return "if_icmplt";
            case if_icmpge:         return "if_icmpge";
            case if_icmpgt:         return "if_icmpgt";
            case if_icmple:         return "if_icmple";
            case goto_:             return "goto";
            case ireturn:           return "ireturn";
            case return_:           return "return";
            case getstatic:         return "getstatic";
            case putstatic:         return "putstatic";
            case invokevirtual:     return "invokevirtual";
            case invokespecial:     return "invokespecial";
            case invokestatic:      return "invokestatic";
            case new_:              return "new";
            default:
                return "<command 0x" + Integer.toHexString(cmd & 0xff) + ">";
        }
    }

}
