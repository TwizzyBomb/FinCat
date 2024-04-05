package hesh.zone.fincat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import hesh.zone.fincat.model.*;
import hesh.zone.fincat.service.*;
import hesh.zone.fincat.config.Constants;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class FinCatApplication implements CommandLineRunner {
	static Scanner scanner;
	public static void main(String[] args) {
		SpringApplication.run(FinCatApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		CatSet chargeList = new CatSet();
		CatSet incomeList = new CatSet();
		FileSystem fileSystem = new FileSystem();

		// load old data into charge & income list objects and fetch new transactions into data List
		List<String[]> data = fileSystem.loadFiles(chargeList, incomeList);

		try{
			// hi
			scanner = new Scanner(System.in);
			System.out.println("Hi");

			// no data means say goodbye!
			if(data.size()==0){
				System.out.println("No data found, thanks for playing!");
				exit();
			}

			// Process the new charge data as needed
			for (String[] row : data) {
				// get category names from user
				System.out.println("How would you like to categorize:" + row[4]);
				String categoryName = scanner.nextLine();
				Charge charge = new Charge(
						row[0].replace("\"", ""),
						"",
						row[4].replace("\"", ""),
						Math.abs(Double.parseDouble(row[1].replace("\"", ""))),
						categoryName);
				System.out.println("\nAdded " + charge.getDescription() +
						" to " + charge.getCategory() +
						" with amount " + charge.getAmount());

				// check if income or charge and add to appropriate obj
				if(row[1].replace("\"", "").startsWith("-")){
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

		}finally{
			try{ scanner.close(); }catch(Exception e){}
		}

		// print totals
		chargeList.printTotals();
		incomeList.printTotals();
	}

	private void exit(){
		// free up resources and attempt to exit
		System.exit(0);
	}
}
