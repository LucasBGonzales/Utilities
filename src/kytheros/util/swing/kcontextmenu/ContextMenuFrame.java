package kytheros.util.swing.kcontextmenu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.border.Border;

import kytheros.util.abstract_interfaces.AbsMouseListener;
import kytheros.util.logger.Log;

@SuppressWarnings("serial")
public class ContextMenuFrame extends JWindow {
	private final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(1, 2, 1, 2);
	private final Border BORDER_SELECTED = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
	private Object[] m_items;
	private ArrayList<ContextListener> m_listeners;
	private Component m_parent;


	public ContextMenuFrame(Component parent, Object... items) {
		m_parent = parent;
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


		// Set Initial Location
		updateLocation();

		this.pack();
	}


	@Override
	public void setVisible(boolean b) {
		if (b)
			updateLocation();
		super.setVisible(b);
	}


	private void itemClicked(int index) {
		// TODO ContextEvents
		for (ContextListener dl : m_listeners)
			dl.itemSelected(new ContextEvent(m_items[index], index));
		this.setVisible(false);
	}


	private void updateLocation() {
		try {
			Point p = MouseInfo.getPointerInfo().getLocation();
			Log.println(p.toString());
			setLocation(p.x, p.y);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
