
package de.stanek.jjvm;

import java.io.IOException;


public class  Main
{

    public static void  main (String[] args)
        throws IOException, JJvmException
    {
        String  classRoot = "../sample/bin";
        String  mainClass = "sample/Simple";
        String  mainMethod = "calculate()I";

        JVM  jvm = new JVM (classRoot);

        int  result = jvm. execute (mainClass, mainMethod);

        System.out.println (result);
    }

}
