public class Memory {
    byte[] memory;

    public Memory() {
        memory = new byte[65536];
    }

    public void set(int index, Short value) {
        memory[index] = value.byteValue();
    }

    public short get(int index) {
       
        return (short) Byte.toUnsignedInt(memory[index]);
       
    }
}
