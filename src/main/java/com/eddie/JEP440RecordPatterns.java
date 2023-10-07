package com.eddie;

/**
 * JDK19的JEP 405: Record Patterns (Preview)将Record的模式匹配作为第一次preview
 * JDK20的JEP 432: Record Patterns (Second Preview)作为第二次preview
 * 此次在JDK21则作为正式版本发布，使用示例如下
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP440RecordPatterns {

    public static void main(String[] args) {
        Point point = new Point(1, 2);
        System.out.println(STR."point desc: \{printSum(point)}");

        ColoredPoint coloredPoint = new ColoredPoint(point, Color.RED);
        Rectangle rectangle = new Rectangle(coloredPoint, coloredPoint);
        printColorOfUpperLeftPoint(rectangle);
    }

    record Point(int x, int y) {}

    // As of Java 21
    static Integer printSum(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            return x + y;
        }
        return null;
    }

    enum Color { RED, GREEN, BLUE }

    record ColoredPoint(Point p, Color c) {}

    record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}

    // As of Java 21
    static void printUpperLeftColoredPoint(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint ul, ColoredPoint lr)) {
            System.out.println(ul.c());
        }
    }

    static void printColorOfUpperLeftPoint(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint(Point p, Color c), ColoredPoint lr)) {
            System.out.println(c);
        }
    }
}
