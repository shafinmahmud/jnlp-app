package net.shafin.common.util;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class Logger {

    public static void print(String log) {
        System.out.println("LOG : [" + DateTimeUtil.getLocalTimeStamp() + "] : " + log);
    }
}
