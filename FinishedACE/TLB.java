package FinishedACE;
import java.util.HashMap;


public class TLB {
	
	private HashMap<Integer, Integer> TLB = new HashMap<Integer,Integer>(16, 2);
	
	public void add(int pageNumber, int frameNumber){
		
		//add page number and frame number into the TLB
		TLB.put(pageNumber, frameNumber);
	}
	
	public HashMap<Integer, Integer> getTLB(){
		
		//returns the entire TLB
		return TLB;
	}
}
