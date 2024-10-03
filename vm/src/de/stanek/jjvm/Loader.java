
package de.stanek.jjvm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import de.stanek.jjvm.heap.JJClass;

class  Loader
{

    Loader (Definer definer, String bootClasses, String appClasses)
    {
        this.definer = definer;
        this.bootClasses = bootClasses;
        this.appClasses = appClasses;
    }
    private final Definer  definer;
    private final String  bootClasses;
    private final String  appClasses;

    JJClass  load (String name)
        throws IOException, JJvmException
    {
        JJClass  jjClass = map. get (name);
        if (jjClass != null)
            return jjClass;

        Path  pathBoot = Path. of (bootClasses, name + ".class");
        if (Files. exists (pathBoot))
            return load (name, pathBoot);
        return load (name, Path. of (appClasses, name + ".class"));
    }
    private final HashMap <String, JJClass>  map = new HashMap<> ();

    private JJClass  load (String name, Path path)
        throws IOException, JJvmException
    {
        try (DataInputStream dis
                = new DataInputStream (
                        new BufferedInputStream (
                                Files.newInputStream(path))))
        {
            JJClass  jjClass = definer. define (dis);
            map. put (name, jjClass);
            return jjClass;
        }
    }

}
