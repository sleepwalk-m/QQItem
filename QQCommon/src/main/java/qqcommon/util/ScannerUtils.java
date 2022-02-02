package qqcommon.util;

import java.util.Scanner;

/**
 * @author: Mask.m
 * @create: 2022/01/30 18:43
 * @description: 键入工具类
 */
public class ScannerUtils {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean flag = true;

    /**
     * 读取长度
     *
     * @param len
     * @return
     */
    public static String readString(int len) {
        String next = "";
        next = scanner.next();
        if (next.length() != 0 && next.length() <= len) {
            flag = false;
        } else {
            System.out.println("输入长度不符合要求，请重新输入");
        }
        return next;
    }
}
