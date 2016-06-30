package FinishedACE;
public class PhysicalMemory {

	int size = 256;
	byte physicalMemory[] = new byte[size * size];

	public void add(int frame, byte value) {

		//add value into physical memory
		physicalMemory[frame] = value;
	}
	
	public byte[] getPhysicalMemory() {

		//returns all of the physical memory
		return physicalMemory;
	}

}
