import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ProcessControl extends OS{

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
            for (int i = 0; i < content.length; i++) {
                
                
            }
            String [] hexarr = new String[content.length];
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

            /*
             
            */
            for(int k = 0; k < hexarr.length;k++) {
                System.out.println(hexarr[k]);
            }
        }
        
    }


    
  
    








    
 
    
}
    
    

