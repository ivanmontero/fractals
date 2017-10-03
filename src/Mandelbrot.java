import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Mandelbrot extends Canvas {
    //private ComputeMandelbrotTask computeTask;
    private BufferedImage image;
    //private Timer renderTimer;
    private final double DEFAULT_X_MIN = -2.5;
    private final double DEFAULT_X_MAX = 1.0;
    private final double DEFAULT_Y_MIN = -1.0;
    private final double DEFAULT_Y_MAX = 1.0;
    private final double ZOOM_SCALE = 1.0;
    private final boolean BLACK_AND_WHITE = true;
    private double xMin = DEFAULT_X_MIN;
    private double xMax = DEFAULT_X_MAX;
    private double yMin = DEFAULT_Y_MIN;
    private double yMax = DEFAULT_Y_MAX;
    private int maxIterations = 100;
    private int zoom = 0;
    private int xPan = 0;
    private int yPan = 0;
    private int xLast = 0;
    private int yLast = 0;

    //Mandelbrot set: Z(n-1)=Zn^2 + C

    @Override
    public void init() {
        /*
        renderTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        */
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoom += e.getUnitsToScroll();
                System.out.println("zoom: " + zoom);
                repaint();
                //computeTask.start();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xPan -= e.getX() - xLast;
                yPan -= e.getY() - yLast;
                xLast = e.getX();
                yLast = e.getY();
                System.out.println("xPan: " + xPan + "   yPan: " + yPan);
                repaint();
                //computeTask.start();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == 2) {
                    xMin = DEFAULT_X_MIN;
                    xMax = DEFAULT_X_MAX;
                    yMin = DEFAULT_Y_MIN;
                    yMax = DEFAULT_Y_MAX;
                    repaint();
                    //computeTask.start();
                } else {
                    xLast = e.getX();
                    yLast = e.getY();
                }
            }
        });
        window.addSlider("Iterations",1, 5000, 100);
        /*
        computeTask = new ComputeMandelbrotTask();
        computeTask.start();
        */
    }

    @Override
    public void draw(Graphics2D g) {

        resolveWindowSpace();
        render();

        //if(image != null)
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public void resolveWindowSpace() {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Point2D.Double p1 = pixelToCoordinate(0, 0);
        Point2D.Double p2x = pixelToCoordinate(1, 0);
        Point2D.Double p2y = pixelToCoordinate(0, 1);
        double deltaX = p1.distance(p2x);
        double deltaY = p1.distance(p2y);
        xMin += deltaX * xPan;
        xMax += deltaX * xPan;
        yMin += deltaY * yPan;
        yMax += deltaY * yPan;
        xMin += deltaX * zoom * ZOOM_SCALE;
        xMax -= deltaX * zoom * ZOOM_SCALE;
        yMin += deltaY * zoom * ZOOM_SCALE;
        yMax -= deltaY * zoom * ZOOM_SCALE;
        zoom = 0;
        xPan = 0;
        yPan = 0;
        window.setTitle(
                String.format("X(%.5f,%.5f) Y(%.5fi,%.5fi)", xMin, xMax, yMin, yMax)
        );

    }

    public void render() {
        maxIterations = window.getValue("Iterations");
        // Mandelbrot algorithm
        for(int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
            for(int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
                Point2D.Double p0 = pixelToCoordinate(xPixel, yPixel);
                double x = 0.0;
                double y = 0.0;
                int iteration = 0;
                while(x*x + y*y < 2*2 && iteration < maxIterations) {
                    double xTemp = x*x - y*y + p0.x;
                    y = 2*x*y + p0.y;
                    x = xTemp;
                    iteration++;
                }
                if(BLACK_AND_WHITE) {
                    int color = (int) (((double) iteration / maxIterations) * 255.0);
                    image.setRGB(xPixel, yPixel, new Color(color, color, color).getRGB());
                } else {
                    image.setRGB(xPixel, yPixel, Color.getHSBColor((float) iteration/maxIterations, 1f, .8f).getRGB());
                }
            }
        }
    }

    public Point2D.Double pixelToCoordinate(int x, int y) {
        return new Point2D.Double(
                (((double)x/image.getWidth())*(xMax-xMin))+xMin,
                (((double)y/image.getHeight())*(yMax-yMin))+yMin
        );
    }

    public static void main(String[] args) {
        new Mandelbrot();
    }
    /*

    private class ComputeMandelbrotTask extends SwingWorker<Void, int[]> {
        // int[] format: 0=x 1=y 2=iterations

        public void start() {
            if(!isDone()) {
                cancel(false);
            }
            try {
                get();
            } catch (Exception ignored){}
            execute();
            System.out.println("Test");
        }

        @Override
        protected Void doInBackground() throws Exception {
            renderTimer.start();
            resolveWindowSpace();
            maxIterations = window.getValue("Iterations");
            // Mandelbrot algorithm
            for(int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                for(int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
                    if(isCancelled()) {
                        return null;
                    }
                    Point2D.Double p0 = pixelToCoordinate(xPixel, yPixel);
                    double x = 0.0;
                    double y = 0.0;
                    int iteration = 0;
                    while(x*x + y*y < 2*2 && iteration < maxIterations) {
                        double xTemp = x*x - y*y + p0.x;
                        y = 2*x*y + p0.y;
                        x = xTemp;
                        iteration++;
                    }
                    publish(new int[]{xPixel, yPixel, iteration});
                }
            }
            return null;
        }

        @Override
        protected void process(List<int[]> chunks) {
            for(int[] data : chunks) {
                if (BLACK_AND_WHITE) {
                    int color = (int) (((double) data[2] / maxIterations) * 255.0);
                    image.setRGB(data[0], data[1], new Color(color, color, color).getRGB());
                } else {
                    image.setRGB(data[0], data[1], Color.getHSBColor((float) data[2] / maxIterations, 1f, .8f).getRGB());
                }
            }
        }

        @Override
        protected void done() {
            renderTimer.stop();
        }

    }
    */

}
