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

    public int hexToDecimal(String in) {
        //System.out.println( "inserted values " + in);
      //  System.out.println();
      
        int x=0;
        for (int i=0;i<in.length();i++){
            if ((int)(in.charAt(i))<=57){
                x=x+((int)(in.charAt(i))-48)*(int)Math.pow(16,(in.length()-i-1));
            }
            else {
                x=x+((int)(in.charAt(i))-55)*(int)Math.pow(16,(in.length()-i-1));
            }
            System.out.println("outputted values " + x);
        }
        // System.out.println(Integer.parseInt(x));
        return x;
    }

    public String decimalToHex(int num) {
        int remainder;
        String hex = "";
        char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        if(num == 0) {
            return hex = "00";
        }
        while(num > 0) {
            remainder = num % 16;
            if(num / 16 == 0 && hex.equals("")) {
                hex = "0" + hexChar[remainder] + hex;
            } else {
                hex = hexChar[remainder] + hex;
            }

            num = num / 16;
        }

        return hex;
    }

    public void getInstruction(String IrHexVal, short pc) { //Sare pc wali values ko ek ek kam increment karna bcs ek increment fucn call sai pehle ho chuka hai

        System.out.println("program counter: " + regStorage.getSpecialRegister(3));

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
                ADDI(pc);
                break;
            case Instructions.SUBI:
                SUBI(pc);
                break;
            case Instructions.MULI:
                MULI(pc);
                break;
            case Instructions.ANDI:
                ANDI(pc);
                break;
            case Instructions.ORI:
                ORI(pc);   
                break;
            case Instructions.BZ:
                BZ(pc);
                break;
            case Instructions.BNZ:
                BNZ(pc);
                break;
            case Instructions.BC:
                BC(pc);
                break;
            case Instructions.BS:
                BS(pc);
                break;
            case Instructions.JMP: 
                JMP(pc);
                break;
            case Instructions.CALL:  
                break;
            case Instructions.ACT:   
                break;

            //Memory Instructions

            case Instructions.MOVL: 
                MOVL(pc);  
                break;
            case Instructions.MOVS: 
                MOVS(pc);  
                break;

            
            //Single final Operand Instructions

            
            case Instructions.SHL:   
                SHL(pc);
                break;
            case Instructions.SHR:
                SHR(pc);
                break;
            case Instructions.RTL:
                RTL(pc);
                break;
            case Instructions.RTR:
                RTR(pc);
                break;
            case Instructions.INC:
                INC(pc);
                break;
            case Instructions.DEC:  
                DEC(pc); 
                break;
            case Instructions.PUSH:   
                break;
            case Instructions.POP:   
                break;

            //No final operand Instructions

            case Instructions.RETURN:   
                break;
            case Instructions.NOOP:
                NOOP();   
                break;
            case Instructions.END:   
                break;
            default:
                break;
        }
    }

    public void MOV(short pc) {
        short R2 = memory.get(pc+1); //gets value from memory of the index for the gpr array. R2 is the grp index
        regStorage.setRegister(memory.get(pc), regStorage.getRegister(R2)); //gets value from index R2 in gpr and puts it in index of R1 obtained from memory
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void ADD(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 + R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));

    }

    public void SUB(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 - R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void MUL(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 * R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void DIV(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 / R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void AND(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 & R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void OR(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        int R2 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc+1)));
        int R3 = (R1 | R2);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+2));
    }

    public void MOVI(short pc) {
       

        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);    //getting second byte of immediate val from memory

       System.out.println("part 1: " + valPat1);
       System.out.println("part 2: " + valPat2);
        
        // int x=hexToDecimal(valPat1);
        // int y=hexToDecimal(valPat2);
        String concat = valPat1 + valPat2;

        concat = concat.toUpperCase();
        System.out.println("concat: " + concat);
        

        int ans = hexToDecimal(concat);
        System.out.println("decimal concat: " + ans);

      //  String concat=((Integer)(x)).toString()+((Integer)(y)).toString();
        //System.out.println("val1="+valPa"t1+"val2="+valPat2);
        //System.out.println("x="+x+" y="+y);
        regStorage.setRegister(memory.get(pc), (short)(ans));
        regStorage.setSpecialRegister(3, (short)(pc+3));

        
       
    }

    public void ADDI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 + value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void SUBI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 - value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void MULI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 * value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void DIVI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 / value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void ANDI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 & value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void ORI(short pc) {
        int R1 = Short.toUnsignedInt(regStorage.getRegister(memory.get(pc)));
        String valPat1 = decimalToHex(memory.get(pc+1) & 0xffff);  //getting immediate value par 1 from memory
        String valPat2 = decimalToHex(memory.get(pc+2) & 0xffff);  //getting immediate value par 2 from memory

        String concat = valPat1 + valPat2;
        int value = hexToDecimal(concat);

        int R3 = (R1 | value);
        flagRegister(R3);
        regStorage.setRegister(memory.get(pc), (short) R3);
        regStorage.setSpecialRegister(3, (short)(pc+3));
    }

    public void MOVL(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+2)));
        String concat = byte1 + byte2; 
        int loc = Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1);

        String part1 = decimalToHex(Short.toUnsignedInt(memory.get(loc)));
        String part2 = decimalToHex(Short.toUnsignedInt(memory.get(loc+1)));
        String concat2 = part1 + part2; 

        int R1 = Integer.parseInt(concat2, 16);
        regStorage.setSpecialRegister(memory.get(pc), (short) R1);
        flagRegister(R1);
        regStorage.setSpecialRegister(3, (short)(pc+3));

    }

    public void MOVS(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+2)));
        String concat = byte1 + byte2; 
        int loc = Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1);

        byte first8Bits = (byte) (regStorage.getRegister(memory.get(pc)) >> 8);
        byte last8Bits = (byte) (regStorage.getRegister(memory.get(pc)));

        memory.set(loc, (short) first8Bits);
        memory.set(loc+1, (short) last8Bits); 

        regStorage.setSpecialRegister(3, (short)(pc+3));
    }
    public void BZ(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String concat = byte1 + byte2;          // the check for branch happens on 2 immediate bytes from memory hence concat

        short flag = regStorage.getSpecialRegister(9);

        int check = flag & 2;
        if (check == 2)
            //adding value of codebase to int value of concatenated hex string
            regStorage.setSpecialRegister(3, (short) ((short) Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1))); 
        else
            regStorage.setSpecialRegister(3, (short) (pc + 2));

    }

    public void BNZ(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String concat = byte1 + byte2;          // the check for branch happens on 2 immediate bytes from memory hence concat

        short flag = regStorage.getSpecialRegister(9);

        int check = flag & 2;    //flag zero bit check
        if (check == 2)
            
            regStorage.setSpecialRegister(3, (short) (pc + 2));
        else
            //adding value of codebase to int value of concatenated hex string
            regStorage.setSpecialRegister(3, (short) ((short) Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1)));
            

    }

    public void BC(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String concat = byte1 + byte2;          // the check for branch happens on 2 immediate bytes from memory hence concat

        short flag = regStorage.getSpecialRegister(9);

        int check = flag & 1;
        if (check == 1)
            //adding value of codebase to int value of concatenated hex string
            regStorage.setSpecialRegister(3, (short) ((short) Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1))); 
        else
            regStorage.setSpecialRegister(3, (short) (pc + 2));

    }


    public void BS(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String concat = byte1 + byte2;          // the check for branch happens on 2 immediate bytes from memory hence concat

        short flag = regStorage.getSpecialRegister(9);

        int check = flag & 4;
        if (check == 4)
            //adding value of codebase to int value of concatenated hex string
            regStorage.setSpecialRegister(3, (short) ((short) Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1))); 
        else
            regStorage.setSpecialRegister(3, (short) (pc + 2));

    }

    public void JMP(short pc) {
        String byte1 = decimalToHex(Short.toUnsignedInt(memory.get(pc)));
        String byte2 = decimalToHex(Short.toUnsignedInt(memory.get(pc+1)));
        String concat = byte1 + byte2;          // the check for branch happens on 2 immediate bytes from memory hence concat

            //adding value of codebase to int value of concatenated hex string
        regStorage.setSpecialRegister(3, (short) ((short) Integer.parseInt(concat, 16) + regStorage.getSpecialRegister(1))); 
        

    }


    public void SHL(short pc) {
        short flag = regStorage.getSpecialRegister(9); //getting flag register
        int check1 = 32768 & regStorage.getSpecialRegister(memory.get(pc)); //getting R1 from SPR and bitwise AND with 100000000...
        if (check1 == 32768)
            flag = (short) (flag | 1);  //check for carry if msb is only on. this turns on carry bit
        else
            flag = (short) (flag & 14); //carry bit is off

        short R1 = regStorage.getRegister(memory.get(pc));
        R1 = (short) (R1 << 1);
        flagRegister(R1);
        regStorage.setRegister(memory.get(pc), R1);
        regStorage.setSpecialRegister(3, (short) (pc+1));
    }

    public void SHR(short pc) {
        short flag = regStorage.getSpecialRegister(9); //getting flag register
        int check1 = 32768 & regStorage.getSpecialRegister(memory.get(pc)); //getting R1 from SPR and bitwise AND with 100000000...
        if (check1 == 32768)
            flag = (short) (flag | 1);  //check for carry if msb is only on. this turns on carry bit
        else
            flag = (short) (flag & 14); //carry bit is off

        short R1 = regStorage.getRegister(memory.get(pc));
        R1 = (short) (R1 >> 1);
        flagRegister(R1);
        regStorage.setRegister(memory.get(pc), R1);
        regStorage.setSpecialRegister(3, (short) (pc+1));
    }

    public void RTL(short pc) {
        short R1 = regStorage.getRegister(memory.get(pc));
        R1 = (short) Integer.rotateLeft(Short.toUnsignedInt(R1), 1);
        regStorage.setRegister(memory.get(pc), R1);
        flagRegister(R1);
        regStorage.setSpecialRegister(3, (short) (pc+1));
    }

    public void RTR(short pc) {
        short R1 = regStorage.getRegister(memory.get(pc));
        R1 = (short) Integer.rotateRight(Short.toUnsignedInt(R1), 1);
        regStorage.setRegister(memory.get(pc), R1);
        flagRegister(R1);
        regStorage.setSpecialRegister(3, (short) (pc+1));
    }

    public void INC(short pc) {
        short R1 = regStorage.getRegister(memory.get(pc));
        R1++;
        regStorage.setRegister(memory.get(pc), R1);
        flagRegister(R1);
        regStorage.setSpecialRegister(3, (short)(pc+1));
    }

    public void DEC(short pc) {
        short R1 = regStorage.getRegister(memory.get(pc));
        R1--;
        regStorage.setRegister(memory.get(pc), R1);
        flagRegister(R1);
        regStorage.setSpecialRegister(3, (short)(pc+1));
    }

    public void NOOP() {

    }












    public void fetch(short base) {
        short codeBase = regStorage.getSpecialRegister(1);  //getting the value of base from code base register
       
        regStorage.setSpecialRegister(3, codeBase); //setting value of code counter aka program counter as code base value
        
        short pc = regStorage.getSpecialRegister(3);

        short ir; //instruction register
        
        
        ir = memory.get(pc);    //get value from memory into instruction register
        System.out.println(ir);
        
        System.out.println((short)Integer.parseInt(Instructions.END, 16));
         
          
         
         while(ir != (short)Integer.parseInt(Instructions.END, 16)) { //condition to only allow pc to increment till where the last instruction num sits in memory
        
        
             
             pc++;   //increment program counter
             regStorage.setSpecialRegister(3, pc); //updating code counter everytime pc increments
              
               
             
             String irHexValue = Integer.toHexString(ir & 0xffff); // converting value in IR from int to hex
             
             
             
             getInstruction(irHexValue, pc); //this calls switch case statement and matches op code and executes instruction accordingly
 
             pc = regStorage.getSpecialRegister(3);
             System.out.println(pc);
             
             ir = memory.get(pc);
             System.out.println(ir);

         }
          
         
        
      
    }

    

   
    public void cpu() {
        readFile();
        loadInstructionIntoMemory(codeBase);
        fetch(codeBase);
        regStorage.toStringmeowww();

    }

    
}
