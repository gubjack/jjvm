
package de.stanek.jjvm;

import java.io.IOException;


public class  Main
{

    public static void  main (String[] args)
        throws IOException, JJvmException
    {
        String  classRoot = "../sample/bin";
        String  clazz = "sample/Simple";
        String  name = "calculate";
        String  descriptor = "()I";

        JVM  jvm = new JVM (classRoot);

        int  result = jvm. execute (clazz, name, descriptor);

        System.out.println (result);
    }

}
