package com.amalitech.bankaccount.enums;

public enum TransferToOrFromType {
    TO("To"),
    FROM("From");

    private final String description;

    TransferToOrFromType(String description) {this.description = description;}

    public String getDescription(){
        return this.description;
    }
}
