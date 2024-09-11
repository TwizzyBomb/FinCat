package hesh.zone.fincat.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import hesh.zone.fincat.model.CatSet;
import hesh.zone.fincat.utilities.Utils;
import hesh.zone.fincat.config.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileSystem {
  @Value("${hesh.paths.test_file_small}")
  String smallTestFilePath;
  
  /**
   * loadLocalTransactionsFile - parses the input json file sent from broswer,
   * loads it into a pipe delimited format, then return the data
   *
   * @return List of String arrays, each representing a different Charge
   */
  public List<String[]> parseTransactionsFile(String filename) throws FileNotFoundException, IOException {
    List<String[]> data;
    
    // all this is wasted because the Gson library parses commas back in when sending the response
    // process csv to remove commas inside quotes
    Utils.replaceCommasOutsideQuotes(filename, Constants.PIPE_FILE_PATH);
    
    // first we go in and fetch the csv bank data
    data = loadTransactionCsvFile(Constants.PIPE_FILE_PATH);
    
    // cleanup
    deleteFile(Constants.PIPE_FILE_PATH);
    return data;
  }

  /**
   * loadLocalTransactionsFile - uses the constant set in Constants to load the transactions,
   * load it into a pipe delimited format, then return the data
   *
   * @return List of String arrays, each representing a different Charge
   */
  public List<String[]> loadLocalTransactionsFile(String filepath) throws FileNotFoundException, IOException {
    List<String[]> data;

    // process csv to remove commas inside quotes
    Utils.replaceLocalCommasOutsideQuotes(filepath, Constants.PIPE_FILE_PATH);

    // first we go in and fetch the csv bank data
    data = loadTransactionCsvFile(Constants.PIPE_FILE_PATH);

    return data;
  }
  /**
   * loadLocalTransactionsFile - uses the constant set in Constants to load the transactions,
   * load it into a pipe delimited format, then return the data
   *
   * @return List of String arrays, each representing a different Charge
   */
  public List<String[]> loadLocalTransactionsFile() throws FileNotFoundException, IOException {
    List<String[]> data;
    
    // process csv to remove commas inside quotes
    Utils.replaceLocalCommasOutsideQuotes(Constants.TEST_EMPTY_FILE_PATH, Constants.PIPE_FILE_PATH);
    
    // first we go in and fetch the csv bank data
    data = loadTransactionCsvFile(Constants.PIPE_FILE_PATH);
    
    return data;
  }
  
  /**
   * loadTransactionCsvFile
   *
   * @param filePath
   * @return List of String arrays, each representing a different Charge
   */
  private List<String[]> loadTransactionCsvFile(String filePath) throws FileNotFoundException, IOException {
    Path path = Path.of(filePath);
    Stream<String> lines;
    try {
      lines = Files.lines(path);
      return lines
              .map(line -> line.split("\\|"))
              .collect(Collectors.toList());
    } catch (FileNotFoundException fnfe) {
      System.out.println("Could not find pipe delimited transaction file ");
      throw fnfe;
    } catch (IOException ioe) {
      System.out.println("Encountered IO Exception looking for pipe delimited transaction file ");
      throw ioe;
    }
  }
  
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
  
  public CatSet getCatSetFile(String fileName) {
    CatSet catSet = readJson(fileName);
    return catSet != null ? catSet : new CatSet();
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
