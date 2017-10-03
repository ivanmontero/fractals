import java.awt.*;
import java.awt.geom.AffineTransform;

public class FractalTree extends Canvas {
    private final double MAX_DECAY = 0.8;
    private final int MIN_LENGTH = 2;
    private double maxWidth = 12;
    private double maxTrunkWidth = 10.0;
    private double angle;
    private int maxLength;
    private double decay;

    @Override
    public void init() {
        //setDoubleBuffered(false);
        // Window settings
        window.setTitle("Fractal Tree");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int)(screenSize.getWidth() * .8), (int)(screenSize.getHeight() * .8));
        // Create sliders
        window.addSlider("Width", 1, 20, 10);
        window.addSlider("Angle", 0, 90, 45/3);
        window.addSlider("Length", 0, getHeight()/2, getHeight()/4);
        window.addSlider("Decay", 40, (int) (MAX_DECAY * 100.0), 70);
    }

    @Override
    public void draw(Graphics2D g) {
        // Clear screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Update sliders
        /*
        JSlider sLength = window.getSlider("Length");  // Buggy
        if(sLength.getMaximum() != getHeight()/2)
            sLength.setMaximum(getHeight()/2);
        */
        // Update values
        maxWidth = window.getValue("Width");
        angle = (double) window.getValue("Angle") * (Math.PI / 180.0);
        decay = (double) window.getValue("Decay") * 0.01;
        maxLength = window.getValue("Length");
        maxTrunkWidth = maxWidth * (decay / MAX_DECAY);
        // Draw tree
        g.setColor(Color.BLACK);
        // Sets origin at bottom middle
        g.translate(getWidth()/2, getHeight());
        // Draw branch
        drawBranch(g, maxLength);
    }

    public void drawBranch(Graphics2D g, int length) {
        // Draw branch
        g.setStroke(new BasicStroke((float) (maxTrunkWidth * ((double) length / maxLength))));
        g.drawLine(0, 0, 0, -length);
        // Translate to end of branch
        g.translate(0, -length);
        //angle = (Math.PI / 2.0) * Math.random();
        // "base case"
        if (length >= MIN_LENGTH) {
            // "Push" -> preserve current transform
            AffineTransform trans = new AffineTransform(g.getTransform());
            // Draw right branch (Recursive)
            g.rotate(angle);
            drawBranch(g, (int) (length * decay));
            // "Pop" -> restore previous transform
            g.setTransform(trans);
            // Draw left branch (Recursive)
            g.rotate(-angle);
            drawBranch(g, (int) (length * decay));
        }
    }

    public static void main(String[] args) {
        new FractalTree();
    }
}
