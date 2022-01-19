package krythos.util.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SimpleProgressBar extends JDialog {
	private JProgressBar jb;


	/**
	 * Creates a {@link SimpleProgressBar} with the specified minimum and
	 * maximum values, and a default Horizontal orientation.
	 * 
	 * @param min Minimum value.
	 * @param max Maximum value.
	 */
	public SimpleProgressBar(Frame parent, int min, int max) {
		this(parent, JProgressBar.HORIZONTAL, min, max);
	}


	/**
	 * Creates a {@link SimpleProgressBar} with the specified minimum,
	 * maximum, and orientation values. Orientation value determined from
	 * {@link SwingConstants}.
	 * 
	 * @param orient Orientation of the JProgressBar
	 * @param min    Minimum Value.
	 * @param max    Maximum Value.
	 */
	public SimpleProgressBar(Frame parent, int orient, int min, int max) {
		super(parent, "", true);
		this.setModalityType(ModalityType.MODELESS);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
		panel.setLayout(new BorderLayout());

		jb = new JProgressBar(orient, min, max);
		jb.setValue(0);
		jb.setStringPainted(true);
		panel.add(jb, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		this.setSize(500, 150);
		SwingMisc.centerWindow(this);
	}


	public void increment() {
		increment(1);
	}


	public void increment(int amount) {
		this.setValue(jb.getValue() + amount);
	}


	public void setValue(int value) {
		this.bar().setValue(value);
	}


	public JProgressBar bar() {
		return jb;
	}


	public String statusString() {
		String ret = "";
		ret += "Minimum: " + bar().getMinimum() + "\n";
		ret += "Maximum: " + bar().getMaximum() + "\n";
		ret += "Value: " + bar().getValue() + "\n";
		ret += "String: " + bar().getString();
		return ret;
	}


	public static void main(String[] args) throws InterruptedException {
		SimpleProgressBar m = new SimpleProgressBar(null, 0, 100);
		m.setTitle("Example Progress Bar");
		m.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		m.setVisible(true);
		while (m.bar().getValue() < m.bar().getMaximum()) {
			m.increment();
			Thread.sleep(1000 / m.bar().getMaximum());
		}
	}
}
