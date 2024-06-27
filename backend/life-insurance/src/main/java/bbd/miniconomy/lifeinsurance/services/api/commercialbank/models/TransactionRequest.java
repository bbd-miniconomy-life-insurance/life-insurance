package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

public class TransactionRequest {
    private String debitAccountName;
    private double transactionAmount;
    private String creditRef;
    private String debitRef;

    public String getDebitAccountName() {
        return debitAccountName;
    }

    public void setDebitAccountName(String debitAccountName) {
        this.debitAccountName = debitAccountName;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCreditRef() {
        return creditRef;
    }

    public void setCreditRef(String creditRef) {
        this.creditRef = creditRef;
    }

    public String getDebitRef() {
        return debitRef;
    }

    public void setDebitRef(String debitRef) {
        this.debitRef = debitRef;
    }
}
