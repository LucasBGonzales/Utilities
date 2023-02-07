package kytheros.util.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class JThumbnailPanel extends JPanel {
	private static final long serialVersionUID = -5429593033499407311L;

	private List<JThumbnailObject> m_thumbnails;
	private Dimension m_size;
	private boolean f_show_descriptions;


	public JThumbnailPanel(Dimension size, boolean show_descriptions) {
		m_thumbnails = new ArrayList<JThumbnailObject>();
		f_show_descriptions = show_descriptions;

		this.setLayout(new GridLayout());
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				refreshGUI();
			}
		});

		setSize(size);
		createGUI();
	}


	public void setSize(Dimension size) {
		m_size = size;
	}


	public void addThumbnail(JThumbnailObject tno) {
		if (contains(tno) == false)
			m_thumbnails.add(tno);
		sort();
	}


	public boolean contains(JThumbnailObject tno) {
		for (JThumbnailObject t : m_thumbnails)
			if (t.equals(tno))
				return true;
		return false;
	}


	private void sort() {
		if (m_thumbnails.size() > 1)
			Collections.sort(m_thumbnails, new JThumbnailObject.SortByDescription(true));
	}


	public void createGUI() {
		this.removeAll();
		GridLayout layout = (GridLayout) this.getLayout();
		layout.setHgap(2);
		layout.setVgap(2);
		refreshGUI();
	}


	public void refreshGUI() {
		GridLayout layout = (GridLayout) this.getLayout();
		layout.setHgap(2);
		layout.setVgap(2);
		int cols = (int) Math.max(1, Math.round(this.getWidth() / m_size.getWidth()));
		int rows = (int) Math.max(1, Math.round(this.getHeight() / m_size.getHeight()));
		layout.setColumns(cols);
		layout.setRows(rows);
		int new_labels = (cols * rows) - this.getComponents().length;

		// Remove Extraneous Components
		while (this.getComponents().length > cols * rows)
			this.remove(this.getComponents().length - 1);

		// Add Missing components
		for (int i = 0; i < new_labels; i++) { // For Each Cell
			JLabel lbl = new JLabel();
			lbl.setOpaque(true);
			lbl.setBackground(new Color((int) (Math.random() * Integer.MAX_VALUE)));
			this.add(lbl);
		}
		for(int i=0; i < m_thumbnails.size() && i < cols * rows; i++)
		{
			JThumbnailPanelItem item = new JThumbnailPanelItem(m_thumbnails.get(i), false);
			this.remove(i);
			this.add(item, i);
		}
		this.validate();
	}


	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Testing JThumbnailPanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(200, 200, 200, 200);

		JThumbnailPanel panel = new JThumbnailPanel(new Dimension(75, 75), false);
		panel.addThumbnail(new JThumbnailObject(ImageIO.read(new File("D:\\My Stuff\\Pictures\\Textures\\fabric.jpeg")),
				"Fabric", "1"));
		panel.addThumbnail(new JThumbnailObject(
				ImageIO.read(new File("D:\\My Stuff\\Pictures\\Textures\\wallhaven-56522.jpg")), "Paper", "2"));
		panel.addThumbnail(new JThumbnailObject(
				ImageIO.read(new File("D:\\My Stuff\\Pictures\\Textures\\wallhaven-187790.jpg")), "Abstract", "3"));

		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		panel.refreshGUI();
	}


	private class JThumbnailPanelItem extends JPanel {
		private static final long serialVersionUID = -4029437960921848210L;
		private JThumbnailObject m_thumbnail;
		private JLabel m_label;
		private JTextArea m_text;
		private boolean f_showText;


		public JThumbnailPanelItem(JThumbnailObject tno, boolean show_text) {
			m_thumbnail = tno;
			f_showText = show_text;

			this.setLayout(new BorderLayout());
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					refreshGUI();
				}
			});
			createGUI();
		}


		public void setSize(Dimension size) {
			m_size = size;
		}


		private void createGUI() {
			m_label = new JLabel();
			m_label.setOpaque(true);
			m_label.setBackground(new Color((int) (Math.random() * Integer.MAX_VALUE)));
			this.add(m_label, BorderLayout.CENTER);

			m_text = new JTextArea(m_thumbnail.getDescription());
			m_text.setEditable(false);
			this.add(m_text, BorderLayout.SOUTH);

			refreshGUI();
		}


		private void refreshGUI() {
			Image img = m_thumbnail.getImage();
			int width = m_label.getWidth();
			int height = m_label.getHeight();
			ImageIcon scaled_img = null;
			if (width != 0 && height != 0)
				scaled_img = new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
			m_label.setIcon(scaled_img);
			m_text.setVisible(f_showText);
		}
	}


	public static class JThumbnailObject {
		private Image m_image;
		private String m_description;
		private String m_ID;


		public JThumbnailObject() {
			m_ID = String.valueOf(Math.random() * Integer.MAX_VALUE);
			m_description = "N/A";
			Image m_image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
		}


		public JThumbnailObject(Image image, String description, String ID) {
			m_ID = ID;
			setImage(image);
			setDescription(description);
		}


		public String getID() {
			return m_ID;
		}


		public String getDescription() {
			return m_description;
		}


		public Image getImage() {
			return m_image;
		}


		public void setImage(Image image) {
			m_image = image;
		}


		public void setDescription(String description) {
			m_description = description;
		}


		@Override
		public boolean equals(Object obj) {
			if (obj instanceof JThumbnailObject == false)
				return false;
			return getID().equals(((JThumbnailObject) obj).getID());
		}


		public static class SortByDescription implements Comparator<JThumbnailObject> {
			private boolean m_ascending;


			public SortByDescription(boolean ascending) {
				m_ascending = ascending;
			}


			@Override
			public int compare(JThumbnailObject o1, JThumbnailObject o2) {
				String s1 = o1.getDescription();
				String s2 = o2.getDescription();

				if (m_ascending)
					return s1.compareTo(s2);
				else
					return s2.compareTo(s1);
			}
		}
	}
}
