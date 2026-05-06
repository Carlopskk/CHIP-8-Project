public class CPU {

    int[] fontSet = {
        0xF0,
        0x90,
        0x90,
        0x90,
        0xF0, // 0
        0x20,
        0x60,
        0x20,
        0x20,
        0x70, // 1
        0xF0,
        0x10,
        0xF0,
        0x80,
        0xF0, // 2
        0xF0,
        0x10,
        0xF0,
        0x10,
        0xF0, // 3
        0x90,
        0x90,
        0xF0,
        0x10,
        0x10, // 4
        0xF0,
        0x80,
        0xF0,
        0x10,
        0xF0, // 5
        0xF0,
        0x80,
        0xF0,
        0x90,
        0xF0, // 6
        0xF0,
        0x10,
        0x20,
        0x40,
        0x40, // 7
        0xF0,
        0x90,
        0xF0,
        0x90,
        0xF0, // 8
        0xF0,
        0x90,
        0xF0,
        0x10,
        0xF0, // 9
        0xF0,
        0x90,
        0xF0,
        0x90,
        0x90, // A
        0xE0,
        0x90,
        0xE0,
        0x90,
        0xE0, // B
        0xF0,
        0x80,
        0x80,
        0x80,
        0xF0, // C
        0xE0,
        0x90,
        0x90,
        0x90,
        0xE0, // D
        0xF0,
        0x80,
        0xF0,
        0x80,
        0xF0, // E
        0xF0,
        0x80,
        0xF0,
        0x80,
        0x80, // F
    };

    Display display;
    Memory memory;
    Decoder decoder;
    Keyboard keyboard;
    int[] v = new int[16];
    int[] stack = new int[16];
    int sp = 0;
    int regI = 0;
    int delayTimer = 0;
    int soundTimer = 0;

    int stepCount = 0;

    public CPU(Memory memory, Display display, Keyboard keyboard) {
        this.memory = memory;
        this.display = display;
        this.decoder = new Decoder();
        this.keyboard = keyboard;
        for (int i = 0; i < fontSet.length; i++) {
            memory.write(i, fontSet[i]);
        }
    }

    int pc = 0x200;

    public void step() {
        int instrucao = memory.readInstruction(pc);
        pc += 2;

        int tipo = (instrucao & 0xF000) >> 12;
        int x = (instrucao & 0x0F00) >> 8;
        int y = (instrucao & 0x00F0) >> 4;
        int valor = (instrucao & 0x00FF);
        int endereco = (instrucao & 0x0FFF);
        int n = (instrucao & 0x000F);

        switch (tipo) {
            case 0x0:
                switch (valor) {
                    case 0x00:
                        break;
                    case 0xE0:
                        display.clear();
                        break;
                    case 0xEE:
                        sp--;
                        pc = stack[sp];
                        break;
                    default:
                        System.out.println(
                            "0x0 desconhecida, valor 0x" +
                                Integer.toHexString(valor)
                        );
                        break;
                }
                break;
            case 0x1:
                pc = endereco;
                break;
            case 0x2:
                stack[sp] = pc;
                sp++;
                pc = endereco;
                break;
            case 0x3:
                if (v[x] == valor) {
                    pc += 2;
                }
                break;
            case 0x4:
                if (v[x] != valor) {
                    pc += 2;
                }
                break;
            case 0x5:
                if (n == 0) {
                    if (v[x] == v[y]) {
                        pc += 2;
                    }
                }
                break;
            case 0x6:
                v[x] = valor;
                break;
            case 0x7:
                v[x] = (v[x] + valor) & 0xFF;
                break;
            case 0x8:
                switch (n) {
                    case 0x0:
                        v[x] = v[y];
                        break;
                    case 0x1:
                        v[x] |= v[y];
                        break;
                    case 0x2:
                        v[x] &= v[y];
                        break;
                    case 0x3:
                        v[x] ^= v[y];
                        break;
                    case 0x4:
                        v[0xF] = 0;
                        if (v[x] + v[y] > 255) {
                            v[0xF] = 1;
                        }
                        v[x] = (v[x] + v[y]) & 0xFF;
                        break;
                    case 0x5:
                        v[0xF] = 0;
                        if (v[x] >= v[y]) {
                            v[0xF] = 1;
                        }
                        v[x] = (v[x] - v[y]) & 0xFF;
                        break;
                    case 0x6:
                        int lsb = v[x] & 0x1;
                        v[x] = (v[x] >> 1) & 0xFF;
                        v[0xF] = lsb;
                        break;
                    case 0x7:
                        v[0xF] = 0;
                        if (v[y] >= v[x]) {
                            v[0xF] = 1;
                        }
                        v[x] = (v[y] - v[x]) & 0xFF;
                        break;
                    case 0xE:
                        int msb = (v[x] & 0x80) >> 7;

                        v[x] = (v[x] << 1) & 0xFF;

                        if (msb != 0) {
                            v[0xF] = 1;
                        } else {
                            v[0xF] = 0;
                        }
                        break;
                }
                break;
            case 0x9:
                if (v[x] != v[y]) {
                    pc += 2;
                }
                break;
            case 0xA:
                regI = endereco;
                break;
            case 0xC:
                v[x] = ((int) (Math.random() * 256) - 0) & valor;
                break;
            case 0xD:
                int posX = v[x];
                int posY = v[y];
                int linhas = n;
                v[0xF] = 0;

                for (int linha = 0; linha < linhas; linha++) {
                    int _byte = memory.read(regI + linha);

                    for (int bit = 0; bit < 8; bit++) {
                        if ((_byte & (0x80 >> bit)) != 0) {
                            int px = (posX + bit) % 64;
                            int py = (posY + linha) % 32;

                            if (display.pixels[px][py]) {
                                v[0xF] = 1;
                            }

                            display.pixels[px][py] ^= true;
                        }
                    }
                }
                display.repaint();
                break;
            case 0xE:
                switch (valor) {
                    case 0x9E:
                        if (keyboard.keys[v[x]]) {
                            pc += 2;
                        }
                        break;
                    case 0xA1:
                        if (!keyboard.keys[v[x]]) {
                            pc += 2;
                        }
                        break;
                }
                break;
            case 0xF:
                switch (valor) {
                    case 0x07:
                        v[x] = delayTimer;
                        break;
                    case 0x0A:
                        boolean press = false;
                        for (int i = 0; i < 16; i++) {
                            if (keyboard.keys[i]) {
                                v[x] = i;
                                press = true;
                                break;
                            }
                        }
                        if (!press) {
                            pc -= 2;
                        }
                        break;
                    case 0x15:
                        delayTimer = v[x];
                        break;
                    case 0x18:
                        soundTimer = v[x];
                        break;
                    case 0x1E:
                        regI += v[x];
                        break;
                    case 0x29:
                        regI = v[x] * 5;
                        break;
                    case 0x33:
                        memory.write(regI + 0, v[x] / 100);
                        memory.write(regI + 1, (v[x] / 10) % 10);
                        memory.write(regI + 2, v[x] % 10);
                        break;
                    case 0x55:
                        for (int i = 0; i <= x; i++) {
                            memory.write(regI + i, v[i]);
                        }
                        break;
                    case 0x65:
                        for (int i = 0; i <= x; i++) {
                            v[i] = memory.read(regI + i);
                        }
                        break;
                }
                break;
            default:
                System.out.println(
                    "Desconhecida: 0x" +
                        Integer.toHexString(instrucao).toUpperCase()
                );
                break;
        }
    }
}
