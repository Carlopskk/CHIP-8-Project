import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Display display = new Display();
        JFrame frame = new JFrame("CHIP-8");
        Memory memory = new Memory();
        Keyboard keyboard = new Keyboard();
        frame.addKeyListener(keyboard);

        CPU cpu = new CPU(memory, display, keyboard);

        frame.add(display);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();

        byte[] rom = Files.readAllBytes(Path.of("8-scrolling.ch8"));
        for (int i = 0; i < rom.length; i++) {
            memory.write(0x200 + i, rom[i] & 0xFF);
        }

        Timer timer = new Timer(16, e -> {
            for (int i = 0; i < 10; i++) {
                cpu.step();
            }
            if (cpu.delayTimer > 0) cpu.delayTimer--;
            if (cpu.soundTimer > 0) cpu.soundTimer--;
        });
        timer.start();
    }
}
