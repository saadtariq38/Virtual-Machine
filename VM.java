import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class VM extends OS {
    
    int[] stack = new int[25];
    short[] instructions;
    String filename;

    short codeBase;  //
    short sp = -1; //stack pointer
    //int codepointer; //kind of the instruction pointer

    public VM(String filename, short base ) {
        this.filename = filename;
        this.codeBase = base;

    }


    public void readFile() { //reading from file and converting hex values to an int array. One byte was needed but i stored in int which givbes 4 bytes
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            String[] fileData;
            
          
            
            String data = myReader.nextLine();
            fileData = data.split(" ");
            
            instructions = new short[fileData.length];
              for(int i = 0; i < fileData.length;i++) {
                instructions[i] = Short.parseShort(fileData[i]);
                
              }

            myReader.close();
           
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } 
       
    }

    public void loadInstructionIntoMemory(short base) {
        short codeCounter = base;
        for(int i = base, j = 0;i < instructions.length;i++, j++) {
            memory.set(i, instructions[j]); //saving into memory
            codeCounter++;
            regStorage.setRegister(3, codeCounter);     //updating register R3 aka code counter
        }
       
    }

   
    public void cpu() {
        readFile();
        loadInstructionIntoMemory(codeBase);
        System.out.println("Memory location 0: " + memory.get(0));
        System.out.println("Memory location 1: " + memory.get(1));
        System.out.println("Memory location 2: " + memory.get(2));
        System.out.println("Memory location 3: " + memory.get(3));
        System.out.println("Memory location 4: " + memory.get(4));
        System.out.println("Memory location 5: " + memory.get(5));
        System.out.println("Memory location 6: " + memory.get(6));

    }

    
}
