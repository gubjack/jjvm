
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
    public short  getShort (int counter)
    {
        byte  byte1 = code [counter];
        ++counter;
        byte  byte2 = code [counter];
        return (short) ((byte1 << 8) | (0xFF & byte2));
    }

}
