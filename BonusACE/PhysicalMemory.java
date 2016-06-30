package BonusACE;
public class PhysicalMemory {

	int size = 256;
	byte physicalMemory[] = new byte[128 * size];

	public void add(int frame, byte value) {

		physicalMemory[frame] = value;
	}

	public void setMemory(byte[] array) {
		physicalMemory = array;
	}

	public void printMemory() {
		for (int i = 0; i < size * size; i++) {
			System.out.println(i + ". " + physicalMemory[i]);
		}
	}

	public void remove(int frame) {
		physicalMemory[frame] = 0;
	}

	public byte[] getPhysicalMemory() {

		return physicalMemory;
	}

}
