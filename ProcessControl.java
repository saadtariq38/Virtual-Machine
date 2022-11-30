import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class ProcessControl extends OS{


    String [] hexarr;
    int pageCount;
    Queue<PCB> FCFSQueue = queues.getFCFSQueue();       //the queues are declared in the OS class since they are common to all processes
    PriorityQueue<PCB> pQueue = queues.getPriorityQueue();  //same goes for this

    
    public ProcessControl() {
        
    }
    

    public void convertAndReadBinProcessFiles() throws IOException {

        String[] filename = new String[6];//array which stores all process file paths to be accessed
        filename[0] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/p5";
        filename[1] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/power";
        filename[2] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/flags";
        filename[3] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/large0";
        filename[4] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/sfull";
        filename[5] = "/Users/Saad/OneDrive - Institute of Business Administration/IBA code/5th Semester/OS Virtual Machine/noop";

        for (int j = 0; j < 6;j++) {  //THIS LOOP READS EACH FILEPATH, CONVERTS FILE TO HEX VAULES AND PARSES ALL THE NEEDED VALUES AND INSERTS INTO MEMORY VIA PAGING AND CREATES PCBS AND ADDS THEM INTO READYQUEUE
            byte[] content = Files.readAllBytes(Paths.get(filename[j]));
            hexarr = new String[content.length];
            for (int s = 0; s < content.length; s++) {        //converting bytes read from binary file into hex values
                hexarr[s]=Integer.toHexString(content[s]);
               
               
                if (hexarr[s].length() < 2) {
                    hexarr[s] = 0+hexarr[s];
                }
                if (hexarr[s].length() > 2) {
                    hexarr[s] = hexarr[s].substring(hexarr[s].length()-2,hexarr[s].length());
                }
            }

            String name = filename[j].substring(80, filename[j].length());
            System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
            System.out.println("Loading process : "+name);
            int processPriority = Integer.parseInt(hexarr[0],16); //parsing process priority from first byte
            System.out.println("Process priority: "+processPriority);
            int processID = Integer.parseInt(hexarr[1]+hexarr[2],16); //parsing process id from second and third byte
            System.out.println("Process ID: "+processID);
            int dataSize = Integer.parseInt(hexarr[3]+hexarr[4],16); //parsing data size from third and fourth byte
            System.out.println("Data Size: "+dataSize);
            int processSize = hexarr.length;
            System.out.println("Process size: "+processSize);
            int codeSize = processSize - dataSize - 8;
            System.out.println("Code size: "+codeSize);


            PCB pcb = new PCB((long)processPriority,(long)processID,processSize,dataSize,name);
            if (pcb.processPriority < 16 && pcb.processPriority >= 0){      //condition to add process to priority queue
                pQueue.add(pcb);
            } else if(pcb.processPriority >= 16 && pcb.processPriority < 32){   //condition to add process to FCFS queue
                FCFSQueue.add(pcb);
            }

            loadIntoMem(pcb); 
            
            memory.printMem();
           // memory.printVirtualMem();
            

            System.out.println("IDFHGDGIDLIGLHIDFBHIFBIFIBIFBIFBHFGAFHGLAHFGHFGFGAKFHG");           //line break pls dont judge thankyou
             
            /*
             
            for(int k = 0; k < hexarr.length;k++) {
                System.out.println(hexarr[k]);
            }
             */
            
        }
        
        
    }


    public void loadIntoMem(PCB pcb) {// this is called inside convertAndReadBinProcessFiles() function
        String s = "";
        int k = 0;
	       	   int pagenum = 0;
	       	   if (pcb.getProcessSize() > 128) { //LOADS INTO MEMORY VIA PAGING
	       		   
	       		    for (int i = 8; i < pcb.getProcessSize(); i++) {
	       		    	 s = s+hexarr[i]+" ";
	       		    	 k++;
		       		     if (k == 128 || i == pcb.getProcessSize()-1) {
                            int  firstfreeIndex = memory.findFirstFreeFrameIndex();
                            memory.loadDataIntoFrame(firstfreeIndex, s, memory);
                            pcb.pageTable[pagenum] = firstfreeIndex;
                            System.out.println("page table index " + pagenum + " has value " + firstfreeIndex);
		       		          // mem.loadIntoFrameNumber(pcb.returnPageTables()[pagenum],s);
		       		           pagenum++;
		       		           s = "";
		       		           k = 0;
		       	         }
		       	    }
                    pageCount = pagenum + 1;        //saves the number of pages used out of 512 to help when reading thm later
	       	   } else {
	       		    for (int i = 8; i < pcb.getProcessSize(); i++) {
		                s = s+hexarr[i]+" ";
		             }
                    int  firstfreeIndex = memory.findFirstFreeFrameIndex();
                    memory.loadDataIntoFrame(firstfreeIndex, s, memory);
		       }
    }


    public int getFrameNum(PCB pcb, int pageNum) {      //get the frame number from the page table
        return pcb.pageTable[pageNum];
    }

    public int getFrameBaseIndex(int frameNum) {        //uses frame num to calculate the starting index of that particular frame to add the offset to
        return frameNum * 128;
    }

    public void loadOneFrameToVirtualMemory(int frameBase, int vmStartingIndex) {
        System.out.println("vm starting index " + vmStartingIndex);
        for(int i=vmStartingIndex; i < vmStartingIndex + 128 ;i++) {
            memory.VirtualSet(i, memory.get(frameBase));    //gets data from physical memory and stores it i virtual memory
            frameBase++;        // this increments the physical memory index whereas i is the virtual memory index.
                                //all oof the frames 128 bytes are stored in the virtual memory.
        }
    }

    public void StoreAllFramesInVirtualMemory(PCB pcb, int pageCount) {        // stores all frames from physcial memory to the virtual memory
        int startingPos = 0;                                    // saves starting position for the next frame and starts saving fro there.
        int i = 0;
        while(i < pageCount) {           
            System.out.println("i is : " + i);                   //for each page num i.e each frame of that process
            int frameNum = getFrameNum(pcb, i);    
            System.out.println("frame num: " + frameNum);                 //get frame num
            int frameIndex = getFrameBaseIndex(frameNum);  
            System.out.println("frame index: "  + frameIndex);         //get starting index of that frame in physical memory
            loadOneFrameToVirtualMemory(frameIndex, startingPos);   //gets frame into virtual memory using starting pos in vm for that frame
                                                                    //and uses frame index to get data from physical memory.
            startingPos+=128;
            i++;
            
        }
    }

    



















    
  
    








    
 
    
}
    
    







