package com.amalitech.bankaccount.utils;

import java.util.Scanner;
import java.util.regex.Pattern;

public record InputValidationHelper(String msg, String errMsg, String regex) {

    public String validatedStringInputValue() {
        Scanner scanner = new Scanner(System.in);
        String input;

        Pattern pattern = Pattern.compile(this.regex());
        while (true) {
            IO.print(this.msg);
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


    public int validatedIntInputValue() {
        Scanner scanner = new Scanner(System.in);
        int input;

        Pattern pattern = Pattern.compile(this.regex);

        while (true) {
            IO.print(this.msg);

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


    public double validatedDoubleInputValue() {
        Scanner scanner = new Scanner(System.in);
        double input;

        Pattern pattern = Pattern.compile(this.regex);
        while (true) {
            IO.print(this.msg);
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

    public char validatedCharInputValue() {
        Scanner scanner = new Scanner(System.in);
        char input;

        Pattern pattern = Pattern.compile(this.regex);
        while (true) {
            IO.print(this.msg);
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

    public int validatedIntInputValueWithRange(int lowerBound, int upperBound) {
        Scanner scanner = new Scanner(System.in);
        int input;

        while (true) {
            IO.print(this.msg);

            if(scanner.hasNextInt()){
                input = scanner.nextInt();

                if(input >= lowerBound && input <= upperBound){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(this.errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }

    public int validatedIntInputPositiveValue() {
        Scanner scanner = new Scanner(System.in);
        int input;

        while (true) {
            IO.print(this.msg);

            if(scanner.hasNextInt()){
                input = scanner.nextInt();

                if(input >= 0){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(this.errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }

    public double validatedDoubleInputPositiveValue() {
        Scanner scanner = new Scanner(System.in);
        double input;

        while (true) {
            IO.print(this.msg);

            if(scanner.hasNextDouble()){
                input = scanner.nextDouble();

                if(input >= 0){
                    break;
                }else{
                    IO.println(errMsg);
                    scanner.nextLine();
                }
            }else {
                IO.println(this.errMsg);
                scanner.nextLine();
            }

        }

        return input;
    }
}
