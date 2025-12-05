package com.amalitech.bankaccount.transaction;


import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.enums.TransferToOrFromType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionManager {
    private final ArrayList<Transaction> transactions = new ArrayList<>(200);
    private int transactionCount;
    static int transactionCounter;


    public static void updateCounter(){
        transactionCounter++;
    }

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
        TransactionManager.updateCounter();
    }

    public List<Transaction> getTransactions(){
        return  this.transactions;
    }

    public void previewTransactionConfirmation(Account account, TransactionType transactionType, double transactionAmount, TransactionManager transactionManager, String accNumber){
        List<Transaction> newTransactions = getAllTransactions(accNumber, transactionManager.getTransactions());

        String txnID = String.valueOf(newTransactions.size() + 1);
        double newBalance = transactionType == TransactionType.DEPOSIT ? account.getAccountBalance() + transactionAmount : account.getAccountBalance() - transactionAmount;

        IO.println("""
                
                TRANSACTION CONFIRMATION
                ----------------------------------------------------------------------------------------
                Transaction ID: TXN00%s
                Account: %s
                Type: %s
                Amount: $%,.2f
                Previous Balance: $%,.2f
                New Balance: $%,.2f
                Date/Time: %s
                ----------------------------------------------------------------------------------------
                """.formatted(txnID, account.getAccountNumber(), transactionType.getDescription(), transactionAmount, account.getAccountBalance(), newBalance, ZonedDateTime.now().toString()));
    }

    public void viewTransactionsByAccount(String accountNumber){

        List<Transaction> newTransactions = getAllTransactions(accountNumber, this.transactions);

        if(newTransactions.isEmpty()){
            IO.println("""
                    -------------------------------------------
                    No transaction recorded for this account.
                    -------------------------------------------
                    """);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        String heading = """
                TRANSACTION HISTORY
                ----------------------------------------------------------------------------------------------------------------------------------------------
                %-8s              | %-20s                | %-6s                | %-10s                | %-15s
                ----------------------------------------------------------------------------------------------------------------------------------------------
                """.formatted("TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE");


        stringBuilder.append(heading);


        ArrayList<Transaction> sortedTransactions = new ArrayList<>(newTransactions);
        sortedTransactions.sort(Comparator.comparing(
                Transaction::parseTimeStamp).reversed()
        );

        for(Transaction trn: sortedTransactions){
            String tempStr = getFormattingString(trn);

            stringBuilder.append(tempStr).append("\n");
        }
        stringBuilder.append("----------------------------------------------------------------------------------------------------------------------------------------------");

        IO.println(stringBuilder.toString());
    }

    private static String getFormattingString(Transaction trn) {
        String negSigned = """
               %-8s              | %-20s              | %-6s            | -$%,-10.2f              | $%,-15.2f
               """;

        String posSigned = """
               %-8s              | %-20s              | %-6s               | +$%,-10.2f              | $%,-15.2f
               """;

        String tempStr;

        if(trn.getType().equals(TransactionType.DEPOSIT.getDescription())){
            tempStr = posSigned.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());
        }else if(trn.getType().equals(TransactionType.WITHDRAWAL.getDescription())){
            tempStr = negSigned.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());
        }else if(trn.getType().equals(TransactionType.TRANSFER.getDescription())){
            TransferToOrFromType sign = trn.getTransferToOrFrom() == TransferToOrFromType.FROM ? TransferToOrFromType.FROM : TransferToOrFromType.TO;

            if(TransferToOrFromType.FROM == sign){
                tempStr = negSigned.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());
            }else{
                tempStr = posSigned.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());
            }

        }else{
            tempStr = posSigned.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());
        }
        return tempStr;
    }

    public double calculateTotalDeposits(String accountNumber) {

        if(this.transactions.isEmpty()){
            return 0.0;
        }

        double tempBal = 0.0;
        transactionCount = 0;

        for(Transaction trn: transactions){
            if(trn.getType().equals(TransactionType.DEPOSIT.getDescription()) && trn.getAccountNumber().equals(accountNumber)){
                tempBal += trn.getBalanceAfter();
                ++transactionCount;
            }
        }

        return tempBal;

    }

    public double calculateTotalWithdrawals(String accountNumber) {


        if(this.transactions.isEmpty()){
            return 0.0;
        }

        transactionCount = 0;

        double tempBal = 0.0;

        for(Transaction trn: transactions){
            if(trn.getType().equals(TransactionType.WITHDRAWAL.getDescription()) && trn.getAccountNumber().equals(accountNumber)){
                tempBal += trn.getBalanceAfter();
                ++transactionCount;
            }
        }

        return tempBal;

    }

    public int getTransactionCount(){
        return transactionCount;
    }

    public static List<Transaction> getAllTransactions(String accNumber, List<Transaction> transactions){

        ArrayList<Transaction> transactions1 = new ArrayList<>();

        for(Transaction transaction: transactions){
            if(transaction.getAccountNumber().equals(accNumber)){
                transactions1.add(transaction);
            }
        }

        if(transactions1.isEmpty()) return Collections.emptyList();

        return transactions1;

    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("[ ");
        for(int i = 0; i < this.transactions.size(); i++){

            if (i < (this.transactions.size() - 1)) {
                str.append(this.transactions.get(i).getTransactionId()).append(", ");
            } else {
                str.append(this.transactions.get(i).getTransactionId());
            }
        }
        str.append(" ]");

        return str.toString();
    }

}
