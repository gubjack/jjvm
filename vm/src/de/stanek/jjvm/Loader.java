
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

    Loader (Definer definer, String appClasses)
    {
        this.definer = definer;
        this.appClasses = appClasses;
    }
    private final Definer  definer;
    private final String  appClasses;

    JJClass  load (String name)
        throws IOException, JJvmException
    {
        JJClass  jjClass = map. get (name);
        if (jjClass != null)
            return jjClass;

        try (DataInputStream dis
                = new DataInputStream (
                        new BufferedInputStream (
                                Files.newInputStream(
                                        Path. of (appClasses
                                                , name + ".class")))))
        {
            jjClass = definer. define (dis);
            map. put (name, jjClass);
            return jjClass;
        }
    }
    private final HashMap <String, JJClass>  map = new HashMap<> ();

}
