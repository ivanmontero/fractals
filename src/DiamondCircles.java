import java.awt.*;

public class DiamondCircles extends Canvas{

    //private double decay;
    private int maxRadius;


    @Override
    public void init() {
        window.setTitle("Horizontal Half Circles");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int)(screenSize.getWidth() * .8), (int)(screenSize.getHeight() * .8));

        window.addSlider("Radius", getWidth()/4, getWidth() * 25, getWidth()/4);
        //window.addSlider("Decay", 40, 90, 70);
    }

    @Override
    public void draw(Graphics2D g) {
        maxRadius = window.getValue("Radius");
        //decay = (double) window.getValue("Decay") * 0.01;
        g.translate(getWidth()/2, getHeight()/2);
        drawCircle(g, 0, 0, maxRadius);
    }

    public void drawCircle(Graphics g, int x, int y, int radius) {

        g.drawArc(-radius + x, -radius + y, radius*2, radius*2, 0, 360);
        if(radius > 5) {
            drawCircle(g, x + radius, y, radius/2);
            drawCircle(g, x - radius, y, radius/2);
            drawCircle(g, x, y + radius, radius/2);
            drawCircle(g, x, y - radius, radius/2);
        }
    }


    public static void main(String[] args) {
        new DiamondCircles();
    }
}
