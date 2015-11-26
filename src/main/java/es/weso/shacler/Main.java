package es.weso.shacler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;


import org.joda.time.format.PeriodFormat;

import com.hp.hpl.jena.rdf.model.Model;


public class Main {
	static boolean verbose = false;

	public static void main(String... args) {
		
		System.out.println("Shacl...");
		Logger log = Logger.getLogger(Main.class.getName());
		try {
			Instant start = Instant.now();
			ArgsParser options = new ArgsParser(args);
			
			verbose = options.verbose;
			
		switch (options.processor) {
			case "shacl":
				ShaclValidator validator = new ShaclValidator();
				log.info("Data: " + options.data + ". Schema: " + options.schema);
				Model result = validator.validate(options.data,options.schema);
				System.out.println("End of validation");
				result.write(System.out,"TURTLE");
				break;
			}
			
			
			Instant end = Instant.now();
			if (options.printTime) printTime(start,end);
			
		} catch (Exception e) {
			System.out.println("Exception raised: " + e.getMessage());
			e.printStackTrace();
		} 
		log.info("End of program");
	}

	static void wellcome() {
		verbose("Wellcome to SHCALer");
	}
	
	static void verbose(String msg) {
	  if (verbose) {
		System.out.println(msg);
	  }
	}
	
	static void printTime(Instant start,Instant end) {
		Period timeElapsed = new Period(start,end);
     	System.out.println("Time elapsed " + 
     			PeriodFormat.getDefault().print(timeElapsed));
	}

}
