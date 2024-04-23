package hesh.zone.fincat.model;

import java.util.HashMap;
import java.util.List;

public class Category {
  private String name;
  private double total;
  private HashMap<String, Charge> charges;
  private String parentCatName;
  private HashMap<String, Category> childCategories;
  
  /**
   * Category - regular constructor
   * @param name - name of the category
   */
  public Category(String name) {
    this.name = name;
    total = 0.0;
    charges = new HashMap<String, Charge>();
    childCategories = new HashMap<>();
  }
  
  /**
   * Category - child category constructor
   * @param name - name of this child category
   * @param parentName - name of the parent category associated with this category
   */
  public Category(String name, String parentName) {
    this.name = name;
    total = 0.0;
    charges = new HashMap<String, Charge>();
    this.parentCatName = parentName;
    childCategories = new HashMap<>();
  }
  
  /**
   * add - Takes a charge, concats it's category, date and amount into a key
   * puts it in the categories charge map
   *
   * @param charge
   */
  public void add(Charge charge) {
    //addToTotal(charge.getTotal());
    StringBuilder sb = new StringBuilder();
    sb.append(charge.getCategory());
    sb.append(charge.getTransactionDate().replace("/", ""));
    sb.append(charge.getAmount());
    String chargeKey = sb.toString();
    charges.put(chargeKey, charge);
  }
  
  public void addCategory(Category category) {
    this.childCategories.put(category.getName(), category);
  }
  
  public void addToTotal(double price) {
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
  
  public String getParentCatName() {
    return parentCatName;
  }
  
  public void setParentCatName(String parentName) {
    this.parentCatName = parentName;
  }
  
  public HashMap<String, Category> getChildCategories() {
    return childCategories;
  }
  
  public void setChildCategories(HashMap<String, Category> childCategories) {
    this.childCategories = childCategories;
  }
}
