package com.eddie;

/**
 * ZGC分代回收无疑是一个重磅的GC特性，ZGC之前的版本不支持分代回收
 * 此次支持分代回收的话，可以更方便地对年轻代进行收集，提高GC性能
 * 目前是分代与非分代都支持，使用分代则通过-XX:+UseZGC-XX:+ZGenerational开启
 * 后续版本将会把分代设置为默认的，而-XX:-ZGenerational用于开启非分代
 * 最后将会废除非分代的支持，届时ZGenerational参数也就没有作用了
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP439GenerationalZGC {
}
