package com.eddie;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

/**
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP453StructuredConcurrency {

    private static final ScopedValue<Integer> VALUE = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(stringScope()));
    }

    public static Object[] stringScope() throws Exception {
        return ScopedValue.where(VALUE, 10000).call(() -> {
            // 使用 try-with-resource 来绑定结构化并发的作用域
            // 用于自动管理资源的生命周期，这是一个结构化任务范围
            // 在这个范围内创建的所有子任务都将被视为范围的一部分
            // 如果范围中的任何任务失败，所有其他任务都将被取消
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                // 使用了 scope.fork 来创建两个并行的任务
                // 每个任务都在执行上下文中获取 VALUE 的值，并对其进行操作
                StructuredTaskScope.Subtask<Integer> user  = scope.fork(VALUE::get);
                StructuredTaskScope.Subtask<Boolean> order = scope.fork(() -> VALUE.get().equals(10));
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
