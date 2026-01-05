package com.amalitech.bankaccount.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Utility class for regex-based validation
 * Provides centralized validation logic for account numbers, emails, and phone numbers
 * Uses Predicate lambdas for dynamic validation rules
 */
public class ValidationUtils {
    
    // Regex patterns as constants
    public static final String ACCOUNT_NUMBER_REGEX = "^ACC00\\d+$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_REGEX = "^\\+\\d{1,3}-\\d{3}-\\d{4,12}$";
    public static final String NAME_REGEX = "^[A-ZÀ-ÿ][-,a-z.' ]+( [A-ZÀ-ÿ][-,a-z.' ]+)+$";
    public static final String AGE_REGEX = "^(?:120|1[01][0-9]|[1-9][0-9]?)$";
    public static final String ADDRESS_REGEX = "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$";
    
    // Pre-compiled patterns for better performance
    private static final Pattern accountPattern = Pattern.compile(ACCOUNT_NUMBER_REGEX);
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern phonePattern = Pattern.compile(PHONE_REGEX);
    private static final Pattern namePattern = Pattern.compile(NAME_REGEX);
    private static final Pattern agePattern = Pattern.compile(AGE_REGEX);
    private static final Pattern addressPattern = Pattern.compile(ADDRESS_REGEX);
    
    // Predicate-based validators using lambdas
    public static final Predicate<String> isValidAccountNumber = 
        input -> input != null && accountPattern.matcher(input).matches();
    
    public static final Predicate<String> isValidEmail = 
        input -> input != null && emailPattern.matcher(input).matches();
    
    public static final Predicate<String> isValidPhone = 
        input -> input != null && phonePattern.matcher(input).matches();
    
    public static final Predicate<String> isValidName = 
        input -> input != null && namePattern.matcher(input).matches();
    
    public static final Predicate<String> isValidAge = 
        input -> input != null && agePattern.matcher(input).matches();
    
    public static final Predicate<String> isValidAddress = 
        input -> input != null && addressPattern.matcher(input).matches();
    
    public static final Predicate<Double> isPositiveAmount = 
        amount -> amount != null && amount > 0;
    
    public static final Predicate<Double> isNonNegativeAmount = 
        amount -> amount != null && amount >= 0;
    
    /**
     * Validates an account number
     * Format: ACC followed by at least two zeros and then digits (e.g., ACC001, ACC0025)
     * 
     * @param accountNumber the account number to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateAccountNumber(String accountNumber) {
        return isValidAccountNumber.test(accountNumber);
    }
    
    /**
     * Validates an email address
     * Format: standard email format (e.g., user@domain.com)
     * 
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateEmail(String email) {
        return isValidEmail.test(email);
    }
    
    /**
     * Validates a phone number
     * Format: +country-area-number (e.g., +1-555-7890)
     * 
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean validatePhone(String phone) {
        return isValidPhone.test(phone);
    }
    
    /**
     * Validates a customer name
     * Format: First and last name with valid characters
     * 
     * @param name the name to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateName(String name) {
        return isValidName.test(name);
    }
    
    /**
     * Validates an age value
     * Range: 1-120
     * 
     * @param ageStr the age string to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateAge(String ageStr) {
        return isValidAge.test(ageStr);
    }
    
    /**
     * Validates an address
     * Format: 5-100 alphanumeric characters with common punctuation
     * 
     * @param address the address to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateAddress(String address) {
        return isValidAddress.test(address);
    }
    
    /**
     * Validates that an amount is positive
     * 
     * @param amount the amount to validate
     * @return true if positive, false otherwise
     */
    public static boolean validatePositiveAmount(double amount) {
        return isPositiveAmount.test(amount);
    }
    
    /**
     * Creates a custom validator using a regex pattern
     * Uses functional programming with Predicate
     * 
     * @param regex the regex pattern to use
     * @return a Predicate for validation
     */
    public static Predicate<String> createValidator(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return input -> input != null && pattern.matcher(input).matches();
    }
    
    /**
     * Combines multiple validators using AND logic
     * 
     * @param validators the validators to combine
     * @return a combined Predicate
     */
    @SafeVarargs
    public static Predicate<String> combineValidators(Predicate<String>... validators) {
        Predicate<String> combined = s -> true;
        for (Predicate<String> validator : validators) {
            combined = combined.and(validator);
        }
        return combined;
    }
    
    /**
     * Gets an error message for invalid account number
     */
    public static String getAccountNumberError() {
        return """
            Please provide a valid account number!
            
            Example format:
            ACC001
            ACC002
            ACC0010
            ACC00120
            """;
    }
    
    /**
     * Gets an error message for invalid email
     */
    public static String getEmailError() {
        return """
            Please provide a valid email address!
            
            Example format:
            user@example.com
            john.doe@company.org
            """;
    }
    
    /**
     * Gets an error message for invalid phone
     */
    public static String getPhoneError() {
        return """
            Please provide a valid phone number!
            
            Example format:
            +1-555-7890
            +44-207-9463821
            """;
    }
}
