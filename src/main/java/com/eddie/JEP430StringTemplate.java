package com.eddie;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.util.List;

import static java.util.FormatProcessor.FMT;

/**
 * JEP 430: String Templates (Preview)
 * 在java21之前，字符串拼接或者字符串与表达式组合主要是用StringBuilder、String::format、java.text.MessageFormat，
 * 不过可读性都不是太好，java21引入了StringTemplate(java.lang.StringTemplate)来解决这个问题。
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP430StringTemplate {

    public static void main(String[] args) {
        var JSONP = StringTemplate.Processor.of(
                (StringTemplate st) -> JSON.parseObject(st.interpolate())
        );

        System.out.println("----------------- Str -----------------");
        String name = "Eddie";
        String info = STR."My name is \{name}";
        System.out.println(info);

        // 计算
        int x = 10;
        int y = 20;
        StringTemplate st = StringTemplate.RAW."\{x} + \{y} = \{x + y}";
        List<String> fragments = st.fragments();
        List<Object> values = st.values();
        System.out.println("----------------- Calculate -----------------");
        System.out.println(STR."fragments:\{fragments}");
        System.out.println(STR."values:\{values}");
        System.out.println(STR."str:\{st.interpolate()}");

        // 条件输出
        String filePath = "tmp.dat";
        File file = new File(filePath);
        System.out.println("----------------- Conditional Output -----------------");
        System.out.println(STR."The file \{filePath} \{file.exists() ? "does" : "does not"} exist");

        System.out.println("----------------- Fmt Output -----------------");
        System.out.println(getTableMessage());

        System.out.println("----------------- Custom Process Output -----------------");
        String phone   = "555-123-4567";
        String address = "1 Maple Drive, Anytown";
        JSONObject doc = JSONP."""
            {
                "name":    "\{name}",
                "phone":   "\{phone}",
                "address": "\{address}"
            }
            """;
        System.out.println(doc);
    }


    private static String getTableMessage() {
        record Rectangle(String name, double width, double height) {
            double area() {
                return width * height;
            }
        }
        Rectangle[] zone = new Rectangle[]{
                new Rectangle("Alfa", 17.8, 31.4),
                new Rectangle("Bravo", 9.6, 12.4),
                new Rectangle("Charlie", 7.1, 11.23),
        };
        return FMT."""
            Description     Width    Height     Area
            %-12s\{zone[0].name}  %7.2f\{zone[0].width}  %7.2f\{zone[0].height}     %7.2f\{zone[0].area()}
            %-12s\{zone[1].name}  %7.2f\{zone[1].width}  %7.2f\{zone[1].height}     %7.2f\{zone[1].area()}
            %-12s\{zone[2].name}  %7.2f\{zone[2].width}  %7.2f\{zone[2].height}     %7.2f\{zone[2].area()}
            \{" ".repeat(26)} Total \{" ".repeat(2)}%7.2f\{zone[0].area() + zone[1].area() + zone[2].area()}
            """;
    }
}
