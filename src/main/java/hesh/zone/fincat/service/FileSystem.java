package hesh.zone.fincat.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import hesh.zone.fincat.model.CatSet;
import hesh.zone.fincat.utilities.Utils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystem {
  @Value("${hesh.paths.test_file_small}")
  private String smallTestFilePath;

  @Value("${hesh.paths.pipe_file}")
  private String pipeFilePath;

  @Value("${hesh.paths.empty_file}")
  private String emptyFilePath;
  
  /**
   * loadLocalTransactionsFile - parses the input json file sent from browser,
   * loads it into a pipe delimited format, then return the data
   *
   * @return List of String arrays, each representing a different Charge
   */
  public List<String[]> parseTransactionsFile(String filename) throws FileNotFoundException, IOException, ExecutionException, InterruptedException {
    List<String[]> data;
    
    // all this is wasted because the Gson library parses commas back in when sending the response
    // process csv to remove commas inside quotes
    Utils.replaceCommasOutsideQuotes(filename, pipeFilePath);
    
    // first we go in and fetch the csv bank data
    CompletableFuture<List<String[]>> csvLoadFuture = loadTransactionCsvFile(pipeFilePath);
    data = csvLoadFuture.get();
    
    // cleanup
    deleteFile(pipeFilePath);
    return data;
  }
  
  /**
   * loadLocalTransactionsFile - uses the constant set in Constants to load the transactions,
   * load it into a pipe delimited format, then return the data
   *
   * @return List of String arrays, each representing a different Charge
   */
  public List<String[]> loadLocalTransactionsFile() throws FileNotFoundException, IOException, ExecutionException, InterruptedException {
    List<String[]> data;
    
    // process csv to remove commas inside quotes
    Utils.replaceLocalCommasOutsideQuotes(smallTestFilePath, pipeFilePath);
    
    // first we go in and fetch the csv bank data
    CompletableFuture<List<String[]>> csvLoadFuture = loadTransactionCsvFile(pipeFilePath);
    data = csvLoadFuture.get();
    
    return data;
  }
  
  /**
   * loadTransactionCsvFile
   *
   * @param filePath
   * @return List of String arrays, each representing a different Charge
   */
  @Async
  private CompletableFuture<List<String[]>> loadTransactionCsvFile(String filePath) throws FileNotFoundException, IOException {
    Path path = Path.of(filePath);

    try(Stream<String> lines = Files.lines(path)){
      return CompletableFuture.completedFuture(
              lines
              .map(line -> line.split("\\|"))
              .collect(Collectors.toList())
      );
    } catch (FileNotFoundException fnfe) {
      System.out.println("Could not find pipe delimited transaction file ");
      throw fnfe;
    } catch (IOException ioe) {
      System.out.println("Encountered IO Exception looking for pipe delimited transaction file ");
      throw ioe;
    }
  }

  @Async
  public static boolean deleteFile(String filePath) {
    // Create a File object representing the file to be deleted
    File file = new File(filePath);
    
    // Check if the file exists
    if (file.exists()) {
      // Attempt to delete the file
      if (file.delete()) {
        System.out.println("File deleted successfully: " + filePath);
        return true;
      } else {
        System.err.println("Failed to delete the file: " + filePath);
        return false;
      }
    } else {
      System.err.println("File does not exist: " + filePath);
      return false;
    }
  }
  
  @Async
  public void writeJson(String fileName, CatSet catSet) throws IOException {
    // Create a Gson instance
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    try (FileWriter writer = new FileWriter(fileName)) {
      // Convert the data object to JSON string and write to the file
      gson.toJson(catSet, writer);
      System.out.println("JSON data has been written to the file: " + fileName);
    } catch (IOException e) {
      System.out.println("An error occurred while writing JSON to the file: " + e.getMessage());
      throw e;
    }
  }

  @Async
  public CompletableFuture<CatSet> getCatSetFile(String fileName) {
    CatSet catSet = readJson(fileName);
    return CompletableFuture.completedFuture( catSet != null ? catSet : new CatSet() );
  }
  
  public CatSet readJson(String fileName) {
    CatSet catSet = null;
    try (FileReader reader = new FileReader(fileName)) {
      // Create a Gson instance
      Gson gson = new Gson();
      
      // Parse JSON data from the file into a Java object
      catSet = gson.fromJson(reader, CatSet.class);
      System.out.println("Successfully loaded previous json file:" + fileName);
    } catch (IOException e) {
      System.out.println("IOException while reading " + fileName + " exception message: " + e.getMessage());
//            throw e;
    }
    return catSet;
  }
}
