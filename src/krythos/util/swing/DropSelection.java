package krythos.util.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

/**
 * This class will create a drop-down menu of selectable text labels.
 * This version is designed for use with {@code JInternalFrame}s.
 * 
 * @author Lucas Gonzales "Krythos"
 *
 */
public class DropSelection {
	private List<DropListener> m_dropListeners;

	private List<DropLabel> m_lstLabels;
	private JTextComponent m_txtComponent;
	private Window m_parent;
	private Container m_container;
	private JWindow m_window;
	private KeyListener m_keyListener;
	private MouseListener m_mouseListener;
	private FocusListener m_focusListener;
	private Color m_clrBackground;
	private Border m_border;

	private int m_selectedIndex;


	/**
	 * Creates a selection menu for a {@code JTextComponent}. Must define
	 * the frame upon which to draw the {@code DropSelection} and a list
	 * if items to draw.
	 * 
	 * @param txtComponent The {@code JTextComponent} to create the
	 *                     {@code DropSelection} list for.
	 * @param frame        The {@code JInternalFrame} upon which to draw
	 *                     the {@code DropSelection}.
	 * @param list         The {@code String[]} with which to build the
	 *                     {@code DropSelection} items.
	 */
	public DropSelection(JTextComponent txtComponent, Window parent, Container container, String[] list) {
		m_dropListeners = new ArrayList<DropListener>();

		defineListeners();

		m_selectedIndex = -1;
		m_txtComponent = txtComponent;
		m_parent = parent;
		m_container = container;
		m_window = new JWindow(m_parent);
		m_window.setOpacity(.75f);

		setList(list);

		setBackground(Color.white.brighter());
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		adjustLabelPositions();


		m_txtComponent.addKeyListener(m_keyListener);
		m_txtComponent.addMouseListener(m_mouseListener);
		m_txtComponent.addFocusListener(m_focusListener);

		setVisible(m_txtComponent.isFocusOwner());
	}


	/**
	 * Example of working drop selection
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setBounds(500, 300, 200, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JInternalFrame i_f = new JInternalFrame();
		i_f.setVisible(true);

		JTextField txt = new JTextField(20);
		txt.setBounds(0, 0, 100, 20);
		txt.setVisible(true);

		JButton btn = new JButton("hi");
		btn.setBounds(101, 0, 20, 20);
		btn.setVisible(true);

		i_f.setLayout(null);
		i_f.add(txt);
		i_f.add(btn);
		f.add(i_f);
		f.setVisible(true);

		DropSelection ds = new DropSelection(txt, f, i_f, new String[] { "hi", "hello", "hurricane", "seven", "sever" });

		ds.setVisible(true);
	}


	/**
	 * Adjusts the label positions oriented to the txtComponent.
	 */
	private void adjustLabelPositions() {
		int componentHeight = 15;
		for (int i = 0; i < m_lstLabels.size(); i++) {
			int x, y, w, h;
			Rectangle r;
			if(m_container == null)
				r = m_container.getBounds();
			else
				r = new Rectangle(0,0,0,0);
			x = m_txtComponent.getX() + r.x;
			w = m_txtComponent.getWidth();
			y = m_txtComponent.getY() + m_txtComponent.getHeight() + (i * componentHeight) + r.y;
			h = componentHeight;

			m_lstLabels.get(i).setBounds(x, y, w, h);
		}
	}


