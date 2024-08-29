package hesh.zone.fincat.model;

public class Pair<T> {
  private T income;
  private T charges;
  
  public Pair(T income, T charges) {
    this.income = income;
    this.charges = charges;
  }
  
  public T getIncome() {
    return income;
  }
  
  public void setIncome(T income) {
    this.income = income;
  }
  
  public T getCharges() {
    return charges;
  }
  
  public void setCharges(T charges) {
    this.charges = charges;
  }
}