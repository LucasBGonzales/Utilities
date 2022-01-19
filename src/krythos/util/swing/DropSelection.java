package krythos.util.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import krythos.util.abstract_interfaces.AbsKeyListener;
import krythos.util.abstract_interfaces.AbsMouseListener;

/**
 * This class will create a drop-down menu of selectable text labels. This
 * version is designed for use with {@code JInternalFrame}s.
 * 
 * @author Lucas Gonzales "Krythos"
 *
 */
public class DropSelection {
	/**
	 * This event is so that implementing code can easily see when an item has been
	 * selected, where it was in the list, and what the text of the DropLabel was.
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
		 * Returns the {@code String} of the selected label.
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

	/**
	 * DropLabels are JLabels but with additional functionality to make them operate
	 * easily as a navigable list of selectable items.
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
		 * If the {@code Color} is null, the label is made transparent. Else, it is made
		 * opaque and {@code super.setBackground(Color)} is called.
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

		DropSelection ds = new DropSelection(txt, f, new String[] { "hi", "hello", "hurricane", "seven", "sever" });

		ds.setVisible(true);
	}

	private List<DropListener> m_dropListeners;
	private List<DropLabel> m_lstLabels;
	private String[] m_dictionary;
	private List<String> m_displayList;
	private JTextComponent m_txtComponent;
	private Container m_parent;
	private JWindow m_window;
	private KeyListener m_keyListener;
	private MouseListener m_mouseListener;

	private FocusListener m_focusListener;
	private ComponentAdapter m_componentAdapter;
	private int m_selectedIndex;
	private int m_listLimit;


	// Window Variables
	private Color m_clrBackground;
	private Border m_border;
	private boolean m_visible;
	private float m_opacity;


	/**
	 * Creates a selection menu for a {@code JTextComponent}. Must define the
	 * {@code Container} upon which to draw the {@code DropSelection}.
	 * 
	 * @param txtComponent The {@code JTextComponent} to create the
	 *                     {@code DropSelection} list for.
	 * @param parent       The {@code Container} upon which to draw the
	 *                     {@code DropSelection}.
	 */
	public DropSelection(JTextComponent txtComponent, Container parent) {
		this(txtComponent, parent, new String[0]);
	}


