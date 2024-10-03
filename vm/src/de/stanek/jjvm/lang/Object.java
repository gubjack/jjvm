
package de.stanek.jjvm.lang;

/*

= Adapt this and super class name =

javap -v vm/bin/de/stanek/jjvm/lang/Object.class
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #1                          // de/stanek/jjvm/lang/Object
  super_class: #3                         // java/lang/Object
hexdump -C vm/bin/de/stanek/jjvm/lang/Object.class
  000000c0  65 63 74 2e 6a 61 76 61  00 21 00 01 00 03 00 00  |ect.java.!......|
change to
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #3                          // java/lang/Object
  super_class: #0
  000000c0  65 63 74 2e 6a 61 76 61  00 21 00 03 00 00 00 00  |ect.java.!......|
with
  printf '\x00\x03\x00\x00' | dd of=vm/bin/de/stanek/jjvm/lang/Object.class bs=1 seek=202 count=4 conv=notrunc

= Copy to boot classes directory =

  cp vm/bin/de/stanek/jjvm/lang/Object.class vm/boot/java/lang/

*/

public class  Object {

}
