import javax.swing.*;
import java.awt.*;

abstract public class Canvas extends JPanel {
    protected Window window;
    private boolean initialized;
    private boolean antialiased;

    public Canvas() {
        window = new Window(this);
        init();
        initialized = true;
        antialiased = true;
    }

    abstract public void init();

    abstract public void draw(Graphics2D g);

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        if(antialiased)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(initialized)
            draw(g);
    }

    public void setAntialiased(boolean aa) {
        this.antialiased = aa;
    }
}
