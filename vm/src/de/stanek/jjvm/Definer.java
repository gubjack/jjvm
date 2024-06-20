
package de.stanek.jjvm;

import java.io.DataInputStream;
import java.io.IOException;

import de.stanek.jjvm.heap.ConstantPool;
import de.stanek.jjvm.heap.CPClass;
import de.stanek.jjvm.heap.CPMethodRef;
import de.stanek.jjvm.heap.CPNameAndType;
import de.stanek.jjvm.heap.CPUtf8;
import de.stanek.jjvm.heap.Heap;
import de.stanek.jjvm.heap.JJAttribute;
import de.stanek.jjvm.heap.JJAttributes;
import de.stanek.jjvm.heap.JJClass;
import de.stanek.jjvm.heap.JJMethods;


class  Definer
{

    Definer (Heap heap)
    {
        this.heap = heap;
    }
    private final Heap  heap;

    JJClass define (DataInputStream dis)
        throws IOException, JJvmException
    {
        int  magic = dis. readInt ();
        if (magic != 0xCAFEBABE)
            throw new JJvmException ("Invalid magic");
        @SuppressWarnings("unused")
        short  minor_version = dis. readShort ();
        @SuppressWarnings("unused")
        short  major_version = dis. readShort ();

        ConstantPool  constantPool = readConstantPool (dis);
//        System.out.println (constantPool);

        @SuppressWarnings("unused")
        short  access_flags = dis. readShort ();
        @SuppressWarnings("unused")
        short  this_class = dis. readShort ();
        @SuppressWarnings("unused")
        short  super_class = dis. readShort ();

        short  interfaces_count = dis. readShort ();
        if (interfaces_count > 0)
            throw new JJvmException ("Interfaces implemented");
        short  fields_count = dis. readShort ();
        if (fields_count > 0)
            throw new JJvmException ("Fields present");

        JJMethods  methods = readMethods (dis, constantPool);

        return heap. createJJClass (constantPool, methods);
    }

    private ConstantPool  readConstantPool (DataInputStream dis)
        throws IOException, JJvmException
    {
        short  constant_pool_count = dis. readShort ();
        ConstantPool  cp = heap. createConstantPool (constant_pool_count);

        for (short  index = 1;  index < constant_pool_count;  ++index)
        {
            byte  tag = dis. readByte ();
            switch (tag)
            {
                case ConstantPool.CONSTANT_Utf8:
                {
                    short  length = dis. readShort ();
                    byte[]  bytes = new byte [length];
                    dis. read (bytes);
                    CPUtf8  info = heap. createCPUtf8 (length, bytes);
                    cp. set (index, tag, info);
                    break;
                }
                case ConstantPool.CONSTANT_Class:
                {
                    short  name_index = dis. readShort ();
                    CPClass  info = heap. createCPClass (name_index);
                    cp. set (index, tag, info);
                    break;
                }
                case ConstantPool.CONSTANT_MethodRef:
                {
                    short  class_index = dis. readShort ();
                    short  name_and_type_index = dis. readShort ();
                    CPMethodRef  info = heap. createCPMethodRef (
                                            class_index, name_and_type_index);
                    cp. set (index, tag, info);
                    break;
                }
                case ConstantPool.CONSTANT_NameAndType:
                {
                    short  name_index = dis. readShort ();
                    short  descriptor_index = dis. readShort ();
                    CPNameAndType  info = heap. createCPNameAndType (
                                                name_index, descriptor_index);
                    cp. set (index, tag, info);
                    break;
                }
                default:
                    throw new JJvmException (
                            "Unknown constant pool tag " + tag);
            }
        }
        return cp;
    }

    private JJMethods  readMethods (DataInputStream dis, ConstantPool cp)
        throws IOException, JJvmException
    {
        short  methods_count = dis. readShort ();
        JJMethods  methods = heap. createJJMethods (methods_count);

        for (short  index = 0;  index < methods_count;  ++index)
        {
            @SuppressWarnings("unused")
            short  access_flags2 = dis. readShort ();

            short  name_index = dis. readShort ();
            String  name = cp. utf8 (name_index);

            short  descriptor_index = dis. readShort ();
            String  descriptor = cp. utf8 (descriptor_index);

            int  params = 0;
            for (int  i = 1;  i < descriptor.length ();  ++i)
            {
                char  c = descriptor. charAt (i);
                if (c == ')')
                    break;
                ++params;
            }
            int  results = 0;
            if (descriptor. charAt (descriptor. length () - 1) == 'I')
                results = 1;

            JJAttributes  jjAttributes = readAttributes (dis, cp);
            methods. set (index, 
                            heap. createJJMethod (name, descriptor
                                            , jjAttributes, params, results));
        }
        return methods;
    }
    private JJAttributes  readAttributes (DataInputStream dis, ConstantPool cp)
        throws IOException, JJvmException
    {
        short  attributes_count = dis. readShort ();
        JJAttributes  jjAttributes
                                = heap. createJJAttributes (attributes_count);

        for (short  index = 0;  index < attributes_count;  ++index)
        {
            short  attribute_name_index = dis. readShort ();
            String  attribute_name = cp. utf8 (attribute_name_index);

            int  attributes_length = dis. readInt ();
            switch (attribute_name)
            {
                case JJAttribute.CODE:
                {
                    short  max_stack = dis. readShort ();
                    short  max_locals = dis. readShort ();

                    int  code_length = dis. readInt ();
                    byte[]  code = new byte [code_length];
                    dis. read (code);

                    short  exception_table_length = dis. readShort ();
                    if (exception_table_length > 0)
                        throw new JJvmException ("Exception table present");

                    short  attributes_count2 = dis. readShort ();
                    for (short  index3 = 0;  index3 < attributes_count2
                            ;  ++index3)
                    {
                        short  attribute_name_index2 = dis. readShort ();
                        cp. assertUtf8 (attribute_name_index2);

                        int  attributes_length2 = dis. readInt ();
                        dis. skipBytes(attributes_length2);
                    }

                    jjAttributes. set (index
                            , heap. createJJAttributeCode (attribute_name
                                                , max_stack, max_locals, code));
                    break;
                }
                default:
                    dis. skipBytes(attributes_length);
            }
        }
        return jjAttributes;
    }

}
