
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

    // methods
    public JJMethods  createJJMethods (short methods_count)
    {
        return new JJMethods (methods_count);
    }
    public JJMethod  createJJMethod (String name, String descriptor
                                    , JJAttributes attributes
                                    , int params, int results)
    {
        return new JJMethod (name, descriptor, attributes, params, results);
    }

    // class
    public JJClass  createJJClass (ConstantPool cp, JJMethods methods)
        throws JJvmException
    {
        return new JJClass (cp, methods);
    }


    // +++ runtime data +++

    // stack frame
    public JJStackFrame  createJJStackFrame (short max_stack, short max_locals)
    {
        int[]  locals = new int[max_locals];
        int[]  stack = new int[max_stack];
        return new JJStackFrame(locals, stack);
    }

}
