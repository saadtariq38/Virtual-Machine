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

    public void flagRegister(int num) {
        short flag = regStorage.getSpecialRegister(9); //getting value of flag register at index 9 of spr
        if(num == 0) {
            flag = (short) (flag | 2); //update zero flag bit to 1
            
        } else {
            flag = (short) (flag & 13); //update zero flag bit to 0
            
        }
        regStorage.setSpecialRegister(9, flag); //sets the new value of flag register with updated bit



        if (num > 32767 && num < 65536) {
            flag = (short) (flag | 4); //update flag sign bit to 1
        } else {
            flag = (short) (flag & 11); //update flag sign bit to 0
        }
        regStorage.setSpecialRegister(9, flag); //sets the new value of flag register with updated bit

        if(num > 65535) {
            flag = (short) (flag | 8); //update flag overflow bit to 1
        } else {
            flag = (short) (flag & 7); //update flag overflow bit to 0
        }
        regStorage.setSpecialRegister(9, flag); //sets the new value of flag register with updated bit
    }

    public void getInstruction(String IrHexVal, short pc) { //Sare pc wali values ko ek ek kam increment karna bcs ek increment fucn call sai pehle ho chuka hai

        //ALL PC VALS ARE +1 ALR BCS WE INCREMENTED BEFORE FUNCTION CALL IN FETCHG FUNC
        switch(IrHexVal) {
            //Register-Register Instructions

            case Instructions.MOV:
                MOV(pc);
                break; 
            case Instructions.ADD:
                ADD(pc);
                break;
            case Instructions.SUB:
                SUB(pc);
                break;
            case Instructions.MUL:
                MUL(pc);
                break;
            case Instructions.DIV:
                DIV(pc);
                break;
            case Instructions.AND:
                AND(pc);
                break;
            case Instructions.OR:
                OR(pc);
                break;

             //Register Final-Immediate Instructions

            case Instructions.MOVI:
                MOVI(pc);
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
            default:
                break;
        }
    }

    public void MOV(short pc) {
        short R2 = regStorage.getRegister(pc+1); //gets value from gpr at index pc+1
        regStorage.setRegister(pc, R2); //stores value in gpr in index pc
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void ADD(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(pc+1));
        int R3 = (R1 + R2);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));

    }

    public void SUB(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(pc+1));
        int R3 = R1-R2;
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void MUL(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(pc+1));
        int R3 = R1*R2;
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void DIV(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(pc+1));
        int R3 = R1/R2;
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void AND(short pc) {
        int R1 = regStorage.getRegister(pc);
        int R2 = regStorage.getRegister(pc+1);
        int R3 = R1 & R2;
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void OR(short pc) {
        int R1 = regStorage.getRegister(pc);
        int R2 = regStorage.getRegister(pc+1);
        int R3 = R1 | R2;
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void MOVI(short pc) {
        int val = memory.get(pc+1); //getting immediate value from memory
        regStorage.setRegister(pc, (short) val);
        regStorage.setSpecialRegister(3, (short)(pc+2));

       
    }

    public void ADDI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 + value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void SUBI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 - value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void MULI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 * value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void DIVI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 / value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void ANDI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 & value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void ORI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(pc));
        int value = memory.get(pc+1);
        int R3 = (R1 | value);
        flagRegister(R3);
        regStorage.setRegister(pc, (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }






    public void fetch(short base) {
        short codeBase = regStorage.getSpecialRegister(1);  //getting the value of base from code base register
       
        regStorage.setSpecialRegister(3, codeBase); //setting value of code counter aka program counter as code base value
        
        short pc = regStorage.getSpecialRegister(3);

        short ir; //instruction register
        
        
        ir = memory.get(pc);    //get value from memory into instruction register
        
        
        
         
        while(ir != (short)Integer.parseInt(Instructions.END, 16)) { //condition to only allow pc to increment till where the last instruction num sits in memory
       
       
            
            pc++;   //increment program counter
            regStorage.setSpecialRegister(3, pc); //updating code counter everytime pc increments
             
              
            
            String irHexValue = Integer.toHexString(ir & 0xffff); // converting value in IR from int to hex
            
            
            
            getInstruction(irHexValue, pc); //this calls switch case statement and matches op code and executes instruction accordingly

            pc = regStorage.getSpecialRegister(3);
            
            ir = memory.get(pc);
        }
         
        
      
    }

    

   
    public void cpu() {
        readFile();
        loadInstructionIntoMemory(codeBase);
        fetch(codeBase);

    }

    
}
