package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

public class TransactionResponse {
    private String transactionId;
    private String debitAccountName;
    private String creditAccountName;
    private double amount;
    private String status;
    private String debitRef;
    private String creditRef;
    private String date;

    public String getDebitAccountName() {
        return debitAccountName;
    }

    public void setDebitAccountName(String debitAccountName) {
        this.debitAccountName = debitAccountName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreditAccountName() {
        return creditAccountName;
    }

    public void setCreditAccountName(String creditAccountName) {
        this.creditAccountName = creditAccountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDebitRef() {
        return debitRef;
    }

    public void setDebitRef(String debitRef) {
        this.debitRef = debitRef;
    }

    public String getCreditRef() {
        return creditRef;
    }

    public void setCreditRef(String creditRef) {
        this.creditRef = creditRef;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
