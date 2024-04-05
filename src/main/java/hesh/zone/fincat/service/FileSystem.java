package hesh.zone.fincat.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import hesh.zone.fincat.model.CatSet;
import hesh.zone.fincat.utilities.Utils;
import hesh.zone.fincat.config.Constants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileSystem {
    @Value("${hesh.paths.test_file_small}")
    String smallTestFilePath;

    /**
     * @param chargeList - charge list obj
     * @param incomeList - income list obj
     * @return List of String arrays, each representing a different Charge
     */
    public List<String[]> loadFiles(CatSet chargeList, CatSet incomeList){
        List<String[]> data;

        // process csv to remove commas inside quotes
        Utils.replaceCommasOutsideQuotes(Constants.TEST_SMALL_PATH, Constants.PIPE_FILE_PATH);

        // first we go in and fetch the csv bank data
        data = loadTransactionCsvFile(Constants.PIPE_FILE_PATH);

        // also the previously saved json file (does nothing but print if file not there)
        /* chargeList = Utils.readJson("artifacts/out/chargelist.json");
        incomeList = Utils.readJson("artifacts/out/incomeList.json");
        */
        chargeList = readJson(Constants.CHARGE_LIST_PATH);
        incomeList = readJson(Constants.INCOME_LIST_PATH);

        return data;
    }

    /**
     * loadTransactionCsvFile
     * @param filePath
     * @return List of String arrays, each representing a different Charge
     */
    private List<String[]> loadTransactionCsvFile(String filePath) {
        Path path = Path.of(filePath);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            return lines
                    .map(line -> line.split("\\|"))
                    .collect(Collectors.toList());
        } catch(FileNotFoundException fnfe){
            System.out.println("Could not find pipe delimited transaction file ");
        } catch(IOException ioe){
            System.out.println("Encountered IO Exception looking for pipe delimited transaction file ");
        }
        return new ArrayList<String[]>();
    }

    public void writeJson(String fileName, CatSet catSet){
        // Create a Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(fileName)) {
            // Convert the data object to JSON string and write to the file
            gson.toJson(catSet, writer);
            System.out.println("JSON data has been written to the file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing JSON to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CatSet readJson(String fileName){
        CatSet catSet = new CatSet();
        try (FileReader reader = new FileReader(fileName)) {
            // Create a Gson instance
            Gson gson = new Gson();

            // Parse JSON data from the file into a Java object
            catSet = gson.fromJson(reader, CatSet.class);
            System.out.println("Successfully loaded previous json file:" + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while reading JSON from the file: " + e.getMessage());
//            e.printStackTrace();
        }
        return catSet;
    }
}
