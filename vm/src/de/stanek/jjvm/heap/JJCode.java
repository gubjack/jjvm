
package de.stanek.jjvm.heap;


public class  JJCode
{

    JJCode (byte[] code)
    {
        this.code = code;
    }
    public final byte[]  code;
    public byte  peek (int count)
    {
        return code [count];
    }

}
