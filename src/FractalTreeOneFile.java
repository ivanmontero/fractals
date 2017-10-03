import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

public class FractalTreeOneFile extends JPanel {
    private final double MAX_DECAY = 0.8;
    private final int MIN_LENGTH = 2;
    private JFrame window;
    private JPanel dataPanel, labelPanel, sliderPanel;
    private Map<String, JSlider> sliders;
    private Map<String, JLabel> labels;
    private GridBagConstraints sliderContraint;
    private ChangeListener changeListener;
    private double angle, decay, maxWidth = 12, maxTrunkWidth = 10.0;
    private int maxLength;
    private boolean initialized;

    public FractalTreeOneFile() {
        window = new JFrame();
        sliders = new HashMap<String, JSlider>();
        labels = new HashMap<String, JLabel>();
        sliderContraint = new GridBagConstraints();
        sliderContraint.fill = GridBagConstraints.HORIZONTAL;
        changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(JSlider s : sliders.values()) {
                    labels.get(s.getName()).setText(s.getName() + " [" + s.getValue() + "]");
                }
                repaint();
            }
        };
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int)(screenSize.getWidth() * .8), (int)(screenSize.getHeight() * .8));
        this.setSize((int)(screenSize.getWidth() * .8), (int)(screenSize.getHeight() * .8));
        window.add(this, BorderLayout.CENTER);
        window.setTitle("Fractal Tree");
        dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout());
        sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridBagLayout());
        labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());
        dataPanel.add(labelPanel, BorderLayout.WEST);
        dataPanel.add(sliderPanel, BorderLayout.CENTER);
        window.add(dataPanel, BorderLayout.PAGE_END);
        addSlider("Width", 1, 20, 10);
        addSlider("Angle", 0, 90, 45/3);
        addSlider("Length", 0, getHeight()/2, getHeight()/4);
        addSlider("Decay", 40, (int) (MAX_DECAY * 100.0), 70);
        window.setVisible(true);
        window.revalidate();
        initialized = true;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(initialized) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            maxWidth = sliders.get("Width").getValue();
            angle = (double) sliders.get("Angle").getValue() * (Math.PI / 180.0);
            decay = (double) sliders.get("Decay").getValue() * 0.01;
            maxLength = sliders.get("Length").getValue();
            maxTrunkWidth = maxWidth * (decay / MAX_DECAY);
            g.setColor(Color.BLACK);
            g.translate(getWidth() / 2, getHeight());
            drawBranch(g, maxLength);
        }
    }

    public void drawBranch(Graphics2D g, int length) {
        g.setStroke(new BasicStroke((float) (maxTrunkWidth * ((double) length / maxLength))));
        g.drawLine(0, 0, 0, -length);
        g.translate(0, -length);
        if (length >= MIN_LENGTH) {
            AffineTransform trans = new AffineTransform(g.getTransform());
            g.rotate(angle);
            drawBranch(g, (int) (length * decay));
            g.setTransform(trans);
            g.rotate(-angle);
            drawBranch(g, (int) (length * decay));
        }
    }

    public void addSlider(String name, int min, int max, int initial) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
        slider.setName(name);
        slider.addChangeListener(changeListener);
        sliderContraint.gridy = sliders.size();
        sliders.put(name, slider);
        sliderContraint.weightx = 1.0;
        sliderPanel.add(slider, sliderContraint);
        sliderContraint.weightx = 0.0;
        JLabel label = new JLabel(name + " [" + initial + "]");
        labels.put(name, label);
        labelPanel.add(label, sliderContraint);
    }

    public static void main(String[] args) { new FractalTreeOneFile(); }
}
