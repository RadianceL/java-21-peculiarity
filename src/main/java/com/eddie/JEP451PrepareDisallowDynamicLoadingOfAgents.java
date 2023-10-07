package com.eddie;

/**
 * 对将代理动态加载到正在运行的 JVM 中时发出警告，后续版本将不允许动态加载agent。
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP451PrepareDisallowDynamicLoadingOfAgents {
    // 在 JDK 9 及更高版本中，可以通过-XX:-EnableDynamicAgentLoading禁止动态加载agent。
    // 在 JDK 21 中，允许动态加载agent，但 JVM 会在发生时发出警告。例如：
    // WARNING: A {Java,JVM TI} agent has been loaded dynamically (file:/u/bob/agent.jar)
    // WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
    // WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
    // WARNING: Dynamic loading of agents will be disallowed by default in a future release

    // 若要允许工具动态加载agent而不发出警告，用户必须在命令行上使用-XX:+EnableDynamicAgentLoading
}
