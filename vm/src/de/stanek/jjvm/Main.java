
package de.stanek.jjvm;

import java.io.IOException;


public class  Main
{

    public static void  main (String[] args)
        throws IOException, JJvmException
    {
        String  appClasses = "../sample/bin";
        String  bootClasses = "boot";
        String  clazz = "sample/Simple";

        Diagnose  diag =  Boolean. getBoolean ("jjvm.diagnose")
                            ?  new Diagnose ()
                            :  null;

        JVM  jvm = new JVM (diag, bootClasses, appClasses);

        jvm. executeMain (clazz);

        jvm. cleanup ();

        if (diag != null)
            diag. sleep ();
    }

}
