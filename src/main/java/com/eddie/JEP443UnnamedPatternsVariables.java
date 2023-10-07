package com.eddie;

/**
 * Unnamed Patterns and Variables支持用_来替代没有使用的变量声明
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP443UnnamedPatternsVariables {

    public static void main(String[] args) {
        String r = "0";
        if (r instanceof String _) {
            System.out.println("aaaa");
        }

        int acc = 0;
        String[] orders = new String[]{"1121", "12333"};
        int LIMIT = 10;
        for (String _ : orders) {
            if (acc < LIMIT) {
                acc++;
            }
        }
    }
}
