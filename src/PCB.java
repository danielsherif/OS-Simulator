

public class PCB  {
	private int ID;
	private State state;
	private int pc;
	private int minAddress;
	private int maxAddress;
	private static int count=0;
	
	public PCB() {
		++count;
		ID = count;
		state = State.Ready;
	
		pc = 0;
		
	}




	public int getMinAddress() {
		return minAddress;
	}


	public void setMinAddress(int minAddress) {
		this.minAddress = minAddress;
	}


	public int getMaxAddress() {
		return maxAddress;
	}


	public void setMaxAddress(int maxAddress) {
		this.maxAddress = maxAddress;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public State getState() {
		return state;
	}


	public void setState(State state) {
		this.state = state;
	}


	public int getPc() {
		return pc;
	}


	public void setPc(int pc) {
		this.pc = pc;
	}
	
	
	
	
}
