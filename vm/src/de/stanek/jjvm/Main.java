
package de.stanek.jjvm;

import java.io.IOException;


public class  Main
{

    public static void  main (String[] args)
        throws IOException, JJvmException
    {
        String  classRoot = "../sample/bin";
        String  clazz = "sample/Simple";

        JVM  jvm = new JVM (classRoot);

        int  result = jvm. executeMain (clazz);

        System.out.println (result);
    }

}
