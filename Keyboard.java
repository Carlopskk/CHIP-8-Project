import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {

    boolean[] keys = new boolean[16];

    @Override
    public void keyPressed(KeyEvent e) {
        int key = mapKey(e.getKeyCode());
        if (key >= 0) {
            keys[key] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = mapKey(e.getKeyCode());
        if (key >= 0) {
            keys[key] = false;
        }
    }

    private int mapKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_1:
                return 0x1;
            case KeyEvent.VK_2:
                return 0x2;
            case KeyEvent.VK_3:
                return 0x3;
            case KeyEvent.VK_4:
                return 0xC;
            case KeyEvent.VK_Q:
                return 0x4;
            case KeyEvent.VK_W:
                return 0x5;
            case KeyEvent.VK_E:
                return 0x6;
            case KeyEvent.VK_R:
                return 0xD;
            case KeyEvent.VK_A:
                return 0x7;
            case KeyEvent.VK_S:
                return 0x8;
            case KeyEvent.VK_D:
                return 0x9;
            case KeyEvent.VK_F:
                return 0xE;
            case KeyEvent.VK_Z:
                return 0xA;
            case KeyEvent.VK_X:
                return 0x0;
            case KeyEvent.VK_C:
                return 0xB;
            case KeyEvent.VK_V:
                return 0xF;
            default:
                return -1;
        }
    }
}
