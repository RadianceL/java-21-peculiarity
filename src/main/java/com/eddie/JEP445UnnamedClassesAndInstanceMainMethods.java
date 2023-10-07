package com.eddie;

/**
 * 命名的类和实例main方法这个特性可以简化hello world示例
 * main方法选择的优先顺序是static的优于非static的，有args的优于没有args的
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP445UnnamedClassesAndInstanceMainMethods {
//    static void main(String[] args) {
//        System.out.println("static main with args");
//    }
//
//    static void main() {
//        System.out.println("static main without args");
//    }
//
//    void main(String[] args) {
//        System.out.println("main with args");
//    }

    void main() {
        System.out.println("main without args");
    }
}