	/**
	 * Defines the various listeners used by DropSelection
	 */
	private void defineListeners() {
		m_keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}


			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getSource().equals(m_txtComponent)) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (m_selectedIndex > 0)
							setSelectedIndex(m_selectedIndex - 1);
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (m_selectedIndex < m_lstLabels.size() - 1)
							setSelectedIndex(m_selectedIndex + 1);
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						itemSelected(m_lstLabels.get(m_selectedIndex));
					} else if (e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU)
						setVisible(!isVisible());
				}
			}


			@Override
			public void keyTyped(KeyEvent e) {
			}
		};

		m_mouseListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}


			@Override
			public void mouseEntered(MouseEvent e) {
				if (((JComponent) e.getSource()) instanceof DropLabel) {
					setSelectedIndex(Integer.valueOf(((DropLabel) e.getSource()).getName().substring(3)));
				}
			}


			@Override
			public void mouseExited(MouseEvent e) {
			}


			@Override
			public void mousePressed(MouseEvent e) {
			}


			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		m_focusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				setVisible(true);
			}


			@Override
			public void focusLost(FocusEvent e) {
				setVisible(false);
			}
		};
	}


	/**
	 * Send a DropListener event.
	 * 
	 * @param e
	 */
	private void sendEvent(DropEvent e) {
		for (DropListener l : m_dropListeners)
			l.itemSelected(e);
	}


	/**
	 * Sets the background colors to the stored color.
	 */
	private void setBackgrounds() {
		for (DropLabel lbl : m_lstLabels)
			lbl.setBackground(m_clrBackground);
	}


	/**
	 * Sets the borders to the stored border.
	 */
	private void setBorders() {
		for (DropLabel lbl : m_lstLabels)
			lbl.setBorderTo(m_border);
	}


	/**
	 * Updates the list to represent the currently selected index.
	 */
	private void updateSelected() {
		for (int i = 0; i < m_lstLabels.size(); i++)
			if (i == m_selectedIndex)
				m_lstLabels.get(i).setSelected(true);
			else
				m_lstLabels.get(i).setSelected(false);
	}


	/**
	 * Adds a DropListener to the DropListeners List.
	 * 
	 * @param listener
	 */
	public void addListener(DropListener listener) {
		m_dropListeners.add(listener);
	}


	/**
	 * Returns whether the list is currently visible. Will also return
	 * false if the list has a size less than 0.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return m_lstLabels.size() > 0 ? m_lstLabels.get(0).isVisible() : false;
	}


	/**
	 * Sets the background the labels should use, then updates the labels.
	 * 
	 * @param c
	 */
	public void setBackground(Color c) {
		m_clrBackground = c;
		setBackgrounds();
	}


	/**
	 * Sets the border the labels should use, then updates the labels.
	 * 
	 * @param b
	 */
	public void setBorder(Border b) {
		m_border = b;
		setBorders();
	}


	/**
	 * Creates the List of DropLabels
	 * 
	 * @param list
	 */
	public void setList(String[] list) {
		m_lstLabels = new ArrayList<DropLabel>(list.length);
		for (int i = 0; i < list.length; i++) {
			DropLabel lbl = new DropLabel();

			lbl.setText(list[i]);

			// Listener and identifier
			lbl.setName("lbl" + i);
			lbl.addKeyListener(m_keyListener);
			lbl.addMouseListener(m_mouseListener);

			m_parent.add(lbl, null);

			m_lstLabels.add(i, lbl);
		}

		setBackgrounds();
		setBorders();
	}


	/**
	 * Sets the selected index, updates the UI to reflect this, and sends
	 * a DropEvent.
	 * 
	 * @param index
	 */
	public void setSelectedIndex(int index) {
		m_selectedIndex = index;
		updateSelected();
		sendEvent(new DropEvent(m_lstLabels.get(m_selectedIndex).getText(), m_selectedIndex));
	}


	/**
	 * Sets the labels to {@code visible} according to
	 * {@code DropLabel.setVisible(boolean)). Also sets the selected index back to zero.
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		setSelectedIndex(0);
		for (DropLabel lbl : m_lstLabels)
			lbl.setVisible(visible);
	}


	/**
	 * When an item is selected, the text of the label goes into the
	 * JTextComponent and the list is hidden.
	 * 
	 * @param label
	 */
	protected void itemSelected(DropLabel label) {
		m_txtComponent.setText(label.getText());
		setVisible(false);
	}


	/**
	 * DropLabels are JLabels but with additional functionality to make
	 * them operate easily as a navigable list of selectable items.
	 * 
	 * @author Lucas Gonzales "Krythos"
	 *
	 */
	private class DropLabel extends JLabel {
		private static final long serialVersionUID = 6811796537120381249L;
		private Border m_brdSelected;


		/**
		 * Sets default border and initializes to {@code setSelected(false)}.
		 */
		public DropLabel() {
			m_brdSelected = BorderFactory.createLineBorder(Color.RED, 3);
			setSelected(false);
		}


		/**
		 * If the {@code Color} is null, the label is made transparent. Else,
		 * it is made opaque and {@code super.setBackground(Color)} is called.
		 */
		@Override
		public void setBackground(Color c) {
			if (c == null)
				this.setOpaque(false);
			else
				this.setOpaque(true);

			super.setBackground(c);
		}


		/**
		 * Sets the selection border.
		 * 
		 * @param b
		 */
		public void setBorderTo(Border b) {
			m_brdSelected = b;
		}


		/**
		 * Sets the selection state of this DropLabel
		 * 
		 * @param selected
		 */

		public void setSelected(boolean selected) {
			if (selected)
				setBorder(m_brdSelected);
			else
				setBorder(null);
		}
	}


	/**
	 * This event is so that implementing code can easily see when an item
	 * has been selected, where it was in the list, and what the text of
	 * the DropLabel was.
	 * 
	 * @author Lucas Gonzales "Krythos"
	 *
	 */
	public class DropEvent {
		private String m_str;
		private int m_index;


		/**
		 * Takes the {@code String} and index of the selected label.
		 * 
		 * @param s
		 * @param i
		 */
		public DropEvent(String s, int i) {
			m_str = s;
			m_index = i;
		}


		/**
		 * Returns the index of the selected label
		 * 
		 * @return
		 */
		public int getIndex() {
			return m_index;
		}


		/**
		 * Retursn the {@code String} of the selected label.
		 * 
		 * @return
		 */
		public String getString() {
			return m_str;
		}
	}


	/**
	 * Interface to distribute DropEvents.
	 * 
	 * @author Lucas Gonzales "Krythos"
	 *
	 */
	public interface DropListener {
		public void itemSelected(DropEvent e);
	}
}
