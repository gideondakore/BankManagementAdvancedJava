package com.amalitech.bankaccount.utils;

import java.util.Scanner;
import java.util.regex.Pattern;

public record InputValidationHelper() {
    private static final Scanner scanner = new Scanner(System.in);

    public static String validatedStringInputValue(String msg, String errMsg, String regex) {
        String input;

        Pattern pattern = Pattern.compile(regex);
        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (pattern.matcher(line).matches()) {
                input = line;
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;
    }


    public static int validatedIntInputValue(String msg, String errMsg, String regex) {
        int input;

        Pattern pattern = Pattern.compile(regex);

        while (true) {
            IO.print(msg);

            String line = scanner.nextLine();

            if (pattern.matcher(line).matches()) {
                input = Integer.parseInt(line);
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;
    }


    public static double validatedDoubleInputValue(String msg, String errMsg, String regex) {
        double input;

        Pattern pattern = Pattern.compile(regex);
        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (pattern.matcher(line).matches()) {
                input = Double.parseDouble(line);
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;
    }

    public static char validatedCharInputValue(String msg, String errMsg, String regex) {
        char input;

        Pattern pattern = Pattern.compile(regex);
        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (pattern.matcher(line).matches()) {
                input = line.charAt(0);
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;
    }

    public static int validatedIntInputValueWithRange(int lowerBound, int upperBound, String msg, String errMsg) {
        int input;

        while (true) {
            IO.print(msg);

            if(scanner.hasNextInt()){
                input = scanner.nextInt();

                if(input >= lowerBound && input <= upperBound){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }

    public static int validatedIntInputPositiveValue(String msg, String errMsg) {
        int input;

        while (true) {
            IO.print(msg);

            if(scanner.hasNextInt()){
                input = scanner.nextInt();

                if(input >= 0){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }

    public static double validatedDoubleInputPositiveValue(String msg, String errMsg) {
        double input;

        while (true) {
            IO.print(msg);

            if(scanner.hasNextDouble()){
                input = scanner.nextDouble();

                if(input >= 0){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }

    public static String validatedName(String msg, String errMsg){
        String input;

        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (ValidationUtils.validateName(line)) {
                input = line;
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;

    }

    public static int validatedAge(String msg, String errMsg){
        int input;

        while (true) {
            IO.print(msg);

            String line = scanner.nextLine();
            if (ValidationUtils.validateAge(line)) {
                input = Integer.parseInt(line);
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;

    }

    public static String validatedPhone(String msg, String errMsg){
        String input;

        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (ValidationUtils.validatePhone(line)) {
                input = line;
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;

    }

    public static String validatedAddress(String msg, String errMsg){
        String input;

        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (ValidationUtils.validateAddress(line)) {
                input = line;
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;

    }

    public static String validatedEmail(String msg, String errMsg){
        String input;

        while (true) {
            IO.print(msg);
            String line = scanner.nextLine();
            if (ValidationUtils.validateEmail(line)) {
                input = line;
                break;
            } else {
                IO.println(errMsg);
            }
        }

        return input;

    }


}
