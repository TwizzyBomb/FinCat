package hesh.zone.fincat.controller;

import com.google.gson.Gson;
import hesh.zone.fincat.service.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyController {
  
  @Autowired
  FileSystem fileSystem;
  
  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    String message = "Hello, World!";
    return ResponseEntity.ok(message);
  }
  
  @GetMapping("/error")
  public ResponseEntity<String> error() {
    String errorMessage = "An error occurred!";
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
  }
  
  @GetMapping("/custom")
  public ResponseEntity<String> customResponse() {
    String customMessage = "This is a custom response!";
    return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(customMessage);
  }
  
  // Your other endpoint mappings and methods here
//  @PostMapping("/upload")
//  public ResponseEntity<String> respondWithAllCharges(@RequestBody String fileName) {
//    System.out.println("Filename:" + fileName);
//    List<String[]> data = null;
//    try {
//      // fetch new transactions into data List
//      data = fileSystem.loadTransactionsFile(fileName);
//      Gson gson = new Gson();
//      return ResponseEntity.ok(gson.toJson(data));
//    } catch (Exception e) {
//      // Handle the exception and return an error response
//      String errorMessage = "Failed to load transactions file " + e.getMessage();
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//    }
  
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
}
