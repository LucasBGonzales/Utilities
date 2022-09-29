package krythos.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import krythos.util.abstract_interfaces.AbsMouseListener;
import krythos.util.logger.Log;

public class KContextMenu {
	public class ContextEvent {
		private int m_index;
		private Object m_source;


		public ContextEvent(Object source, int index) {
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


	public interface ContextListener {
		public void itemSelected(ContextEvent e);
	}


	@SuppressWarnings("serial")
	private class ContextMenuFrame extends JWindow {
		private final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(1, 2, 1, 2);
		private final Border BORDER_SELECTED = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		private Container m_container;
		private Object[] m_items;
		private ArrayList<ContextListener> m_listeners;
		private Component m_parent;


		public ContextMenuFrame(Container container, Component parent, Object... items) {
			m_parent = parent;
			m_container = container;
			m_items = items;
			m_listeners = new ArrayList<ContextListener>();
			createGUI();
			this.setVisible(false);
		}


		public void addContextListener(ContextListener listener) {
			if (!containsContextListener(listener))
				m_listeners.add(listener);
		}


		public boolean containsContextListener(ContextListener listener) {
			for (ContextListener l : m_listeners)
				if (l.equals(listener))
					return true;
			return false;
		}


		public boolean removeContextListener(ContextListener listener) {
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

			m_container.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
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

		@Override
		public void setVisible(boolean b) {
			if(b)
				updateLocation();
			super.setVisible(b);
		}

		private void itemClicked(int index) {
			// TODO ContextEvents
			Log.debug(this, "Item Clicked:" + m_items[index].toString());
			for (ContextListener dl : m_listeners)
				dl.itemSelected(new ContextEvent(m_items[index], index));
			this.setVisible(false);
		}


		private void updateLocation() {
			try {
				/**
				 * OLD method, placed just under component.
				 * Point p = m_parent.getLocationOnScreen(); 
				 * p.y = p.y + m_parent.getHeight();
				 */
				Point p = MouseInfo.getPointerInfo().getLocation();
				Log.println(p.toString());
				setLocation(p.x, p.y);
			} catch (IllegalComponentStateException ex) {
				// Do Nothing
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	private Container m_container;
	private ContextMenuFrame m_frame;
	private Object[] m_items;
	private Component m_parent;


	public static void main(String... args) {
		Log.setLevel(Log.LEVEL_DEBUG);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		f.setContentPane(contentPane);


		JLabel lbl = new JLabel("Test Label");
		KContextMenu menu = new KContextMenu(f, lbl, "Test", "List", "Stuff", "Longer Test");
		menu.addContextListener(e -> {
			Log.showMessageDialog(e.getSource().toString() + " at index " + e.getIndex());
		});

		f.add(lbl);

		f.pack();
		f.setBounds(200, 200, 300, 200);
		SwingMisc.centerWindow(f);
		f.setVisible(true);
	}


	public KContextMenu(Container container, Component parent, Object... items) {
		m_container = container;
		m_parent = parent;
		m_items = items;
		parent.addMouseListener(new AbsMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					setVisible(true);
			}
		});
		createFrame();
		setVisible(false);
	}


	/**
	 * Adds {@code listener} for ContextEvent triggers.
	 * 
	 * @param listener
	 */
	public void addContextListener(ContextListener listener) {
		m_frame.addContextListener(listener);
	}


	/**
	 * Returns {@code true} if the listener is contained in the ContextListener
	 * list.
	 * 
	 * @param listener
	 */
	public void containsContextListener(ContextListener listener) {
		m_frame.containsContextListener(listener);
	}


	/**
	 * Removes {@code listener} for ContextEvent triggers.
	 * 
	 * @param listener
	 */
	public void removeContextListener(ContextListener listener) {
		m_frame.removeContextListener(listener);
	}


	/**
	 * Replaces the items currently in the {@link KContextMenu}.
	 * 
	 * @param items Items which will be displayed in the context menu. They are
	 *              displayed as a {@code String}, as is returned by
	 *              {@code Object.toString()}.
	 */
	public void setItems(Object... items) {
		m_items = items;
		createFrame();
	}


	public void setLocation(int x, int y) {
		m_frame.setLocation(x, y);
	}


	/**
	 * Manually set KContextMenu visibility.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		m_frame.setVisible(visible);
		m_frame.requestFocus();
	}


	private void createFrame() {
		m_frame = new ContextMenuFrame(m_container, m_parent, m_items);
	}
}
