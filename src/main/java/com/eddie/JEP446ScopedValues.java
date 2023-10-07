package com.eddie;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

/**
 * Scoped Values在JDK20的JEP 429: Scoped Values (Incubator)作为Incubator
 * 此次在JDK21作为preview版本
 * ScopedValue是一种类似ThreadLocal的线程内/父子线程传递变量的更优方案。
 * ThreadLocal提供了一种无需在方法参数上传递通用变量的方法，
 * InheritableThreadLocal使得子线程可以拷贝继承父线程的变量。
 * 但是ThreadLocal提供了set方法，变量是可变的，另外remove方法很容易被忽略，导致在线程池场景下很容易造成内存泄露
 * ScopedValue则提供了一种不可变、不拷贝的方案，即不提供set方法，子线程不需要拷贝就可以访问父线程的变量。具体使用如下
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP446ScopedValues {
    /**
     * Snapshot
     * An immutable map from ScopedValue to values. Unless otherwise specified, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown.
     * Snapshot是一个从ScopedValue到值的不可变映射。除非特别说明，否则将null参数传递给这个类的构造器或方法会导致抛出NullPointerException异常。这个类的主要用途是为ScopedValue实例创建一个不可变的映射，这样在运行时，无论其它代码如何修改原始的ScopedValue实例，Snapshot中的值都不会发生变化。它为了提供一个安全的方式来在多线程环境下共享值。
     *
     * Carrier
     * A mapping of scoped values, as keys, to values. A Carrier is used to accumlate mappings so that an operation (a Runnable or Callable) can be executed with all scoped values in the mapping bound to values. The following example runs an operation with k1 bound (or rebound) to v1, and k2 bound (or rebound) to v2. ScopedValue.where(k1, v1).where(k2, v2).run(() -> ... ); A Carrier is immutable and thread-safe. The where method returns a new Carrier object, it does not mutate an existing mapping. Unless otherwise specified, passing a null argument to a method in this class will cause a NullPointerException to be thrown.
     * Carrier类用于累积映射，以便可以执行一个操作（Runnable或Callable），在该操作中，映射中的所有scoped values都绑定到值。Carrier是不可变的，并且是线程安全的。where方法返回一个新的Carrier对象，不会改变现有的映射。这是用于在ScopedValue实例和对应值之间创建和保持映射关系的工具，使得这些映射关系可以在执行操作时被一并应用。
     *
     * Cache
     * A small fixed-size key-value cache. When a scoped value's get() method is invoked, we record the result of the lookup in this per-thread cache for fast access in future.
     * Cache是一个小型的固定大小的键值缓存。当调用一个scoped value的get()方法时，我们在这个每线程缓存中记录查找的结果，以便在将来快速访问。这个类的主要作用是优化性能。通过缓存get()方法的结果，可以避免在多次获取同一个ScopedValue的值时进行重复的查找操作。只有当ScopedValue的值被更改时，才需要更新缓存。
     */
    public final static ScopedValue<String> LOGGED_IN_USER = ScopedValue.newInstance();

    void main() throws Exception {
        String loggedInUser = "eddie";
        ScopedValue.where(LOGGED_IN_USER, loggedInUser)
                .run(() -> System.out.println(LOGGED_IN_USER.get()));

        System.out.println(Arrays.toString(stringScope()));
    }

    // 声明了一个静态的、最终的 ScopedValue<String> 实例
    // ScopedValue 是一个支持在特定范围内（如任务或线程）中传递值的类
    // 它的使用类似于 ThreadLocal，但更适合于结构化并发
    private static final ScopedValue<String> VALUE = ScopedValue.newInstance();

    public static Object[] stringScope() throws Exception {
        return ScopedValue.where(VALUE, "value").call(() -> {
            // 使用 try-with-resource 来绑定结构化并发的作用域
            // 用于自动管理资源的生命周期，这是一个结构化任务范围
            // 在这个范围内创建的所有子任务都将被视为范围的一部分
            // 如果范围中的任何任务失败，所有其他任务都将被取消
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                // 使用了 scope.fork 来创建两个并行的任务
                // 每个任务都在执行上下文中获取 VALUE 的值，并对其进行操作
                StructuredTaskScope.Subtask<String> user  = scope.fork(VALUE::get);
                StructuredTaskScope.Subtask<Integer> order = scope.fork(() -> VALUE.get().length());
                // join() 方法等待所有范围内的任务完成
                // throwIfFailed() 方法会检查所有任务的结果，如果任何任务失败，则会抛出异常
                try {
                    scope.join().throwIfFailed();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 在所有任务完成后，使用 resultNow() 方法获取每个任务的结果，并将结果放入一个对象数组中
                return new Object[]{user.get(), order.get()};
            }
        });
    }
}
