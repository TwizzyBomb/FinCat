package hesh.zone.fincat.controller;

import com.google.gson.Gson;
import hesh.zone.fincat.config.Constants;
import hesh.zone.fincat.model.Breakdown;
import hesh.zone.fincat.model.CatSet;
import hesh.zone.fincat.model.Charge;
import hesh.zone.fincat.model.Pair;
import hesh.zone.fincat.service.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class MyController {

  @Value("hesh.paths.charge_list")
  private String chargeListPath;
  
  @Autowired
  FileSystem fileSystem;
  private static CatSet chargeList = null;
  private static CatSet incomeList = null;
  
  @GetMapping("/error")
  public ResponseEntity<String> error() {
    String errorMessage = "An error occurred!";
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
  }
  
  // add charge
  @PostMapping("/add")
  public ResponseEntity<String> addToObjects(@RequestHeader("Raw-Category-Name") String rawCatName, @RequestBody String json) {

    System.out.println("charge list path:" + chargeListPath);
    System.out.println("Json:" + json.toString());
    List<String[]> data = null;
    String sResponse = null;

    CompletableFuture<CatSet> chrgLstFuture = fileSystem.getCatSetFile(Constants.CHARGE_LIST_PATH);
    CompletableFuture<CatSet> incmLstFuture = fileSystem.getCatSetFile(Constants.INCOME_LIST_PATH);
    
    // convert the json into a charge and add to catset list then save back to file
    try {
      Gson gson = new Gson();
      Charge charge = gson.fromJson(json, Charge.class);

      chargeList = chrgLstFuture.get(); // blocking but I get some benefit of the above code executing while file ops occur
      incomeList = incmLstFuture.get();
      // check if income or charge and add to appropriate obj
      if (charge.getAmount() < 0) {
        // add charge to category in list, create category if doesn't exist
        chargeList.add(rawCatName, charge);
        sResponse = "Added to charges";
      } else {
        // positive = income
        incomeList.add(rawCatName, charge);
        sResponse = "Added to income";
      }
      if(null == sResponse) {
        sResponse = "An error occured adding charge to a list";
      }
      
      // this will have to kick off on another thread
      // serialize current sorted category objects to json
      fileSystem.writeJson(Constants.CHARGE_LIST_PATH, chargeList);
      fileSystem.writeJson(Constants.INCOME_LIST_PATH, incomeList);
      
      // print totals
      chargeList.createCmdBreakdown();
      incomeList.createCmdBreakdown();
      
      chargeList = null;
      incomeList = null;
      
      return ResponseEntity.ok(sResponse);
    } catch (Exception e) {
      // Handle the exception and return an error response
      String errorMessage = "Failed to add charge " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }
  
  @PostMapping("/upload")
  public ResponseEntity<String> respondWithAllCharges(@RequestBody String fileName) {
    System.out.println("Filename:" + fileName);
    List<String[]> data = null;
    try {
      // fetch new transactions into pipe delimited String array List
      data = fileSystem.parseTransactionsFile(fileName);
      Gson gson = new Gson();
      return ResponseEntity.ok(gson.toJson(data));
    } catch (Exception e) {
      // Handle the exception and return an error response
      String errorMessage = "Failed to load transactions file " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }
  
  @PostMapping("/breakdown")
  public ResponseEntity<String> respondWithBreakdown(){
    System.out.println("received request for breakdown");
    System.out.println("charge list path:" + chargeListPath);
    System.out.println("constants charge list file path:" + Constants.CHARGE_LIST_PATH);

    CompletableFuture<CatSet> chrgLstFuture = fileSystem.getCatSetFile(Constants.CHARGE_LIST_PATH);
    CompletableFuture<CatSet> incmLstFuture = fileSystem.getCatSetFile(Constants.INCOME_LIST_PATH);

    try{
    chargeList = chrgLstFuture.get();
    incomeList = incmLstFuture.get();
    
    Breakdown chargeBd = chargeList.createWebBreakdown();
    Breakdown incomeBd = incomeList.createWebBreakdown();
    
    Gson gson = new Gson();

      /* add content header to response */
      return ResponseEntity.ok(gson.toJson(new Pair<Breakdown>(incomeBd, chargeBd)));
    } catch (Exception e){
      // Handle the exception and return an error response
      String errorMessage = "Failed to create/send breakdown " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }
}
