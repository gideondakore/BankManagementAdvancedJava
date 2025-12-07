package com.amalitech.bankaccount.utils;

public final class AppConstants {
    private AppConstants(){}

    public static final String NAME_REGEX = "^[A-ZÀ-ÿ][-,a-z.' ]+( [A-ZÀ-ÿ][-,a-z.' ]+)+$";
    public static final String AGE_REGEX = "^(?:120|1[01][0-9]|[1-9][0-9]?)$";
    public static final String PHONE_NUMBER_REGEX = "\\+\\d{1,3}-\\d{3}-\\d{4,12}";
    public static final String CONTACT_ADDRESS = "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$";
}
