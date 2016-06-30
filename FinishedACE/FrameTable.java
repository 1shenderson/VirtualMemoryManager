package FinishedACE;
public class FrameTable {

	int frameTableSize = 256;
	int[] frameTable = new int[frameTableSize];

	public FrameTable(){
		initialiseFrameTable();
	}
	public void initialiseFrameTable() {
		
		//initialise the table by making each element at every index (i * frameTableSize)
		for (int i = 0; i < frameTableSize; i++) {
			frameTable[i] = (i * 256);
		}
	}

	public int getFrame(int frame) {

		//get a specific frame from the frame table
		return frameTable[frame];
	}
}