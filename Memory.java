public class Memory {

    int[] data = new int[4096];

    public void write(int address, int value) {
        data[address] = value;
    }

    public int read(int address) {
        return data[address];
    }

    public void dump(int star, int end) {
        for (int i = star; i <= end; i++) {}
    }

    public int readInstruction(int address) {
        int byte1 = data[address];
        int byte2 = data[address + 1];
        return ((byte1 << 8) | byte2);
    }
}
