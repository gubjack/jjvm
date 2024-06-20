
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

    Loader (Definer definer, String classRoot)
    {
        this.definer = definer;
        this.classRoot = classRoot;
    }
    private final Definer  definer;
    private final String  classRoot;

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
                                        Path. of (classRoot
                                                , name + ".class")))))
        {
            jjClass = definer. define (dis);
            map. put (name, jjClass);
            return jjClass;
        }
    }
    private final HashMap <String, JJClass>  map = new HashMap<> ();

}
