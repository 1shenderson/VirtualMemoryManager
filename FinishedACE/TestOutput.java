package FinishedACE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestOutput {
	
	public int check(ArrayList<Integer> output){
		
		int i = 0;
		int counter = 0;
		String skip;
		try {
			File filename = new File("correct.txt");
			Scanner correct = new Scanner(filename);
			
			//loop through the correct file whilst it has another integer
			while (correct.hasNext()) {
				if(correct.hasNextInt()){
					if(correct.nextInt() == output.get(i)){
						counter++;
					}
					i++;
				}
				else{
					skip = correct.next();
				}
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return counter;

		
	}
}

