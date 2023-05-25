import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;


public class Process  {
	private PCB pcb;
	private Vector<String> instructions;
	
	public Process(String fileName){
		instructions = new Vector<String>();
		BufferedReader reader;
		try {
			reader= new BufferedReader(new FileReader(fileName));
			String line= reader.readLine();
			while (line!=null){
				instructions.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pcb= new PCB();
	}

	public PCB getPcb() {
		return pcb;
	}

	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}

	public Vector<String> getInstructions() {
		return instructions;
	}

	public void setInstructions(Vector<String> instructions) {
		this.instructions = instructions;
	}
	

}
