import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class Interpreter {
private Pair[] memory;
private int timeSlice;
private Queue<Process> readyList;
private Queue<Integer> blockedOnFileAccess;
private Queue<Integer> blockedOnReadInput;
private Queue<Integer> blockedOnScreenOutput;
private Value fileAccess=Value.one;
private Value readInput= Value.one;
private Value screenOutput= Value.one;
private int pid_file;
private int pid_read;
private int pid_output;

public Interpreter(int timeSlice){
	
	this.timeSlice=timeSlice;
	memory= new Pair[40];
	readyList= new LinkedList<>();
	blockedOnFileAccess =new LinkedList<>();
	blockedOnReadInput =new LinkedList<>();
	blockedOnScreenOutput =new LinkedList<>();
	
}

public void executeInstructions(Vector<String> instruction){
    
	switch(instruction.get(0)){

	}
	
}

public void semWait(String resource){
	switch (resource){
	case "FileAccess": 
		
		if(fileAccess==Value.one) {
		int idWithResource=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		fileAccess=Value.zero;
		pid_file=idWithResource;
		}
		
	else{int pid=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				pid= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				memory[i].setValue(State.Blocked);
				blockedOnFileAccess.add(pid);
				break;
		}}}
		
	case "ReadInput": 
		
		if(readInput==Value.one) {
		int idWithResource=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		readInput=Value.zero;
		pid_read=idWithResource;
		}
	
		else{int pid=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				pid= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				memory[i].setValue(State.Blocked);
				blockedOnReadInput.add(pid);
				break;
		}}}
	
	case "ScreenOutput": 
		
		if(screenOutput==Value.one) {
		int idWithResource=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		screenOutput=Value.zero;
		pid_output=idWithResource;
		}
	
		else{int pid=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				pid= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				memory[i].setValue(State.Blocked);
				blockedOnScreenOutput.add(pid);
				break;
		}}}
		
	}
	
}

public void semSignal(String resource){
	switch (resource){
	case "FileAccess": 
		int idWithResource=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		if(fileAccess==Value.zero && pid_file==idWithResource) {
	            if(blockedOnFileAccess.size()==0)
	            	fileAccess=Value.one;
	            else{
	            	int newProcessWithResource=-1;
	            	pid_file= blockedOnFileAccess.peek();
	            	for(int i=0;i<memory.length;i++){
	        			if(memory[i].getVariable().equals("ID"))
	        				newProcessWithResource= (int) memory[i].getValue();
	        			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Blocked && newProcessWithResource==pid_file){
	        				memory[i].setValue(State.Ready);
	        		}}
	            }
		}
		

		
	case "ReadInput": 
		int idWithResource2=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource2= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		if(readInput==Value.zero && pid_read==idWithResource2) {
	            if(blockedOnReadInput.size()==0)
	            	readInput=Value.one;
	            else{
	            	int newProcessWithResource=-1;
	            	pid_read= blockedOnReadInput.peek();
	            	for(int i=0;i<memory.length;i++){
	        			if(memory[i].getVariable().equals("ID"))
	        				newProcessWithResource= (int) memory[i].getValue();
	        			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Blocked && newProcessWithResource==pid_file){
	        				memory[i].setValue(State.Ready);
	        		}}
	            }
		}
		
	case "ScreenOutput": 
		int idWithResource3=-1;
		for(int i=0;i<memory.length;i++){
			if(memory[i].getVariable().equals("ID"))
				idWithResource3= (int) memory[i].getValue();
			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Running){
				break;
		}}
		if(readInput==Value.zero && pid_read==idWithResource3) {
	            if(blockedOnScreenOutput.size()==0)
	            	screenOutput=Value.one;
	            else{
	            	int newProcessWithResource=-1;
	            	pid_read= blockedOnScreenOutput.peek();
	            	for(int i=0;i<memory.length;i++){
	        			if(memory[i].getVariable().equals("ID"))
	        				newProcessWithResource= (int) memory[i].getValue();
	        			if((memory[i].getVariable()).equals("state") && memory[i].getValue()==State.Blocked && newProcessWithResource==pid_file){
	        				memory[i].setValue(State.Ready);
	        		}}
	            }
		}
		
		
	}
	
}





}
