public class Memory {
    short[] memory ;

    public Memory() {
        memory = new short[65536];
    }

    public void set(int index, short value) {
        memory[index] = value;
    }

    public short get(int index) {
        return memory[index];
    }
}
