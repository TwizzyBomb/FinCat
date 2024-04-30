package hesh.zone.fincat.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Categories - This is a list which will keep a running total for finding
 * the proportions the category takes up of the total
 */
public class CatSet {
  //    private static CatSet instance;
  private HashMap<String, Category> categoriesMap;
  private double runningTotal;
  
  public CatSet() {
    categoriesMap = new HashMap<String, Category>();
    runningTotal = 0.0;
  }
  
  // not what we want because we need an income and charge list instance to exist simultaniously
//    public static CatSet getInstance() {
//        if (instance == null) {
//            instance = new CatSet();
//        }
//        return instance;
//    }
  
  public void add(String rawCatName, Charge charge) {
    String parentCatName = null;
    String childCatName = null;
    if (rawCatName.contains(":")) {
      //split the parent from sub-category
      int pos = rawCatName.indexOf(":");
      parentCatName = rawCatName.substring(0, pos);
      childCatName = rawCatName.substring(pos + 1, rawCatName.length());
      System.out.println("Child Name:" + childCatName + " Parent name:" + parentCatName);
    } else {
      parentCatName = rawCatName;
    }
    
    // does parent exist?
    if (!categoriesMap.containsKey(parentCatName)) {
      
      // parent doesn't exist, create one
      Category parentCat = new Category(parentCatName);
      parentCat.addToTotal(charge.getAmount());
      
      // is there a child to add as well?
      if (null != childCatName) {
        // add charges to child category, and add to parent
        Category childCat = new Category(childCatName);
        childCat.add(charge);
        childCat.addToTotal(charge.getAmount());
        childCat.setParentCatName(parentCatName);
        parentCat.getChildCategories().put(childCatName, childCat);
      }
      
      // add parent
      categoriesMap.put(parentCatName, parentCat);
      
    } else {
      // parent exits, does child?
      if (null != childCatName) {
        
        // child in parent cat set?
        if (null == categoriesMap.get(parentCatName).getChildCategories().get(childCatName)) {
          // child doesn't exist
          Category childCat = new Category(childCatName);
          childCat.addToTotal(charge.getAmount());
          childCat.setParentCatName(parentCatName);
          childCat.add(charge);
          categoriesMap.get(parentCatName).getChildCategories().put(childCatName, childCat);
          categoriesMap.get(parentCatName).addToTotal(charge.getAmount());
        } else {
          // both already exist - add the charge to the child cat
          categoriesMap.get(parentCatName).getChildCategories().get(childCatName).add(charge);
          categoriesMap.get(parentCatName).addToTotal(charge.getAmount());
        }
      } else {
        // just add the charge to the existing category object
        categoriesMap.get(parentCatName).add(charge);
        categoriesMap.get(parentCatName).addToTotal(charge.getAmount());
      }
    }
    
    // add to running total:
    runningTotal += charge.getAmount();

//    // check whether solo category existed or not
//    if (!categoriesMap.containsKey(rawCatName)) {
//      // add new key and category
//      Category cat = new Category(rawCatName);
//      cat.addToTotal(charge.getAmount());
//      categoriesMap.put(parentCatName, cat);
//
//    } else {
//      // add the charge to the existing category object
//      categoriesMap.get(parentCatName).add(charge);
//    }
  }

//  public void add(String name, Charge charge) {
//    // add to running total:
//    runningTotal += charge.getAmount();
//
//    // check whether category existed or not
//    if (!categoriesMap.containsKey(name)) {
//      // add new key and category
//      Category cat = new Category(name);
//      cat.addToTotal(charge.getAmount());
//      categoriesMap.put(name, cat);
//
//    } else {
//      // add the charge to the existing category object
//      categoriesMap.get(name).add(charge);
//    }
//  }
  
  public void createBreakdown() {
    int n = categoriesMap.size();
    DecimalFormat df = new DecimalFormat("0.00");
    
    if (n > 0) {
      // loop through, display totals and percentages
      System.out.println("Breakdown:\n");
      for (Category parentCat : categoriesMap.values() ) {
        double totalPercent = 0.0;
        for( Category childCat : parentCat.getChildCategories().values() ) {
          totalPercent += childCat.getTotal();
          System.out.println(df.format(childCat.getTotal())
                  + " towards " + childCat.getName() + "\t\t"
                  + df.format((childCat.getTotal() / runningTotal) * 100)
                  + "% of total spent this month");
        }
        System.out.println("total percent for " + parentCat.getName() + ": "
                + df.format((totalPercent / runningTotal) * 100) + "%");
        
      }
    }
  }
  
  public HashMap<String, Category> getCategoriesMap() {
    return categoriesMap;
  }
  
  public void setCategoriesMap(HashMap<String, Category> categoriesMap) {
    this.categoriesMap = categoriesMap;
  }
}