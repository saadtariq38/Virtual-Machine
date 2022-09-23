public class RegisterStorage {
    short[] registers;
    short[] specialRegisters;

    
    public RegisterStorage() {
       registers = new short[16];
       specialRegisters = new short[16];
    }

    public short getRegister(int index) {
       return registers[index];
    }

    public short getSpecialRegister(int index) {
        return specialRegisters[index];
    }

    public void setSpecialRegister(int index, short value) {
        specialRegisters[index] = value;
    }

    public void setRegister(int index, short value) {
        registers[index] = value;
    }


    public void toStringmeowww () {
        for(int i =0; i < 16;i++) {
            System.out.println("General Purpose Regsiter " + i + ": " + registers[i]);
        }

        System.out.println();

        for(int i =0; i < 16;i++) {
            System.out.println("Special Purpose Regsiter " + i + ": " + specialRegisters[i]);
        }

    }

}
