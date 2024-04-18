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
     * @return List of String arrays, each representing a different Charge
     */
    public List<String[]> loadTransactionFile() throws FileNotFoundException, IOException {
        List<String[]> data;

        // process csv to remove commas inside quotes
        Utils.replaceCommasOutsideQuotes(Constants.TEST_SMALL_PATH, Constants.PIPE_FILE_PATH);

        // first we go in and fetch the csv bank data
        data = loadTransactionCsvFile(Constants.PIPE_FILE_PATH);

        // also the previously saved json file (does nothing but print if file not there)
//        readJson(Constants.CHARGE_LIST_PATH);
//        readJson(Constants.INCOME_LIST_PATH);

        return data;
    }

    /**
     * loadTransactionCsvFile
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
        } catch(FileNotFoundException fnfe){
            System.out.println("Could not find pipe delimited transaction file ");
            throw fnfe;
        } catch(IOException ioe){
            System.out.println("Encountered IO Exception looking for pipe delimited transaction file ");
            throw ioe;
        }
    }

    public void writeJson(String fileName, CatSet catSet) throws IOException{
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

    public CatSet getCatSetFile(String fileName){
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
