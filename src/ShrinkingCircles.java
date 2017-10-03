import java.awt.*;

public class ShrinkingCircles extends Canvas {

    private double decay;
    private int maxRadius;


    @Override
    public void init() {
        window.setTitle("Shrinking Circles");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int)(screenSize.getWidth() * .8), (int)(screenSize.getHeight() * .8));

        window.addSlider("Radius", getWidth()/16, getWidth(), getWidth()/2);
        window.addSlider("Decay", 40, 90, 70);


    }

    @Override
    public void draw(Graphics2D g) {
        maxRadius = window.getValue("Radius");
        decay = (double) window.getValue("Decay") * 0.01;
        g.translate(getWidth()/2, getHeight()/2);
        drawCircle(g, maxRadius);
    }

    public void drawCircle(Graphics g, int radius) {
        g.drawArc(-radius, -radius, radius*2, radius*2, 0, 360);
        if(radius > 0) {
            drawCircle(g, (int) (radius * decay));
        }
    }


    public static void main(String[] args) {
        new ShrinkingCircles();
    }
}
