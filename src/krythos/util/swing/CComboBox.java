package krythos.util.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

@SuppressWarnings("serial") // Same-version serialization only
public class CComboBox<E> extends JComboBox<E> {


	/**
	 * Creates a <code>JComboBox</code> that takes its items from an
	 * existing <code>ComboBoxModel</code>. Since the
	 * <code>ComboBoxModel</code> is provided, a combo box created using
	 * this constructor does not create a default combo box model and
	 * may impact how the insert, remove and add methods behave.
	 *
	 * @param aModel the <code>ComboBoxModel</code> that provides the
	 *               displayed list of items
	 * @see DefaultComboBoxModel
	 */
	public CComboBox(ComboBoxModel<E> aModel) {
		super(aModel);
		init();
	}


	/**
	 * Creates a <code>JComboBox</code> that contains the elements
	 * in the specified array. By default the first item in the array
	 * (and therefore the data model) becomes selected.
	 *
	 * @param items an array of objects to insert into the combo box
	 * @see DefaultComboBoxModel
	 */
	public CComboBox(E[] items) {
		super(items);
		init();
	}


	/**
	 * Creates a <code>JComboBox</code> that contains the elements
	 * in the specified Vector. By default the first item in the vector
	 * (and therefore the data model) becomes selected.
	 *
	 * @param items an array of vectors to insert into the combo box
	 * @see DefaultComboBoxModel
	 */
	public CComboBox(Vector<E> items) {
		super(items);
		init();
	}


	/**
	 * Creates a <code>JComboBox</code> with a default data model.
	 * The default data model is an empty list of objects.
	 * Use <code>addItem</code> to add items. By default the first item
	 * in the data model becomes selected.
	 *
	 * @see DefaultComboBoxModel
	 */
	public CComboBox() {
		super();
		init();
	}


	private void init() {
		setPopupControls();
	}


	protected void setPopupControls() {
		getEditor().getEditorComponent().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}


			@Override
			public void keyPressed(KeyEvent e) {
			}


			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU)
					setPopupVisible(!isPopupVisible());
			}

		});
	}


	public static void main(String[] args) {

		JFrame f = new JFrame();
		f.setBounds(500, 300, 200, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JInternalFrame i_f = new JInternalFrame();
		i_f.setVisible(true);

		CComboBox<String> txt = new CComboBox<String>(
				new String[] { "hi", "hello", "hurricane", "seven", "hi", "hello", "hurricane", "seven", "sever" });
		txt.setEditable(true);
		txt.setBounds(0, 0, 100, 20);
		txt.setVisible(true);

		JButton btn = new JButton("hi");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("button");
				if (txt.isPopupVisible())
					txt.hidePopup();
				else
					txt.showPopup();
			}

		});
		btn.setVisible(true);

		i_f.setLayout(null);
		i_f.add(txt);
		i_f.add(btn);
		f.add(i_f);
		f.setVisible(true);
	}


	@Override
	public void updateUI() {
		super.updateUI();
		removeArrowButton();
	}


	/**
	 * Removes the drop-list Button.
	 */
	public void removeArrowButton() {
		Component[] comp = getComponents();
		Component removeComponent = null;
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof JButton) {
				removeComponent = comp[i];
			}
		}
		if (removeComponent != null) {
			remove(removeComponent);
		}
	}

}
