public class Memory {
    byte[] memory;
    byte[] virtualMemory;

    char[] freeFrameList;


    public Memory() {
        memory = new byte[65536];
        virtualMemory = new byte[65536];
        freeFrameList = new char[65536];
        for (int s = 0;s < freeFrameList.length;s++) {      //this initialises the free frame list with char values of Y since all frames are 
            freeFrameList[s] = 'Y';                         // initially free
        }
    }

    public int findFirstFreeFrameIndex() { //this func is finding the first free frame in the free frame list
        for(int i=0;i< freeFrameList.length;i++) {
            if(freeFrameList[i] == 'Y' && (i + 127) < memory.length) {      //checks for first Y and whether the frame can fit in memory
                freeFrameList[i] = 'N';
                return i;
            }
        }
        return -1;
    }

    public void loadDataIntoFrame(int frameIndex, String s, Memory m) { //arg memory passed so it can use the memory object cretaed in process control file
        int baseIndex = frameIndex;         //this is the index in physical memory where the frame starts
        int limitIndex = baseIndex + 127;   //this is where the frame ends

        String[] arr = s.split(" ");
        for (int i = 0; i < arr.length; i++) {
            int x = Integer.parseInt(arr[i], 16);
            m.set(baseIndex, (short)x);
            baseIndex++;        //putting vals in physical memory and incrementing the index value starting from base index
        }
    }

    public void set(int index, Short value) {
        memory[index] = value.byteValue();
    }

    public short get(int index) {
       
        return (short) Byte.toUnsignedInt(memory[index]);
       
    }

    public void VirtualSet(int index, Short value) {
        virtualMemory[index] = value.byteValue();
    }

    public short VirtualGet(int index) {
       
        return (short) Byte.toUnsignedInt(virtualMemory[index]);
       
    }

    public void printMem() {
        for (int i =0; i < 100;i++) {
            System.out.println(Byte.toUnsignedInt(memory[i]));
        }
    }
    /*
     
    public void printMemory() {
        System.out.println("Decimal Byte (UnSigned) Values:");
        for (int i = 0; i < codeCounter; i++) {
            System.out.println(Byte.toUnsignedInt(MemoryArray[i]));
        }
        System.out.println("Signed Values:");
        for (int i = 0; i < codeCounter; i++) {
            System.out.println(MemoryArray[i]);
        }
        // System.out.println("\nHexa String Values:");
        // for (int i = 0; i < 13; i++) {
        // System.out.println(SPR.decimalToHex(Byte.toUnsignedInt(MemoryArray[i])));
        // }
        // System.out.println("");
        }
     */
    
}
