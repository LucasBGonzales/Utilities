package krythos.util.swing.kcontextmenu;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import krythos.util.abstract_interfaces.AbsMouseListener;
import krythos.util.logger.Log;
import krythos.util.swing.SwingMisc;

public class KContextMenu {
	private ContextMenuFrame mFrame;
	private Object[] mItems;
	private Component mParent;


	/*
	 * Example
	 */
	public static void main(String... args) {
		Log log = new Log(false);
		log.setLevel(Log.LEVEL_DEBUG);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		f.setContentPane(contentPane);


		JLabel lbl = new JLabel("Test Label");
		KContextMenu menu = new KContextMenu(lbl, "Test", "List", "Stuff", "Longer Test");

		// Escape to close Context Menu
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					menu.setVisible(false);
			}
		});


		menu.addContextListener(e -> {
			log.showMessageDialog(e.getSource().toString() + " at index " + e.getIndex());
		});

		f.add(lbl);

		f.pack();
		f.setBounds(200, 200, 300, 200);
		SwingMisc.centerWindow(f);
		f.setVisible(true);
	}


	public KContextMenu(Component parent, Object... items) {
		mParent = parent;
		mItems = items;

		/*
		 * Click Anywhere to close Context Menu (Doesn't work for clicking the Window
		 * Header [tested on Linux*])
		 */
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				if (e.getID() == MouseEvent.MOUSE_PRESSED)
					setVisible(false);
			}
		}, AWTEvent.MOUSE_EVENT_MASK);
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				if (e.getID() == FocusEvent.FOCUS_LOST)
					setVisible(false);
			}
		}, AWTEvent.FOCUS_EVENT_MASK);

		// Right-click target to open Context Menu
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
		mFrame.addContextListener(listener);
	}


	/**
	 * Returns {@code true} if the listener is contained in the ContextListener
	 * list.
	 * 
	 * @param listener
	 */
	public void containsContextListener(ContextListener listener) {
		mFrame.containsContextListener(listener);
	}


	/**
	 * Removes {@code listener} for ContextEvent triggers.
	 * 
	 * @param listener
	 */
	public void removeContextListener(ContextListener listener) {
		mFrame.removeContextListener(listener);
	}


	/**
	 * Replaces the items currently in the {@link KContextMenu}.
	 * 
	 * @param items Items which will be displayed in the context menu. They are
	 *              displayed as a {@code String}, as is returned by
	 *              {@code Object.toString()}.
	 */
	public void setItems(Object... items) {
		mItems = items;
		createFrame();
	}


	/**
	 * Manually set KContextMenu visibility.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		mFrame.setVisible(visible);
		mFrame.requestFocus();
	}


	private void createFrame() {
		mFrame = new ContextMenuFrame(mParent, mItems);
	}
}
