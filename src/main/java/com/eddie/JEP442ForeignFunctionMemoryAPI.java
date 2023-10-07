package com.eddie;

import java.beans.FeatureDescriptor;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

/**
 * Foreign Function & Memory (FFM) API包含了两个incubating API
 * JDK14的JEP 370: Foreign-Memory Access API (Incubator)引入了Foreign-Memory Access API作为incubator
 * JDK15的JEP 383: Foreign-Memory Access API (Second Incubator)Foreign-Memory Access API作为第二轮incubator
 * JDK16的JEP 393: Foreign-Memory Access API (Third Incubator)作为第三轮，它引入了Foreign Linker API (JEP 389)
 * FFM API在JDK 17的JEP 412: Foreign Function & Memory API (Incubator)作为incubator引入
 * FFM API在JDK 18的JEP 419: Foreign Function & Memory API (Second Incubator)作为第二轮incubator
 * JDK19的JEP 424: Foreign Function & Memory API (Preview)则将FFM API作为preview API
 * JDK20的JEP 434: Foreign Function & Memory API (Second Preview)作为第二轮preview
 * JDK21则作为第三轮的preview，使用示例
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP442ForeignFunctionMemoryAPI {

    public static void main(String[] args) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            // 这里在堆外开辟了一段内存来存放字符串Panama，接下来copy到JVM栈上，最后输出
            MemorySegment cString = arena.allocateUtf8String("Panama");
            String jString = cString.getUtf8String(0L);
            System.out.println(jString);
        }

        downCallDemo();

        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();
        MethodHandle radixsort = linker.downcallHandle(stdlib.find("radixsort").get(),  FunctionDescriptor.of(JAVA_LONG, ADDRESS));
        // 2. Allocate on-heap memory to store four strings
        String[] javaStrings = {"mouse", "cat", "dog", "car"};
        // 3. Use try-with-resources to manage the lifetime of off-heap memory
        try (Arena offHeap = Arena.ofConfined()) {
            // 4. Allocate a region of off-heap memory to store four pointers
            MemorySegment pointers
                    = offHeap.allocateArray(ValueLayout.ADDRESS, javaStrings.length);
            // 5. Copy the strings from on-heap to off-heap
            for (int i = 0; i < javaStrings.length; i++) {
                MemorySegment cString = offHeap.allocateUtf8String(javaStrings[i]);
                pointers.setAtIndex(ValueLayout.ADDRESS, i, cString);
            }
            // 6. Sort the off-heap data by calling the foreign function
            radixsort.invoke(pointers, javaStrings.length, MemorySegment.NULL, '\0');
            // 7. Copy the (reordered) strings from off-heap to on-heap
            for (int i = 0; i < javaStrings.length; i++) {
                MemorySegment cString = pointers.getAtIndex(ValueLayout.ADDRESS, i);
                javaStrings[i] = cString.getUtf8String(0);
            }
        } // 8. All off-heap memory is deallocated here
        assert Arrays.equals(javaStrings,
                new String[]{"car", "cat", "dog", "mouse"});

    }

    private static void downCallDemo() throws Throwable {
        Linker linker = Linker.nativeLinker();
        MemorySegment strlen_address = linker.defaultLookup().find("strlen").get();
        MethodHandle strlen = linker.downcallHandle(
                strlen_address,
                FunctionDescriptor.of(JAVA_LONG, ADDRESS)
        );
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment cString = arena.allocateUtf8String("Hello");
            long len = (long) strlen.invokeExact(cString); // 5
            System.out.println(len);
        }
    }

}
