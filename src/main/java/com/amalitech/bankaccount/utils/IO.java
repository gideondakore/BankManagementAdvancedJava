package com.amalitech.bankaccount.utils;

/**
 * IO utility class for console output operations
 * Provides static methods for printing to console
 * This class replaces the implicit IO class available in Java 23+
 */
public final class IO {
    
    private IO() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Prints a string to the console without a newline
     * @param message the message to print
     */
    public static void print(String message) {
        System.out.print(message);
    }
    
    /**
     * Prints a string to the console with a newline
     * @param message the message to print
     */
    public static void println(String message) {
        System.out.println(message);
    }
    
    /**
     * Prints an object to the console with a newline
     * @param obj the object to print
     */
    public static void println(Object obj) {
        System.out.println(obj);
    }
    
    /**
     * Prints an empty line to the console
     */
    public static void println() {
        System.out.println();
    }
    
    /**
     * Prints a formatted string to the console
     * @param format the format string
     * @param args the arguments
     */
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