	/**
	 * Creates a selection menu for a {@code JTextComponent}. Must define the
	 * {@code Container} upon which to draw the {@code DropSelection} and a list if
	 * items to draw.
	 * 
	 * @param txtComponent The {@code JTextComponent} to create the
	 *                     {@code DropSelection} list for.
	 * @param parent       The {@code Container} upon which to draw the
	 *                     {@code DropSelection}.
	 * @param list         The {@code String[]} with which to build the
	 *                     {@code DropSelection} items.
	 */
	public DropSelection(JTextComponent txtComponent, Container parent, String[] list) {
		m_dropListeners = new ArrayList<DropListener>();
		defineListeners();

		m_selectedIndex = -1;
		m_txtComponent = txtComponent;
		m_parent = parent;
		m_parent.addComponentListener(m_componentAdapter);

		m_window = new JWindow();
		m_opacity = 1f;

		m_listLimit = 10;

		setDictionary(list);

		setBackground(Color.white.brighter());
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));


		m_txtComponent.addKeyListener(m_keyListener);
		m_txtComponent.addMouseListener(m_mouseListener);
		m_txtComponent.addFocusListener(m_focusListener);

		setVisible(false);
	}


	/**
	 * Adds a DropListener to the DropListeners List.
	 * 
	 * @param listener
	 */
	public void addDropListener(DropListener listener) {
		m_dropListeners.add(listener);
	}


	/**
	 * Returns whether the list is currently visible. Will also return false if the
	 * list has a size less than 0.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return m_lstLabels.size() > 0 ? m_lstLabels.get(0).isVisible() && m_visible : false;
	}


	/**
	 * Sets the background the labels should use, then updates the labels.
	 * 
	 * @param c
	 */
	public void setBackground(Color c) {
		m_clrBackground = c;
		updateUI();
	}


	/**
	 * Sets the border the labels should use, then updates the labels.
	 * 
	 * @param b
	 */
	public void setBorder(Border b) {
		m_border = b;
		updateUI();
	}


	/**
	 * Sets the dictionary of {@code Strings} to select from for the drop selection
	 * list.
	 * 
	 * @param list
	 */
	public void setDictionary(String[] list) {
		m_dictionary = list;
		updateUI();
	}


	/**
	 * Sets the limit for the number of suggested items to show.
	 * 
	 * @param limit Limit for the number of suggested items to show.
	 * @throws IllegalArgumentException List Limit must be greater than 0.
	 */
	public void setListLimit(int limit) throws IllegalArgumentException {
		if (limit <= 0)
			throw new IllegalArgumentException("List Limit must be greater than 0");
		m_listLimit = limit;
	}


	/**
	 * Sets the window opacity for the drop selection list.
	 * 
	 * @param f Opacity to set.
	 * @throws IllegalArgumentException Opacity must be between 0f and 1f.
	 */
	public void setOpacity(float f) throws IllegalArgumentException {
		if (f < 0f || f > 1f)
			throw new IllegalArgumentException("Opacity must be between 0f and 1f.");
		m_opacity = f;
		updateUI();
	}


	/**
	 * Sets the labels to {@code visible} according to
	 * {@code DropLabel.setVisible(boolean)). Also sets the selected index back to zero.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		setSelectedIndex(0);
		m_visible = visible;
		updateUI();
	}


	/**
	 * Determines the list of {@code Strings} to be displayed. Limited by
	 * m_listLimit.
	 */
	private void calcDisplayList() {
		m_displayList = new ArrayList<String>(0);
		for (int i = 0; i < m_dictionary.length && i < m_listLimit; i++) {
			String word = m_dictionary[i];
			if (word.contains(m_txtComponent.getText()))
				m_displayList.add(word);
		}
	}


	/**
	 * Defines the various listeners used by DropSelection
	 */
	private void defineListeners() {
		m_keyListener = new AbsKeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getSource().equals(m_txtComponent)) {
						if (m_lstLabels != null && e.getKeyCode() == KeyEvent.VK_UP) {
							if (m_selectedIndex > 0)
								setSelectedIndex(m_selectedIndex - 1);

						} else if (m_lstLabels != null && e.getKeyCode() == KeyEvent.VK_DOWN) {
							if (m_selectedIndex < m_lstLabels.size() - 1)
								setSelectedIndex(m_selectedIndex + 1);

						} else if (m_lstLabels != null && e.getKeyCode() == KeyEvent.VK_ENTER) {
							if (m_lstLabels.size() > 0 && isVisible())
								itemSelected(m_lstLabels.get(m_selectedIndex));

						} else if (m_lstLabels != null && e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU || e.getKeyCode() == KeyEvent.VK_ESCAPE)
							setVisible(!isVisible());
						else {
							setVisible(true);
							calcDisplayList();
							updateUI();
						}
				}
			}

		};

		m_mouseListener = new AbsMouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() instanceof DropLabel)
					itemSelected((DropLabel) e.getSource());
			}


			@Override
			public void mouseEntered(MouseEvent e) {
				if (e.getSource() instanceof DropLabel)
					setSelectedIndex(Integer.valueOf(((DropLabel) e.getSource()).getName().substring(3)));
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

		m_componentAdapter = new ComponentAdapter() {

			@Override
			public void componentHidden(ComponentEvent e) {
				setVisible(false);
			}


			@Override
			public void componentMoved(ComponentEvent e) {
				updateUI();
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
	 * Sets the selected index, updates the UI to reflect this, and sends a
	 * DropEvent.
	 * 
	 * @param index
	 */
	private void setSelectedIndex(int index) {
		m_selectedIndex = index;
		if (m_lstLabels != null) {
			updateSelected();
		}
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
	 * Redraws the entire list based on current parameters. These parameters include
	 * the location of {@code m_txtComponent}, opacity and visibility of the window,
	 * and the borders of the {@code DropLabels}.
	 */
	private void updateUI() {
		if (!m_visible || m_displayList == null || m_displayList.size() <= 0) {
			if (m_window != null)
				m_window.setVisible(false);
			return;
		}

		int componentHeight = 15;
		Point txtLoc = m_txtComponent.getLocationOnScreen();
		txtLoc.y = txtLoc.y + m_txtComponent.getHeight();

		m_window.setOpacity(m_opacity);
		m_window.getContentPane().removeAll();
		m_window.revalidate();
		m_window.setLocation(txtLoc);
		m_window.setBounds(txtLoc.x, txtLoc.y, m_txtComponent.getWidth(), (componentHeight * m_displayList.size()) + 1);
		m_window.setAlwaysOnTop(true);

		m_lstLabels = new ArrayList<DropLabel>(m_displayList.size());

		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBounds(txtLoc.x, txtLoc.y, m_txtComponent.getWidth(), (componentHeight * m_displayList.size()) + 1);

		// Generate Labels
		for (int i = 0; i < m_displayList.size(); i++) {
			DropLabel lbl = new DropLabel();
			int x, y, w, h;
			x = 0;
			y = componentHeight * i;
			w = m_txtComponent.getWidth();
			h = componentHeight;
			lbl.setBounds(x, y, w, h);

			lbl.setText(m_displayList.get(i));

			// Listener and identifier
			lbl.setName("lbl" + i);
			lbl.addKeyListener(m_keyListener);
			lbl.addMouseListener(m_mouseListener);
			lbl.setBackground(m_clrBackground);
			lbl.setBorderTo(m_border);
			lbl.setVisible(true);

			p.add(lbl);

			m_lstLabels.add(i, lbl);
		}
		m_window.add(p);
		m_window.setVisible(true);
	}


	/**
	 * When an item is selected, the text of the label goes into the JTextComponent
	 * and the list is hidden.
	 * 
	 * @param label
	 */
	protected void itemSelected(DropLabel label) {
		m_txtComponent.setText(label.getText());
		sendEvent(new DropEvent(m_lstLabels.get(m_selectedIndex).getText(), m_selectedIndex));
		setVisible(false);
	}
}
