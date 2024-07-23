
package de.stanek.jjvm.heap;


public class  JJCode
{

    JJCode (byte[] code)
    {
        this.code = code;
    }
    public final byte[]  code;
    public byte  peek (int counter)
    {
        return code [counter];
    }
    public short  nextShort (int counter)
    {
        ++counter;
        byte  byte1 = code [counter];
        ++counter;
        byte  byte2 = code [counter];
        return (short) ((byte1 << 8) | (0xFF & byte2));
    }
    public int  branch (int counter)
    {
        short  offset = nextShort (counter);
        return counter + offset - 1;
    }

}
