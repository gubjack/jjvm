
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
    public int  branch (int counter)
    {
        int  start_counter = counter;
        ++counter;
        byte  branchbyte1 = code [counter];
        ++counter;
        byte  branchbyte2 = code [counter];
        int  offset = (branchbyte1 << 8) | (0xFF & branchbyte2);
        int  target = start_counter + offset;
        return --target;
    }

}
