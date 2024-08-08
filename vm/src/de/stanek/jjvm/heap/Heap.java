
package de.stanek.jjvm.heap;

import de.stanek.jjvm.JJvmException;

public class  Heap
{

    // +++ class data +++

    // constant pool
    public ConstantPool  createConstantPool (short constant_pool_count)
    {
        return new ConstantPool (constant_pool_count);
    }
    public CPUtf8  createCPUtf8 (short length, byte[] bytes)
    {
        String  stringOnHeap = new String (bytes);
        return new CPUtf8 (length, stringOnHeap);
    }
    public CPInteger  createCPInteger (int value)
    {
        return new CPInteger (value);
    }
    public CPClass  createCPClass (short name_index)
    {
        return new CPClass (name_index);
    }
    public CPFieldRef  createCPFieldRef (short class_index
                                        , short name_and_type_index)
    {
        return new CPFieldRef (class_index, name_and_type_index);
    }
    public CPMethodRef  createCPMethodRef (short class_index
                                            , short name_and_type_index)
    {
        return new CPMethodRef (class_index, name_and_type_index);
    }
    public CPNameAndType  createCPNameAndType (short name_index
                                                , short descriptor_index)
    {
        return new CPNameAndType (name_index, descriptor_index);
    }

    // attributes
    public JJAttributes  createJJAttributes (short attributes_count)
    {
        return new JJAttributes (attributes_count);
    }
    public JJAttributeCode  createJJAttributeCode (String name, short max_stack
                                                    , short max_locals
                                                    , byte[] code)
    {
        byte[]  codeOnHeap = new byte [code.length];
        System.arraycopy(code, 0, codeOnHeap, 0, code.length);
        JJCode  jjCode = new JJCode (codeOnHeap);
        return new JJAttributeCode (name, max_stack, max_locals, jjCode);
    }

    // fields
    public JJFields  createJJFields (short fields_count)
    {
        return new JJFields (fields_count);
    }
    public JJField  createJJField (short access_flags
                                    , String name, String descriptor
                                    , JJAttributes attributes)
    {
        return new JJField (access_flags, name, descriptor, attributes);
    }

    // methods
    public JJMethods  createJJMethods (short methods_count)
    {
        return new JJMethods (methods_count);
    }
    public JJMethod  createJJMethod (short access_flags
                                    , String name, String descriptor
                                    , JJAttributes attributes
                                    , int params, int results)
    {
        return new JJMethod (access_flags, name, descriptor, attributes
                            , params, results);
    }

    // class
    public JJClass  createJJClass (ConstantPool cp, short this_class
                                    , JJMethods methods, JJFields fields)
        throws JJvmException
    {
        return new JJClass (cp, this_class, methods, fields);
    }


    // +++ runtime data +++

    // stack frame
    public JJStackFrame  createJJStackFrame (int max_stack, int max_locals)
    {
        int[]  locals = new int[max_locals];
        int[]  stack = new int[max_stack];
        return new JJStackFrame(locals, stack);
    }

}
