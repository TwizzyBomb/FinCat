package hesh.zone.fincat.model;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Categories - This is a list which will keep a running total for finding
 * the proportions the category takes up of the total
 */
public class CatSet {
//    private static CatSet instance;
    private HashMap<String, Category> categoriesMap;
    private double runningTotal;
    private String listName;
    public CatSet(){
        categoriesMap = new HashMap<String, Category>();
        runningTotal = 0.0;
    }

    // not what we want because we need an income and charge list to exist simultaniously
//    public static CatSet getInstance() {
//        if (instance == null) {
//            instance = new CatSet();
//        }
//        return instance;
//    }

    public void add(String name, Charge charge){
        // add to running total:
        runningTotal += charge.getAmount();

        // check whether category existed or not
        if(!categoriesMap.containsKey(name)){
            // add new key and category
            Category cat = new Category(name);
            cat.addToTotal(charge.getAmount());
            categoriesMap.put(name, cat);

        } else {
            // add the charge to the existing category object
            categoriesMap.get(name).add(charge);
        }
    }

    public void printTotals() {
        int n = categoriesMap.size();
        DecimalFormat df = new DecimalFormat("0.00");

        if (n > 0){
            // loop through, display totals and percentages
            System.out.println("Breakdown:\n");
            for(String catName: categoriesMap.keySet() ) {
                System.out.println(df.format(categoriesMap.get(catName).getTotal())
                        + " towards "
                        + catName
                        + "\t"
                        + df.format((categoriesMap.get(catName).getTotal() / runningTotal) * 100)
                        + "% of total spent this month");
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