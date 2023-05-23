import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class Interpreter {
private Pair[] memory;
private int timeSlice;
private Queue<Integer> readyList;
private Queue<Integer> blockedOnFileAccess;
private Queue<Integer> blockedOnReadInput;
private Queue<Integer> blockedOnScreenOutput;
private Value fileAccess=Value.one;
private Value readInput= Value.one;
private Value screenOutput= Value.one;
private int pid_file;
private int pid_read;
private int pid_output;
private static int clk;
private static int count=3;

private int curPID;



public Interpreter(int timeSlice){
	
	this.timeSlice=timeSlice;
	memory= new Pair[40];
	readyList= new LinkedList<>();
	blockedOnFileAccess =new LinkedList<>();
	blockedOnReadInput =new LinkedList<>();
	blockedOnScreenOutput =new LinkedList<>();
	
}
public void arrival (int a1 ,String p1, int a2 ,String p2 , int a3 , String p3){
	while (true){
	for(int i=0;i<count;i++){
	if (clk==a1){
		
		//this.readyList.add(new Process(p1).getPcb().getID());
		loadintomemory(new Process(p1));
		
		break;
	}
	if (clk==a2){
		//this.readyList.add(new Process(p2));
		loadintomemory(new Process(p2));
		break;
	}
	if (clk==a3){
		//this.readyList.add(new Process(p2));
		loadintomemory(new Process(p3));
		break;
	
	}
	clk++; // :not sure
	
	}	
	
	}
}





public  void loadintomemory(Process p)
{
	//maybe greater than 15
	
	//3ayzen 
	if(! (this.readyList.contains(p.getPcb().getID()) ))
		this.readyList.add(p.getPcb().getID());
	if (memory[0]==null){
		memory[0]=new Pair ("id",p.getPcb().getID());
		memory[1]=new Pair ("state",p.getPcb().getState());
		memory[2]=new Pair ("pc",p.getPcb().getPc());
		memory[3]=new Pair ("min",p.getPcb().getMinAddress());
		memory[4]=new Pair ("max",p.getPcb().getMaxAddress());
		p.getPcb().setMinAddress(10);
		
		int j=0;
		for (int i=10;i<25;i++){
			if (p.getInstructions().get(j)!=null){
			memory[i]=new Pair ("Instruction",p.getInstructions().get(j));
			j++;
			
			}
			break;
			
		}
		p.getPcb().setMaxAddress(j);
		
		
	}
	else if (memory[5]==null){
		memory[5]=new Pair ("id",p.getPcb().getID());
		memory[6]=new Pair ("state",p.getPcb().getState());
		memory[7]=new Pair ("pc",p.getPcb().getPc());
		memory[8]=new Pair ("min",p.getPcb().getMinAddress());
		memory[9]=new Pair ("max",p.getPcb().getMaxAddress());
		p.getPcb().setMinAddress(25);
		int j=0;
		for (int i=25;i<40;i++){
			if (p.getInstructions().get(j)!=null){
			memory[i]=new Pair ("Instruction",p.getInstructions().get(j));
			j++;
			
			}
			break;
			
		}
		p.getPcb().setMaxAddress(24+j); 
		
		
	}
	else 
	{
		clear();
		loadintomemory(p);
		
	}
	

		
}
//bas btfade el mem
private void clear() 
{

  String filePath = "./src/Processes/disk.txt" ;
  String dataToWrite = "";
	if (memory[1].getValue()!=State.Running) 
	{
		for (int i =0; i<5; i++)
		{
			dataToWrite+= memory[i].getVariable() + " ";
			dataToWrite+= memory[i].getValue() +"/n";
			memory[i]=null;
		}
		for (int i =10; i<25; i++)
		{
			dataToWrite+= memory[i].getVariable() + " ";
			dataToWrite+= memory[i].getValue() +"/n";
			memory[i]=null;
		}
	}
	else if (memory[6].getValue()!=State.Running)
		{
			for (int i =5; i<10; i++)
			{
				dataToWrite+= memory[i].getVariable() + " ";
				dataToWrite+= memory[i].getValue() +"/n";
				memory[i]=null;
			}
			for (int i =25; i<40; i++)
			{
				dataToWrite+= memory[i].getVariable() + " ";
				dataToWrite+= memory[i].getValue() +"/n";
				memory[i]=null;
			}
		}

  try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, false ))) {
      out.println(dataToWrite);
  } catch (FileNotFoundException e) {
      e.printStackTrace();
  }
}
	
	
private void swapMemDisk(  int idToRun )
{
	clear();
	if(idToRun == (int)memory[0].getValue()||idToRun ==  (int)memory[5].getValue())
		return;
	
	int count =0;
	boolean flag = memory[0]==null;
	BufferedReader reader;
	try {
		reader= new BufferedReader(new FileReader("./src/Processes/disk.txt"));
		String line= reader.readLine();
		if(flag)
	{
			while (line!=null)
		{
			String [] values = line.split(" ");
			switch(values[0])
			{
			case "id":
				memory[0]= new Pair ("id",Integer.parseInt(values[1]));
				break;
			case "state":
				
				
				State oldState = null;
				if(values[1].equals("Running"))
					oldState=State.Running;
				if(values[1].equals("Blocked"))
					oldState=State.Blocked;
				if(values[1].equals("Ready"))
					oldState=State.Ready;
				if(values[1].equals("Finished"))
					oldState=State.Finished;
				
				memory[1]= new Pair ("state",oldState);
				break;
			case "pc":
				memory[2]= new Pair ("pc",Integer.parseInt(values[1]));
				break;
			case "min":
				memory[3]= new Pair ("min",Integer.parseInt(values[1]));
				break;	
			case "max":
				memory[4]= new Pair ("max",Integer.parseInt(values[1]));
				break;
				
			case "instuction":
				memory[10+count]= new Pair ("instuction",(values[1]));
				count++;
				break;
					
			
			}
		}
	}
		else 
		{
			while (line!=null)
			{
				String [] values = line.split(" ");
				switch(values[0])
				{
				case "id":
					memory[5]= new Pair ("id",Integer.parseInt(values[1]));
					break;
				case "state":
					State oldState = null;
					if(values[1].equals("Running"))
						oldState=State.Running;
					if(values[1].equals("Blocked"))
						oldState=State.Blocked;
					if(values[1].equals("Ready"))
						oldState=State.Ready;
					if(values[1].equals("Finished"))
						oldState=State.Finished;
					
					memory[6]= new Pair ("state",oldState);
					break;
				case "pc":
					memory[7]= new Pair ("pc",Integer.parseInt(values[1]));
					break;
				case "min":
					memory[8]= new Pair ("min",Integer.parseInt(values[1]));
					break;	
				case "max":
					memory[9]= new Pair ("max",Integer.parseInt(values[1]));
					break;
					
				case "instuction":
					memory[25+count]= new Pair ("instuction",(values[1]));
					count++;
					break;
						
				
				}
			}
		}
			
			
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
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
