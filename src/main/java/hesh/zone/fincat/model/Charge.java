package hesh.zone.fincat.model;

public class Charge {

    private String transactionDate;
    private String postingDate;
    private String description;
    private double amount;
    private String category;
    private double total;

    public Charge(String TransactionDate, String PostingDate, String Description, Double Amount, String Category){
        this.transactionDate = TransactionDate;
        this.postingDate = PostingDate;
        this.description = Description;
        this.amount = Amount;
        this.category = Category;
        this.total = Amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
