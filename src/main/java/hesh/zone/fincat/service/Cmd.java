package hesh.zone.fincat.service;

import hesh.zone.fincat.config.Constants;
import hesh.zone.fincat.model.CatSet;
import hesh.zone.fincat.model.Charge;
import hesh.zone.fincat.service.FileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Cmd {
  private static CatSet chargeList = null;
  private static CatSet incomeList = null;
  
  public void terminalRun() throws FileNotFoundException, IOException {
    
    FileSystem fileSystem = new FileSystem();
    chargeList = fileSystem.getCatSetFile(Constants.CHARGE_LIST_PATH);
    incomeList = fileSystem.getCatSetFile(Constants.INCOME_LIST_PATH);
    
    // load old data into charge & income list objects and fetch new transactions into data List
    List<String[]> data = fileSystem.loadLocalTransactionsFile();
    
    try (Scanner scanner = new Scanner(System.in)) {
      // hi
      System.out.println("Hi");
      
      // no data means say goodbye!
      if (data.size() == 0) {
        System.out.println("No data found, thanks for playing!");
        if( null != chargeList ){
          chargeList.createBreakdown();
        }
        if( null != incomeList ){
          incomeList.createBreakdown();
        }
        // bye!
        exit();
      }
      
      // Process the new charge data as needed
      for (String[] row : data) {
        // get category names from user
        System.out.println("How would you like to categorize:" + row[4]);
        String categoryName = scanner.nextLine();
        String chargeName = categoryName;
        
        // parent category - either add to existing or create parent and child
        if (categoryName.contains(":")) {
          //split the parent from sub-category
          int pos = categoryName.indexOf(":");
          chargeName = categoryName.substring(pos + 1, categoryName.length());
        }
        // create charge
        Charge charge = new Charge(
                row[0].replace("\"", ""),
                "",
                row[4].replace("\"", ""),
                Math.abs(Double.parseDouble(row[1].replace("\"", ""))),
                chargeName);
        // log
        System.out.println("\nCreated cat" + charge.getDescription() + " to " + charge.getCategory() + " with amount " + charge.getAmount());
        
        // check if income or charge and add to appropriate obj
        if (row[1].replace("\"", "").startsWith("-")) {
          // add charge to category in list, create category if doesn't exist
          chargeList.add(categoryName, charge);
          System.out.println("added to charges");
        } else {
          // positive = income
          incomeList.add(categoryName, charge);
          System.out.println("added to income");
        }
        
        System.out.println(); // Move to the next line for the next row
      }
      
      // serialize current sorted category objects to json
      fileSystem.writeJson(Constants.CHARGE_LIST_PATH, chargeList);
      fileSystem.writeJson(Constants.INCOME_LIST_PATH, incomeList);
    }
    
    // print totals
    chargeList.createBreakdown();
    incomeList.createBreakdown();
    
    exit();
  }
  
  private void exit() {
    // free up resources and attempt to exit
    System.exit(0);
  }
}
