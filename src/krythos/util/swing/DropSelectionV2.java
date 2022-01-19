package krythos.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import krythos.util.abstract_interfaces.AbsComponentListener;
import krythos.util.abstract_interfaces.AbsKeyListener;
import krythos.util.abstract_interfaces.AbsMouseListener;
import krythos.util.logger.Log;

public class DropSelectionV2 {
	public class DropEvent {
		private Object m_source;
		private int m_index;

		public DropEvent(Object source, int index) {
			m_source = source;
			m_index = index;
		}


		public int getIndex() {
			return m_index;
		}


		public Object getSource() {
			return m_source;
		}
	}

	public interface DropListener {
		public void itemSelected(DropEvent e);
	}

	@SuppressWarnings("serial")
	private class DropSelectionFrame extends JWindow {
		private final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(1, 2, 1, 2);
		private final Border BORDER_SELECTED = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
		private ArrayList<DropListener> m_listeners;
		private Component m_parent;
		private Container m_container;
		private Object[] m_items;

		public DropSelectionFrame(Container container, Component parent, Object... items) {
			m_parent = parent;
			m_container = container;
			m_items = items;
			m_listeners = new ArrayList<DropListener>();
			createGUI();
			this.setVisible(false);
		}


		public void addDropListener(DropListener listener) {
			if (!containsDropListener(listener))
				m_listeners.add(listener);
		}


		public boolean containsDropListener(DropListener listener) {
			for (DropListener l : m_listeners)
				if (l.equals(listener))
					return true;
			return false;
		}


		public boolean removeDropListener(DropListener listener) {
			return m_listeners.remove(listener);
		}


		private void createGUI() {
			// Initialize JFrame
			this.setAlwaysOnTop(true);

			// Initialize ContentPane
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
			this.setContentPane(contentPane);

			// Create labels
			for (int x = 0; x < m_items.length; x++) {
				JTextArea txt = new JTextArea(m_items[x].toString());
				txt.setName("" + x);
				txt.setOpaque(true);
				txt.setBackground(Color.WHITE);
				txt.setMinimumSize(new Dimension(m_parent.getWidth(), 0));
				txt.setEditable(false);
				txt.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				txt.addMouseListener(new AbsMouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						itemClicked(Integer.valueOf(((JTextArea) e.getSource()).getName()));
					}


					@Override
					public void mouseEntered(MouseEvent e) {
						txt.setBorder(BORDER_SELECTED);
					}


					@Override
					public void mouseExited(MouseEvent e) {
						txt.setBorder(BORDER_EMPTY);
					}
				});
				this.add(txt);
			}
			// TODO labels -- Selection Borders

			// Add ComponentListener to parent & container for movement.
			AbsComponentListener componentListener = new AbsComponentListener() {
				@Override
				public void componentMoved(ComponentEvent e) {
					updateLocation();
				}
			};
			m_parent.addComponentListener(componentListener);
			m_container.addComponentListener(componentListener);

			m_container.addKeyListener(new AbsKeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
						setVisible(false);
				}
			});
			this.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					setVisible(true);
				}


				@Override
				public void focusLost(FocusEvent e) {
					setVisible(false);
				}

			});

			// Set Initial Location
			updateLocation();

			this.pack();
		}


		private void itemClicked(int index) {
			// TODO DropEvents
			Log.debug(this, "Item Clicked:" + m_items[index].toString());
			for (DropListener dl : m_listeners)
				dl.itemSelected(new DropEvent(m_items[index], index));
			this.setVisible(false);
		}


		private void updateLocation() {
			try {
				Point p = m_parent.getLocationOnScreen();
				p.y = p.y + m_parent.getHeight();
				setLocation(p.x, p.y);
			} catch (IllegalComponentStateException ex) {
				// Do Nothing
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String... args) {
		Log.setLevel(Log.LEVEL_DEBUG);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		f.setContentPane(contentPane);


		JLabel lbl = new JLabel("Test Label");
		DropSelectionV2 drop = new DropSelectionV2(f, lbl, "Test", "List", "Stuff", "Longer Test");
		drop.addDropListener(e -> {
			Log.printDialog(e.getSource().toString() + " at index " + e.getIndex());
		});
		drop.setVisible(false);
		lbl.addMouseListener(new AbsMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Log.debug(this, "Right Click");
					drop.setVisible(true);
				}
			}
		});

		f.add(lbl);

		f.pack();
		f.setBounds(200, 200, 500, 500);
		f.setVisible(true);
	}

	private Component m_parent;
	private Container m_container;
	private Object[] m_items;
	private DropSelectionFrame m_frame;

	public DropSelectionV2(Container container, Component parent, Object... items) {
		m_container = container;
		m_parent = parent;
		m_items = items;
		createFrame();
	}


	/**
	 * Adds {@code listener} for DropEvent triggers.
	 * 
	 * @param listener
	 */
	public void addDropListener(DropListener listener) {
		m_frame.addDropListener(listener);
	}


	/**
	 * Returns {@code true} if the listener is contained in the DropListener list.
	 * 
	 * @param listener
	 */
	public void containsDropListener(DropListener listener) {
		m_frame.containsDropListener(listener);
	}


	/**
	 * Removes {@code listener} for DropEvent triggers.
	 * 
	 * @param listener
	 */
	public void removeDropListener(DropListener listener) {
		m_frame.removeDropListener(listener);
	}


	/**
	 * Replaces the items currently in the DropSelection.
	 * 
	 * @param items Items which will be displayed in the {@link DropSelection}. They
	 *              are displayed as a {@code String}, as is returned by
	 *              {@code Object.toString()}.
	 */
	public void setItems(Object... items) {
		m_items = items;
		createFrame();
	}


	/**
	 * Manually set DropSelection visibility.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		m_frame.setVisible(visible);
		m_frame.requestFocus();
	}


	private void createFrame() {
		m_frame = new DropSelectionFrame(m_container, m_parent, m_items);
	}
	
	
	public void setLocation(int x, int y) {
		m_frame.setLocation(x,y);
	}
}
