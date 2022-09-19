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
        regStorage.setSpecialRegister(1, base); //sets the code base register to value passed in param
        short codeCounter = base;
        for(int i = base, j = 0;i < instructions.length;i++, j++) {
            memory.set(i, instructions[j]); //saving into memory
            codeCounter++;
            regStorage.setSpecialRegister(3, codeCounter);     //updating register R3 aka code counter
        }
        regStorage.setSpecialRegister(2, codeCounter); //sets the final index where file instruction ends in the code limit register
       
    }

    public void fetch(short base) {
        short codeBase = regStorage.getSpecialRegister(1);  //getting the value of base from code base register
        regStorage.setSpecialRegister(3, codeBase); //setting value of code counter aka program counter as code base value

        short pc = regStorage.getSpecialRegister(3);

        
        while(pc <= regStorage.getSpecialRegister(2)) { //condition to only allow pc to increment till where the last instruction num sits in memory
            // putting value from memory into instruction register goes here
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
        fetch(codeBase);

    }

    
}
