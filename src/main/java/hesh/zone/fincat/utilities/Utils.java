package hesh.zone.fincat.utilities;

import java.io.*;

public class Utils {


    public static void replaceCommasOutsideQuotes(String inputFile, String outputFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            StringBuilder stringBuilder = new StringBuilder();

            int nextChar;
            boolean insideQuotes = false;

            // expects a blank new line at end of file
            while ((nextChar = reader.read()) != -1) {
                char currentChar = (char) nextChar;
                if (currentChar == '\"') {
                    insideQuotes = !insideQuotes;
                }
                if (currentChar == ',' && !insideQuotes) {
                    stringBuilder.append('|');
                } else {
                    stringBuilder.append(currentChar);
                }
                if (currentChar == '\n' && !insideQuotes) {
                    writer.write(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
            }
            // attempt to close these try with resources will work if not
            reader.close();
            writer.close();
        } catch(FileNotFoundException fnfe){
            System.out.println("Encountered File Not Found Exception replacing commas with pipes in csv ");
        } catch(IOException ioe){
            System.out.println("Encountered IO Exception replacing commas with pipes in csv ");
        } catch(NullPointerException npe){
        System.out.println("Encountered Null Pointer Exception replacing commas with pipes in csv. Is your path right?");
        }
    }

}
