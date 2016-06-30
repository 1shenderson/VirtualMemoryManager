package BonusACE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * ACE3 
 * @author Scott Henderson
 * 201212243
 * 
 * This program replicates a Virtual Memory Manager
 * Includes attempt at Bonus modifications
 */

public class VirtualMemoryManager {
	public static void main(String[] args) {
		run();
	}

	private static void run() {
		//create the frame table
		FrameTable ft = new FrameTable();

		//create the page table
		PageTable pt = new PageTable();

		//create physical memory
		PhysicalMemory pm = new PhysicalMemory();

		//create and then get the TLB
		TLB t = new TLB();
		HashMap<Integer, Integer> tlb = t.getTLB();

		//set up the variables needed for the virtual memory manager
		int nextAvailableFrame = 0, pointer = 0, pageFaults = 0, addressesTranslated = 0, tlbHits = 0, physicalAddress = 0, frameNumber = 0;
		int number, pageNumber, offset;
		int size = 256;

		byte memoryValue = 0;

		//create arraylist for the FIFO in the TLB
		ArrayList<Integer> fifo = new ArrayList<Integer>();

		ArrayList<Integer> fifoMemoryList = new ArrayList<Integer>();

		//set up variables for the files
		File path;
		RandomAccessFile disk;

		//get and then scan the input file
		try {
			File filename = new File("InputFile.txt");
			Scanner input = new Scanner(filename);

			//loop through the input file whilst it has another integer
			while (input.hasNextInt()) {

				//get number from the input file and then extract necessary
				//information
				number = input.nextInt();
				pageNumber = extractPageNumber(number);
				offset = extractOffset(number);

				//check to see if the TLB hashmap contains the page
				if (tlb.containsKey(pageNumber)) {

					//get frame number using the page number
					frameNumber = tlb.get(pageNumber);

					//get the physical address
					physicalAddress = frameNumber | offset;

					//get the signed byte stored at the physical memory address
					memoryValue = pm.getPhysicalMemory()[frameNumber + offset];

					//there has been a TLB hit, so add one to the counter
					tlbHits++;
				} else {
					//check the page table to see if there has been a page
					//fault
					if (pt.checkFault(pageNumber) == true) {

						try {
							path = new File("BACKING_STORE");
							disk = new RandomAccessFile(path, "r");

							//seek to correct byte position
							disk.seek(pageNumber * size);

							//read the necessary number of bytes
							disk.read(pm.getPhysicalMemory(), pointer, size);

							//get the signed byte stored at the physical memory
							//address
							memoryValue = pm.getPhysicalMemory()[pointer
							                                     + offset];

							//add the size to the pointer to get the next
							//location
							pointer++;

							//bonus, using the fifo policy
							if (fifoMemoryList.size() == 128) {
								pageNumber = fifoMemoryList.get(0);
								fifoMemoryList.remove(0);
								pm.remove(pt.getPage(pageNumber));
								pt.insertFrameNumber(pageNumber, -1);
								nextAvailableFrame = 0;
							} else {

								//insert the frame number into the page table
								//at the correct location
								fifoMemoryList.add(pageNumber);
								pt.insertFrameNumber(pageNumber,
										ft.getFrame(nextAvailableFrame));
								//get the frame number that's just be inserted
								//into the page table
								frameNumber = pt.getPage(pageNumber);
							}

							//add the value into the physical memory at the
							//correct location using the frame number
							pm.add(frameNumber, memoryValue);

							//add one so the location of the next available
							//frame is kept
							nextAvailableFrame++;

							//had a page fault so add one to the page fault
							//count
							pageFaults++;

							//get the physical address
							physicalAddress = frameNumber | offset;

							//update the tlb
							if (tlb.size() == 16) {
								//if tlb is full remove the first element that
								//was inserted, use the FIFO arraylist to get
								//that element
								tlb.remove(fifo.get(0));
								//remove that element from the FIFO arraylist
								//also
								fifo.remove(0);
							} else {
								//otherwise, add page number and frame number
								//to the TLB
								t.add(pageNumber, frameNumber);
								//keep track of the TLB using the FIFO
								//arraylist
								fifo.add(pageNumber);
							}
						} catch (IOException e) {
							System.out.println("Cannot find position.");
						}
					}
					//not in TLB and no page fault, so must be in page table
					//already
					else {
						//get the frame number from the page table
						frameNumber = pt.getPage(pageNumber);

						//get the physical address
						physicalAddress = frameNumber | offset;

						//get the signed byte stored at the physical memory
						//address
						memoryValue = pm.getPhysicalMemory()[frameNumber
						                                     + offset];
					}
				}

				//calls function to print all necessary data
				printAddresses(number, physicalAddress, memoryValue);

				//add one to the number of addresses translated, to work out
				//page fault and TLB hit rate
				addressesTranslated++;
			}
			//close the scanner
			input.close();

			//calls the function to print the rates, TLB hits and page faults
			printFaults(addressesTranslated, pageFaults, tlbHits);
		} catch (FileNotFoundException e) {
			System.out.println("No file was found.");
		}
	}

	private static int extractPageNumber(int integerNumber) {

		//extract page number from the number from the input file
		int pageNumber = (integerNumber & 0xFF00) >> 8;
		return pageNumber;
	}

	private static int extractOffset(int integerNumber) {

		//extract offset from the number from the input file
		int offset = (integerNumber & 0x00FF);
		return offset;
	}

	private static void printAddresses(int virtualAddress, int physicalAddress,
			int value) {
		//print all necessary data
		System.out.println("Virtual address: " + virtualAddress
				+ " Physical address: " + physicalAddress + " Value: " + value);
	}

	private static void printFaults(int addressesTranslated, int pageFaults,
			int tlbHits) {
		//print page faults, page fault rate, TLB hit and TLB hit rate
		System.out.println("Page faults: " + pageFaults);
		System.out.println("Page fault rate: " + (double) pageFaults
				/ addressesTranslated);
		System.out.println("TLB hits: " + tlbHits);
		System.out.println("TLB hit rate: " + (double) tlbHits
				/ addressesTranslated);
	}
}
