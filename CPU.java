import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CPU extends ProcessControl{

    Path filePath = Path.of("p0.txt");// since my phase 1 was designed to read and exec from a file i keep overwriting a single file
                                            //  with info from the pcb at each iteration
                                            String s = "";
//Files.writeString(filePath, content);



    public void exec() throws IOException {

        while(!queues.pQueue.isEmpty() || !queues.FCFSQueue.isEmpty()) {     //the process keeps executing until both queues are empty
           if(!queues.pQueue.isEmpty()) {
                PCB process = queues.pQueue.remove();   //remove process from priority queue
                memory.clearVirtualMem();   //clear virtual memory in case data and code from prev process still exists
                StoreAllFramesInVirtualMemory(process, pageCount); // Stores code and data in Virtual mem. pageCount passed here to know where to stop reading page table

            

                int codeSize = process.getProcessSize() - process.getDataSize() - 8;   //getting length of the code
                int codeBase = process.getDataSize();        //the code starts from where data ends in virtual memory. Index of where code starts

                for (int i = codeBase; i < codeSize;i++) {          //reading virtual memory till the process wrote to it
                        s = s + Short.toString(memory.VirtualGet(i));      //adds all code values to a string
                }

                Files.writeString(filePath, s);      //writes complete code values in string s to file p0

                VM vm = new VM("p0.txt", (short)0);
                vm.cpu();

            } else {
                PCB process = queues.FCFSQueue.remove();   //remove process from priority queue
                memory.clearVirtualMem();   //clear virtual memory in case data and code from prev process still exists
                StoreAllFramesInVirtualMemory(process, pageCount); //store data and code into virtual memory
            

                int codeSize = process.getProcessSize() - process.getDataSize() - 8;   //getting length of the code
                int codeBase = process.getDataSize();        //the code starts from where data ends in virtual memory. Index of where code starts

                for (int i = codeBase; i < codeSize;i++) {          //reading virtual memory till the process wrote to it
                        s = s + Short.toString(memory.VirtualGet(i));      //adds all code values to a string
                }

                Files.writeString(filePath, s);      //writes complete code values in string s to file p0

                VM vm = new VM("p0.txt", (short)0);
                vm.cpu();
            }
            
        }
    }

    public CPU() {

    }
    
}
