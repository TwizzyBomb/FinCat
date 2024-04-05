package hesh.zone.fincat;

import hesh.zone.fincat.config.PathProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinCatApplication implements CommandLineRunner {
	@Autowired
	private PathProperties paths;

	public static void main(String[] args) {
		SpringApplication.run(FinCatApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("getChargeList: " + paths.getChargeList());
		System.out.println("getIncomeList: " + paths.getIncomeList());
		System.out.println("getTestFileSmall: " + paths.getTestFileSmall());
		System.out.println("getTestFile: " + paths.getTestFile());
		System.out.println("getPipeFile: " + paths.getPipeFile());

	}
}
