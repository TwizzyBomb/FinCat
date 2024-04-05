package hesh.zone.fincat.model;

import java.util.HashMap;

public class Category {
    String name;
    double total;
    HashMap<String, Charge> charges;
    public Category(String name) {
        total = 0.0;
        charges = new HashMap<String, Charge>();
    }

    public void add(Charge charge){
        addToTotal( charge.getTotal() );
        StringBuilder sb = new StringBuilder();
        sb.append(charge.getCategory());
        sb.append(charge.getTransactionDate().replace("/", ""));
        sb.append(charge.getAmount());
        String chargeKey = sb.toString();
        charges.put(chargeKey, charge);
    }

    public void addToTotal(double price){
        this.total += price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public HashMap<String, Charge> getCharges() {
        return charges;
    }

    public void setCharges(HashMap<String, Charge> charges) {
        this.charges = charges;
    }
}
