import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Display extends JPanel {

    private static final int SCALE = 10;
    boolean[][] pixels = new boolean[64][32];
    private BufferedImage buffer = new BufferedImage(
        64 * SCALE,
        32 * SCALE,
        BufferedImage.TYPE_INT_RGB
    );

    public Display() {
        setPreferredSize(new Dimension(64 * SCALE, 32 * SCALE));
        setBackground(Color.BLACK);
        setFocusable(false);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D bg = buffer.createGraphics();
        bg.setColor(Color.BLACK);
        bg.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        bg.setColor(Color.WHITE);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 64; x++) {
                if (pixels[x][y]) {
                    bg.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                }
            }
        }
        bg.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    public void clear() {
        pixels = new boolean[64][32];
        repaint();
    }
}
