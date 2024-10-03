
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

        JVM  jvm = new JVM (bootClasses, appClasses);

        int  result = jvm. executeMain (clazz);

        System.out.println (result);
    }

}
