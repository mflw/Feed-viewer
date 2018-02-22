package com.helloworld;
//package src.main.java.com.helloworld;
import java.util.Scanner;
import java.lang.String;
import java.io.*;
import org.xml.sax.*;

public class Helloworld {
	//public Parser parser = null;

	public static void main(String[] args) {
		Parser parser = null;
		Scanner scanner = new Scanner(System.in);
		String input;
		
		System.out.println("Feed viewer v0.7");
		

		while (true) {
			System.out.println("Enter path to the feed or drag&drop:");
			input = scanner.next();
			if (input.equals("exit")) System.exit(0);
			try {
				System.out.println("--- Verifing XML format..");
					
				parser = new Parser(input);
				System.out.println("Created: "+parser.getDate());
				System.out.println("--- Validate categories..");

				parser.validateCategories();
				System.out.println("--- Validate offers..");
				parser.getOffers();

				System.out.println("--- Completed.");

			} catch (IOException e) {
            	System.out.println("File or XML DTD file not found!");
            	System.out.println(e.getMessage());
            	System.out.println("Проверьте ссылку на файл или добавьте dtd файл схемы в папку с xml документом и попробуйте снова:"); 
        	} catch (SAXParseException e) {
            	System.out.println(e.getMessage());
            	System.out.println("Строка: "+e.getLineNumber()+", Символ: "+e.getColumnNumber()+".");
            	System.out.println("--- Completed."); 
           	} catch (SAXException e) {
            	System.out.println(e.getMessage());
            	System.out.println("Попробуйте снова:"); 
           	}	
           	System.out.println("Find offerId:"); 
			input=scanner.next();
			if (input.equals("exit")) System.exit(0);
			System.out.println("--- Searching offer..");

			Offer findedOffer = Parser.findOffer(Long.parseLong(input));

			if (findedOffer==null) {
				System.out.println("Offer doesn't exist!");
			} else { 
			System.out.println("   available: "+findedOffer.getAvailable());
            System.out.println("   name: "+finded.getName());
            System.out.println("   categoryId: "+finded.getCategoryId());
            System.out.println("   price: "+findedOffer.getPrice());
        	}
		}	
	}
}



