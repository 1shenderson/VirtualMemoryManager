package FinishedACE;

public class PageTable {

	int pageTableSize = 256;
	int[] pageTable = new int[pageTableSize];
	
	public PageTable(){
		initialiseTable();
	}
	
	
	public void initialiseTable(){
		
		//initialise the table by making each element at every index -1
		for(int i = 0; i < pageTableSize; i++){
			pageTable[i] = -1;
		}
	}
	
	public boolean checkFault(int page){
		
		//check the page table to see if the page is -1, if so there has been a page fault
		if(pageTable[page] == -1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void insertFrameNumber(int pageNumber, int frame){
		
		//insert the frame number into the page table at the appropriate index
		pageTable[pageNumber] = frame;
	}
	
	public int getPage(int pageNumber){
		
		//get a specific page from the page table
		return pageTable[pageNumber];
	}
	
	public int[] getPageTable(){
		return pageTable;
	}
}
