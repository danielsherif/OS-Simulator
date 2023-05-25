import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Interpreter {
	private Pair[] memory;
	private int timeSlice;
	private Queue<Integer> readyList;
	private Queue<Integer> blockedOnFileAccess;
	private Queue<Integer> blockedOnReadInput;
	private Queue<Integer> blockedOnScreenOutput;
	private Queue<Integer> blocked;
	private Value fileAccess=Value.one;
	private Value readInput= Value.one;
	private Value screenOutput= Value.one;
	private int pid_file;
	private int pid_read;
	private int pid_output;
	private static int clk;
	private static int count=0;

	private int curPID;



	public Interpreter(int timeSlice){

		this.timeSlice=timeSlice;
		memory= new Pair[40];
		readyList= new LinkedList<>();
		blockedOnFileAccess =new LinkedList<>();
		blockedOnReadInput =new LinkedList<>();
		blockedOnScreenOutput =new LinkedList<>();
		blocked =new LinkedList<>();
		clk = 0;

	}
	
	public static void main(String[] args) {
		Interpreter i = new Interpreter(2);
		i.arrival(0, "src/Processes/Program_1.txt", 1, "src/Processes/Program_2.txt", 2, "src/Processes/Program_3.txt");
		
		

	}

	
	public void arrival (int a1 ,String p1, int a2 ,String p2 , int a3 , String p3){
		int counter=0;
		while (counter !=3){


			if (clk==a1){

				//this.readyList.add(new Process(p1).getPcb().getID());
				loadintomemory(new Process(p1));
				counter++;
				//break;
			}
			if (clk==a2){
				//this.readyList.add(new Process(p2));
				loadintomemory(new Process(p2));
				counter++;
				//break;
			}
			if (clk==a3){
				//this.readyList.add(new Process(p2));
				loadintomemory(new Process(p3));
				counter++;
				//break;

			}
			clk++; // :not sure



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
			memory[2]=new Pair ("pc",10);
			memory[3]=new Pair ("min",p.getPcb().getMinAddress());
			memory[4]=new Pair ("max",p.getPcb().getMaxAddress());
			p.getPcb().setMinAddress(10);
            p.getPcb().setPc(10);
			int j=0;
			for (int i=10;i<22;i++){ //deh error bardo
				if (p.getInstructions().get(j)!=null && i<p.getInstructions().size()){
					memory[i]=new Pair ("Instruction",p.getInstructions().get(j));
					j++;

				}
				//break; //shelt deh error

			}
			p.getPcb().setMaxAddress(24);

          
		}
		else if (memory[5]==null){
			memory[5]=new Pair ("id",p.getPcb().getID());
			memory[6]=new Pair ("state",p.getPcb().getState());
			memory[7]=new Pair ("pc",25);
			memory[8]=new Pair ("min",p.getPcb().getMinAddress());
			memory[9]=new Pair ("max",p.getPcb().getMaxAddress());
			p.getPcb().setMinAddress(25);
			p.getPcb().setPc(25);
			int j=0;
			for (int i=25;i<37;i++){
				if (p.getInstructions().get(j)!=null && i<p.getInstructions().size()){
					memory[i]=new Pair ("Instruction",p.getInstructions().get(j));
					j++;

				}
				//break;

			}
			p.getPcb().setMaxAddress(39); 


		}
		else 
		{
			clear();
			loadintomemory(p);

		}



	}

	public Vector<Pair> getProcessOnDisk() throws IOException{
		Vector<Pair> valuesOnDisk= new Vector();
		BufferedReader reader;
		reader= new BufferedReader(new FileReader("./src/Processes/disk.txt"));
		String line= reader.readLine();
		while (line!=null)
		{
			String [] values = line.split(" ");
			switch(values[0])
			{
			case "id":
				valuesOnDisk.add(new Pair ("id",Integer.parseInt(values[1])));
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

				valuesOnDisk.add( new Pair ("state",oldState));
				break;
			case "pc":
				valuesOnDisk.add(new Pair ("pc",Integer.parseInt(values[1])));
				break;
			case "min":
				valuesOnDisk.add(new Pair ("min",Integer.parseInt(values[1])));
				break;	
			case "max":
				valuesOnDisk.add(new Pair ("max",Integer.parseInt(values[1])));
				break;

			case "instruction":
				valuesOnDisk.add( new Pair ("instruction",(values[1])));
				break;
				
			default: valuesOnDisk.add(new Pair(values[0],values[1]));	
            // mesh mehandeleen case variables law 3amalna assign we7atena fel mem


			}
		}
		reader.close();
		return valuesOnDisk;


	}
	
	public void clearOnly(int curPID){
		String filePath = "./src/Processes/disk.txt" ;
		if (memory[0]!=null &&(int) memory[0].getValue()==curPID) 
		{
			for (int i =0; i<5; i++)
			{
				memory[i]=null;
			}
			for (int i =10; i<25; i++) //error
			{
				memory[i]=null;
			}
		}
		else if (memory[5]!=null &&(int) memory[5].getValue()==curPID)
		{
			for (int i =5; i<10; i++)
			{
				memory[i]=null;
			}
			for (int i =25; i<40; i++)
			{
				memory[i]=null;
			}
		}
	}
	// btfade el mem w bet write ely etfada lel disk
	public void clear() 
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
			for (int i =10; i<25; i++) //error
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


	public void swapMemDisk(   ) throws IOException
	{
		if(curPID == (int)memory[0].getValue()||curPID ==  (int)memory[5].getValue())
			return;
		Vector<Pair> resultFromDisk= new Vector<Pair>();
		for(int i=0;i<getProcessOnDisk().size();i++){
			resultFromDisk.add(new Pair(getProcessOnDisk().get(i).getVariable(),getProcessOnDisk().get(i).getValue()));
		}
		clear();
		boolean flag = memory[0]==null;
		int pcOld=(int)resultFromDisk.get(2).getValue();
		int minAddressOld=(int)resultFromDisk.get(3).getValue();
		int maxAddressOld= (int)resultFromDisk.get(4).getValue();
		if(flag){
			if(minAddressOld!=10){
				resultFromDisk.get(3).setValue(25);
				resultFromDisk.get(2).setValue(10+pcOld-minAddressOld);
				resultFromDisk.get(4).setValue(39);}
			int count3=0;
			for(int i=0;i<5;i++){
				memory[i]= new Pair(resultFromDisk.get(i).getVariable(),resultFromDisk.get(i).getValue());
				count3++;}
			for(int i=10;i<resultFromDisk.size()+5;i++)
				memory[i]= new Pair(resultFromDisk.get(count3).getVariable(),resultFromDisk.get(count3).getValue());
		        count3++;}
		else{
			if(minAddressOld!=25){
				resultFromDisk.get(3).setValue(10);
				resultFromDisk.get(2).setValue(25+pcOld-minAddressOld);
			    resultFromDisk.get(4).setValue(24);}
			int count2=0;
			for(int i=5;i<10;i++){
				memory[i]= new Pair(resultFromDisk.get(i).getVariable(),resultFromDisk.get(i).getValue());
				count2++;}

			for(int i=25;i<resultFromDisk.size()+20;i++)
				memory[i]= new Pair(resultFromDisk.get(count2).getVariable(),resultFromDisk.get(count2).getValue());
                count2++;
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

				fileAccess=Value.zero;
				pid_file=curPID;
			}

			else{
				for(int i=0;i<memory.length;i++){
					if(memory[i]!=null){
						if(memory[i].getVariable().equals("id") && (int)memory[i].getValue()==curPID){
							memory[i+1].setValue(State.Blocked);
							blockedOnFileAccess.add(curPID);
							blocked.add(curPID);
							break;}
					}}}

		case "ReadInput": 

			if(readInput==Value.one) {
				readInput=Value.zero;
				pid_read=curPID;
			}

			else{

				for(int i=0;i<memory.length;i++){
					if(memory[i]!=null){
						if(memory[i].getVariable().equals("id") && (int)memory[i].getValue()==curPID){
							memory[i+1].setValue(State.Blocked);
							blockedOnReadInput.add(curPID);
							blocked.add(curPID);
							break;}
					}}
			}

		case "ScreenOutput": 

			if(screenOutput==Value.one) {
				screenOutput=Value.zero;
				pid_output=curPID;

			}

			else{
				for(int i=0;i<memory.length;i++){
					if(memory[i]!=null){
						if(memory[i].getVariable().equals("id") && (int)memory[i].getValue()==curPID){
							memory[i+1].setValue(State.Blocked);
							blockedOnScreenOutput.add(curPID);
							blocked.add(curPID);
							break;
						}}}}}

	}

	public void semSignal(String resource) throws IOException
	{
		switch (resource){
		case "file": 
			if(fileAccess==Value.zero && pid_file==curPID) {
				if(blockedOnFileAccess.size()==0)
					fileAccess=Value.one;
				else{
					pid_file= blockedOnFileAccess.peek();
					boolean found=false;
					for(int i=0;i<memory.length;i++){
						if((memory[i].getVariable()).equals("id") && (int)memory[i].getValue()==pid_file && memory[i+1].getVariable().equals("state") &&memory[i+1].getValue().equals("Blocked")){
							memory[i+1].setValue(State.Ready);
							blockedOnFileAccess.remove();
							blocked.remove();
							readyList.add(pid_file);
							found=true;
						}}

					if(!found){
						Vector <Pair> changeOnDisk= new Vector<Pair>();
						for(int i=0;i<getProcessOnDisk().size();i++){
							changeOnDisk.add(new Pair(getProcessOnDisk().get(i).getVariable(),getProcessOnDisk().get(i).getValue()));
						}

						String filePath = "./src/Processes/disk.txt" ;
						String dataToWrite = "";
						for (int i =0; i<changeOnDisk.size(); i++)
						{           dataToWrite+= changeOnDisk.get(i).getVariable() + " ";
						dataToWrite+= changeOnDisk.get(i).getValue() +"/n";

						}
						try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, false ))) {
							out.println(dataToWrite);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				}}



		case "userOutput": 
			if(screenOutput==Value.zero && pid_output==curPID) {
				if(blockedOnScreenOutput.size()==0)
					screenOutput=Value.one;
				else{
					pid_output= blockedOnScreenOutput.peek();
					boolean found=false;
					for(int i=0;i<memory.length;i++){
						if((memory[i].getVariable()).equals("id") && (int)memory[i].getValue()==pid_output && memory[i+1].getVariable().equals("state") &&memory[i+1].getValue().equals("Blocked")){
							memory[i+1].setValue(State.Ready);
							blockedOnScreenOutput.remove();
							blocked.remove();
							readyList.add(pid_output);
							found=true;
						}}

					if(!found){
						Vector <Pair> changeOnDisk= new Vector();
						for(int i=0;i<getProcessOnDisk().size();i++){
							changeOnDisk.add(new Pair(getProcessOnDisk().get(i).getVariable(),getProcessOnDisk().get(i).getValue()));
						}

						String filePath = "./src/Processes/disk.txt" ;
						String dataToWrite = "";
						for (int i =0; i<changeOnDisk.size(); i++)
						{           dataToWrite+= changeOnDisk.get(i).getVariable() + " ";
						dataToWrite+= changeOnDisk.get(i).getValue() +"/n";

						}
						try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, false ))) {
							out.println(dataToWrite);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				}}
		case "userInput": 
			if(readInput==Value.zero && pid_read==curPID) {
				if(blockedOnReadInput.size()==0)
					readInput=Value.one;
				else{
					pid_read= blockedOnReadInput.peek();
					boolean found=false;
					for(int i=0;i<memory.length;i++){
						if((memory[i].getVariable()).equals("id") && (int)memory[i].getValue()==pid_read && memory[i+1].getVariable().equals("state") &&memory[i+1].getValue().equals("Blocked")){
							memory[i+1].setValue(State.Ready);
							blockedOnScreenOutput.remove();
							blocked.remove();
							readyList.add(pid_read);
							found=true;
						}}

					if(!found){
						Vector <Pair> changeOnDisk= new Vector<Pair>();
						for(int i=0;i<getProcessOnDisk().size();i++){
							changeOnDisk.add(new Pair(getProcessOnDisk().get(i).getVariable(),getProcessOnDisk().get(i).getValue()));
						}

						String filePath = "./src/Processes/disk.txt" ;
						String dataToWrite = "";
						for (int i =0; i<changeOnDisk.size(); i++)
						{           dataToWrite+= changeOnDisk.get(i).getVariable() + " ";
						dataToWrite+= changeOnDisk.get(i).getValue() +"/n";

						}
						try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, false ))) {
							out.println(dataToWrite);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				}}
		}
	}


	public void execute( ) throws IOException
	{
		if(curPID !=(int) memory[0].getValue()&&  curPID != (int) memory[5].getValue())
			swapMemDisk();

		//change el state men ready le running 
		// el sem signal w wait wel hbal da 
		int x=clk+timeSlice;
		if(memory[0]!= null||memory[5]!= null)
		{

			if(curPID ==(int) memory[0].getValue())
			{
				int pc=(int) memory[2].getValue();
				for(int i= 0; i<timeSlice && pc<22; i++)
				{
					if(memory[pc]!= null )
					{
						String[] words = ((String) memory[pc].getValue()).split(" ");
						String input1= words[0];
						String input2= words[1];
						String input3="";
						String input4="";
						if (words.length >= 3) {
							input3 = words[2];
						}

						if (words.length >= 4) {
							input4 = words[3];
						}

						implement(clk, x,input1 , input2, input3 , input4);
						pc++;
					}

				}
				memory[2].setValue(pc);
				
			}
			else 
			{   
				int pc=(int) memory[7].getValue();
				for(int i= 0; i<timeSlice && pc<37; i++)
				{
					if(memory[pc]!= null)
					{
						String[] words = ((String) memory[pc].getValue()).split(" ");
						String input1= words[0];
						String input2= words[1];
						String input3="";
						String input4="";
						if (words.length >= 3) {
							input3 = words[2];
						}

						if (words.length >= 4) {
							input4 = words[3];
						}

						implement(clk,x,input1 , input2, input3 , input4);
						pc++;
					}
					

				}
				memory[7].setValue(pc);
			}
		}
	}

     
	public void scheduleProcesses() throws IOException{
		if(readyList.isEmpty())
			return;
		if((int)memory[0].getValue()==curPID){
			int lastInstructionAddress=-1;
			for(int i=10;i<22;i++){
				if(memory[i]!=null &&memory[i].getVariable().equals("instruction"))
				   lastInstructionAddress=i;}
			if(lastInstructionAddress==(int)memory[2].getValue()){
				count++;
				clearOnly(curPID);
			}}
		else if((int)memory[5].getValue()==curPID){
				int lastInstructionAddress=-1;
				for(int i=25;i<37;i++){
					if(memory[i]!=null &&memory[i].getVariable().equals("instruction"))
					   lastInstructionAddress=i;}
				if(lastInstructionAddress==(int)memory[2].getValue()){
					count++;
					clearOnly(curPID);
				}}
		else {
			if(memory[0]!=null && (int)memory[0].getValue()==curPID){
				if(!(memory[1].getValue()==State.Blocked)){
					readyList.add(curPID);
					memory[1].setValue(State.Ready);
				}
			}
			else if(!(memory[6].getValue()==State.Blocked)){
					readyList.add(curPID);
					memory[6].setValue(State.Ready);
					
				}}
		if(readyList.peek()!=null){
			curPID=readyList.remove();
			swapMemDisk();
			execute();
			scheduleProcesses();
		}
	}
			
			
			
		
	


	public void implement(int clk,int incrementedClk, String input1 , String input2, String input3,  String input4 ) throws IOException 
	{  if(clk>=incrementedClk)
		return;
	
		Object value;
        switch(input1)
		{
		case "semWait" :
			semWait(input2);
			break;

		case "semSignal":
			semSignal(input2);
			break;

		case "printFromTo":	

			int y= Integer.parseInt(input3);
			for (int x= Integer.parseInt(input2); x<y; x++)
			{
				System.out.print(x);
			}
			break;

		case "assign":

		
			if(input3.equals("input"))
			{
				String in= (String) getY(input2,"");
				implement(clk,incrementedClk,input1, input2, in, "");
                
			}
			else if (input3.equals("readFile"))
			{
				String in= (String) getY(input3,input4);
				implement(clk,incrementedClk,input1, input2 , in, "");
			}
			else 
			{ 
				if(containsOnlyNumbers(input3))
				{
					value= Integer.parseInt(input3);

				}
				else 
				{
					value= input3;
				}
               if((int)memory[0].getValue()==curPID){
            	   for(int j=22;j<25;j++){
            		   if(memory[j]==null){
            			   memory[j]= new Pair(input2,value);
            	           break;}
               }
				
			}
               else{
            	   for(int j=37;j<40;j++){
            		   if(memory[j]==null){
            			   memory[j]= new Pair(input2,value);
            	           break;}}
               }}

			

			break;

		case "print":
			System.out.print(input2);
			break;

		case "writeFile":

			String filePath =input2 + ".txt";
			String dataToWrite = input3;
			try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath))) {
				out.println(dataToWrite);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "readFile":
			readFile(input2);

			break;

		}
		clk++;
		return;
	}


	public Object getY(String y, String yy) throws IOException
	{
		Object out= null;

		if (y.equals("input"))
		{
			Scanner scanner = new Scanner(System.in);
			System.out.print("Please enter a value");
			out = scanner.nextLine(); // Read a line of text from the user
		}
		else 
			out = readFile(yy);

		return out;
	}


	public static boolean containsOnlyNumbers(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static String readFile(String fileName) {
		StringBuilder content = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content.toString();
	}
	

	/*	BufferedReader reader;
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
	}*/




}









