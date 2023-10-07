package com.eddie;

/**
 * 在JDK14JEP 305: Pattern Matching for instanceof (Preview)作为preview
 * 在JDK15JEP 375: Pattern Matching for instanceof (Second Preview)作为第二轮的preview
 * 在JDK16JEP 394: Pattern Matching for instanceof转正
 * JDK17引入JEP 406: Pattern Matching for switch (Preview)
 * JDK18的JEP 420: Pattern Matching for switch (Second Preview)则作为第二轮preview
 * JDK19的JEP 427: Pattern Matching for switch (Third Preview)作为第三轮preview
 * JDK20的JEP 433: Pattern Matching for switch (Fourth Preview)作为第四轮preview
 * 而此次JDK21将Pattern Matching for switch作为正式版本发布，示例如下
 *
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP441PatternMatchingForSwitch {
    static String formatter(Object obj) {
        String formatted = "unknown";
        if (obj instanceof Integer i) {
            formatted = String.format("int %d", i);
        } else if (obj instanceof Long l) {
            formatted = String.format("long %d", l);
        } else if (obj instanceof Double d) {
            formatted = String.format("double %f", d);
        } else if (obj instanceof String s) {
            formatted = String.format("String %s", s);
        }
        return formatted;
    }

    // As of Java 21
    static String formatterPatternSwitch(Object obj) {
        return switch (obj) {
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> obj.toString();
        };
    }

    // As of Java 21
    static void testFooBarNew(String s) {
        switch (s) {
            case null         -> System.out.println("Oops");
            case "Foo", "Bar" -> System.out.println("Great");
            default           -> System.out.println("Ok");
        }
    }

    // As of Java 21
    static void testStringEnhanced(String response) {
        switch (response) {
            case null -> { }
            case "y", "Y" -> System.out.println("You got it");
            case "n", "N" -> System.out.println("Shame");
            case String s when s.equalsIgnoreCase("YES") && s.contains("Y") -> System.out.println("You got it");
            case String s when s.equalsIgnoreCase("NO") -> System.out.println("Shame");
            case String s -> System.out.println("Sorry?");
        }
    }


    // As of Java 21
    sealed interface Currency permits Coin {}
    enum Coin implements Currency { HEADS, TAILS }

    static void goodEnumSwitch1(Currency c) {
        switch (c) {
            // Qualified name of enum constant as a label
            case Coin.HEADS -> System.out.println("Heads");
            case Coin.TAILS -> System.out.println("Tails");
        }
    }

    static void goodEnumSwitch2(Coin c) {
        switch (c) {
            case HEADS -> {
                System.out.println("Heads");
            }
            case Coin.TAILS -> {    // Unnecessary qualification but allowed
                System.out.println("Tails");
            }
        }
    }

}
