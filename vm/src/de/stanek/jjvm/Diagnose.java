
package de.stanek.jjvm;


public class  Diagnose
{

    public void  out (String msg)
    {
        System.out. println (msg);
    }

    public void  sleep ()
    {
        try
        {
            Thread. sleep (1_500);
        }
        catch (InterruptedException e)
        {
            e. printStackTrace ();
        }
    }

}
