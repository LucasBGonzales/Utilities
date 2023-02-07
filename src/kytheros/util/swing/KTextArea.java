package kytheros.util.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * JTextArea with extra custom functionality.<br>
 * Custom functionality includes:<br>
 * • Placeholder Text<br>
 * • Digit-Only Formatting<br>
 * 
 * @author kytheros
 *
 */
@SuppressWarnings("serial")
public class KTextArea extends JTextArea {
	private boolean f_digitOnly;
	private String placeholder;


	public static void main(final String[] args) {
		// Example
		final KTextArea tf = new KTextArea("");
		tf.setColumns(20);
		tf.setPlaceholder("");
		final Font f = tf.getFont();
		tf.setFont(new Font(f.getName(), f.getStyle(), 30));
		JOptionPane.showMessageDialog(null, tf);
	}


	public KTextArea() {
		this(null, null, 0, 0);
	}


	public KTextArea(final Document pDoc) {
		super(pDoc);
	}


	public KTextArea(final Document pDoc, final String pText, final int pRows, final int pColumns) {
		super(pDoc, pText, pRows, pColumns);
		setDisabledTextColor(Color.GRAY); // Preffered to the lighter gray of default disabled text color.
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (f_digitOnly) {
					char c = e.getKeyChar();
					if (!Character.isDigit(c))
						e.consume();
				}
			}
		});
	}


	public KTextArea(final int pRows, final int pColumns) {
		super(pRows, pColumns);
	}


	public KTextArea(final String pText) {
		super(pText);
	}


	public KTextArea(final String pText, final int pRows, final int pColumns) {
		super(pText, pRows, pColumns);
	}


	/**
	 * Returns the Placeholder Text which will display when the KTextArea is empty.
	 * 
	 * @return
	 */
	public String getPlaceholder() {
		return placeholder;
	}


	/**
	 * If set to true, any keystrokes that are not a number will be consumed.
	 * 
	 * @param digitOnly
	 */
	public void setDigitOnly(boolean digitOnly) {
		this.f_digitOnly = digitOnly;
	}


	/**
	 * Set Placeholder Text to display when the KTextArea is empty.
	 * 
	 * @param s
	 */
	public void setPlaceholder(final String s) {
		placeholder = s;
	}


	@Override
	protected void paintComponent(final Graphics pG) {
		super.paintComponent(pG);

		// If Placeholder is not needed or not set, then return.
		if (placeholder == null || placeholder.length() == 0 || getText().length() > 0)
			return;
		// Else draw the Placeholder text.
		final Graphics2D g = (Graphics2D) pG;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getDisabledTextColor());
		g.drawString(placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top);
	}
}
