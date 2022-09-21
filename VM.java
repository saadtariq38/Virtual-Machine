import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class VM extends OS {
    
    short[] stack = new short[25];
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
                instructions[i] = (short)Integer.parseInt(fileData[i], 16);
                
                
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
            regStorage.setSpecialRegister(3, codeCounter);     //updating register R3 aka code counter. CODE COUNTER VAL 12 HERE    
        }
        regStorage.setSpecialRegister(2, codeCounter); //sets the final index where file instruction ends in the code limit register
        
    }

    public void getInstruction(String IrHexVal, short pc) { //Sare pc wali values ko ek ek kam increment karna bcs ek increment fucn call sai pehle ho chuka hai

        //ALL PC VALS ARE +1 ALR BCS WE INCREMENTED BEFORE FUNCTION CALL IN FETCHG FUNC
        switch(IrHexVal) {
            //Register-Register Instructions

            case Instructions.MOV:
               break; 
            case Instructions.ADD:
                break;
            case Instructions.SUB:
                break;
            case Instructions.MUL:
                break;
            case Instructions.AND:
                break;
            case Instructions.OR:
                break;

             //Register Final-Immediate Instructions

            case Instructions.MOVI:
                short R2 = regStorage.getRegister(pc+1); //gets value from gpr at index pc+1
                regStorage.setRegister(pc, R2); //stores value in gpr in index pc
                regStorage.setSpecialRegister(3, (short)(pc+2));
                break;  
            case Instructions.ADDI:
                break;
            case Instructions.SUBI:
                break;
            case Instructions.MULI:
                break;
            case Instructions.ANDI:
                break;
            case Instructions.ORI:   
                break;
            case Instructions.BZ:
                break;
            case Instructions.BNZ:
                break;
            case Instructions.BC:
                break;
            case Instructions.BS:
                break;
            case Instructions.JMP:   
                break;
            case Instructions.CALL:   
                break;
            case Instructions.ACT:   
                break;

            //Memory Instructions

            case Instructions.MOVL:   
                break;
            case Instructions.MOVS:   
                break;

            
            //Single final Operand Instructions

            
            case Instructions.SHL:   
                break;
            case Instructions.SHR:
                break;
            case Instructions.RTL:
                break;
            case Instructions.RTR:
                break;
            case Instructions.INC:
                break;
            case Instructions.DEC:   
                break;
            case Instructions.PUSH:   
                break;
            case Instructions.POP:   
                break;

            //No final operand Instructions

            case Instructions.RETURN:   
                break;
            case Instructions.NOOP:   
                break;
            case Instructions.END:   
                break;
            
        }
    }

    public void fetch(short base) {
        short codeBase = regStorage.getSpecialRegister(1);  //getting the value of base from code base register
        System.out.println("code base" + codeBase);
        regStorage.setSpecialRegister(3, codeBase); //setting value of code counter aka program counter as code base value
        
        short pc = regStorage.getSpecialRegister(3);

        short ir; //instruction register
       /*  while(pc <= regStorage.getSpecialRegister(2)) { //condition to only allow pc to increment till where the last instruction num sits in memory
        
        
        
       }
       
       */
      ir = memory.get(pc);    //get value from memory into instruction register
      
      pc++;   //increment program counter
      regStorage.setSpecialRegister(3, pc); //updating code counter everytime pc increments
       
        
      
      String irHexValue = Integer.toHexString(ir & 0xffff); // converting value in IR from int to hex
      

      
      getInstruction(irHexValue, pc); //this calls switch case statement and matches op code and executes instruction accordingly

      
    }

    

   
    public void cpu() {
        readFile();
        loadInstructionIntoMemory(codeBase);
        fetch(codeBase);

    }

    
}
