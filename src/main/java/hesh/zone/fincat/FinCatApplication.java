package hesh.zone.fincat;

import hesh.zone.fincat.model.CatSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import hesh.zone.fincat.service.Cmd;

@SpringBootApplication
public class FinCatApplication { // implements CommandLineRunner
	public static void main(String[] args) {
		SpringApplication.run(FinCatApplication.class, args);
	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		try {
//			new Cmd().terminalRun();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//	}
}
