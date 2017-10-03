import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Window extends JFrame {
    private JPanel canvas;
    private JPanel dataPanel;
    private JPanel labelPanel;
    private JPanel sliderPanel;
    private Map<String, JSlider> sliders;
    private Map<String, JLabel> labels;
    private GridBagConstraints sliderContraint;
    private ChangeListener changeListener;

    public Window(JPanel canvas) {
        this(canvas, "Fractal");
    }

    public Window(JPanel canvas, String name) {
        super(name);
        this.canvas = canvas;
        sliders = new HashMap<String, JSlider>();
        labels = new HashMap<String, JLabel>();
        sliderContraint = new GridBagConstraints();
        sliderContraint.fill = GridBagConstraints.HORIZONTAL;
        changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateLabels();
                repaint();
            }
        };
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.add(canvas, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void repaint() {
        canvas.repaint();
    }

    public void addSlider(String name, int min, int max, int initial) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
        slider.setName(name);
        slider.setPaintLabels(true);
        slider.addChangeListener(changeListener);
        if(dataPanel == null) {
            dataPanel = new JPanel();
            dataPanel.setLayout(new BorderLayout());
            sliderPanel = new JPanel();
            sliderPanel.setLayout(new GridBagLayout());
            labelPanel = new JPanel();
            labelPanel.setLayout(new GridBagLayout());
            dataPanel.add(labelPanel, BorderLayout.WEST);
            dataPanel.add(sliderPanel, BorderLayout.CENTER);
            this.add(dataPanel, BorderLayout.PAGE_END);
        }
        sliderContraint.gridy = sliders.size();
        sliders.put(name, slider);
        sliderContraint.weightx = 1.0;
        sliderPanel.add(slider, sliderContraint);
        sliderContraint.weightx = 0.0;
        JLabel label = new JLabel(name + " [" + initial + "]");
        //label.setFont(new Font(Font.MONOSPACED, Font.PLAIN,11));
        labels.put(name, label);
        labelPanel.add(label, sliderContraint);
        //setSize(frame.getWidth(), this.getHeight() + slider.getHeight());
        //frame.setSize(frame.getWidth(), this.getHeight() + slider.getHeight());
        //frame.revalidate();
        refresh();
    }

    public void updateLabels() {
        for(String name : sliders.keySet()) {
            JSlider slider = sliders.get(name);
            labels.get(name).setText(slider.getName() + " [" + slider.getValue() + "]");
        }
    }

    public void refresh() {
        this.revalidate();
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        refresh();
    }

    public int getValue(String name) {
        //JSlider slider = getSlider(name);
        JSlider slider = sliders.get(name);
        if(slider == null) {
            System.err.println("Value " + name + " does not exist.");
            return -1;
        } else {
            return slider.getValue();
        }
    }
    //TODO: Set antialiasing

}
