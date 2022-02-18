package krythos.util.swing.dialogs;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputListDialog extends JDialog {
	private static final long serialVersionUID = -8707636189331803226L;
	private ListSelection[] list_selections;


	public InputListDialog(Frame parent, ListSelection[] list_selections) {
		super(parent, "Input Dialog", true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);

		// Close Window returns an empty value.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// closeOperation();
				cancel();
			}
		});

		this.list_selections = list_selections;
		createGUI();
	}


	private void createGUI() {
		Container content_pane = getContentPane();
		content_pane.setLayout(new BoxLayout(content_pane, BoxLayout.PAGE_AXIS));

		for (int i = 0; i < list_selections.length; i++) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

			JLabel txt = new JLabel(list_selections[i].message.toString());
			txt.setOpaque(false);
			txt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			panel.add(txt);

			JComboBox<Object> cbx = new JComboBox<Object>(list_selections[i].getSelectionValues());
			cbx.setSelectedIndex(list_selections[i].getValueIndex());
			cbx.setName(i + "");
			cbx.addActionListener(
					e -> list_selections[Integer.valueOf(cbx.getName())].setSelectedValue(cbx.getSelectedIndex()));

			panel.add(cbx);

			// Spacer
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

			content_pane.add(panel);
		}

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
		pnlButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(e -> closeOperation());

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> cancel());

		pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnOK);
		pnlButtons.add(btnCancel);
		
		content_pane.add(pnlButtons);

		// this.setMinimumSize(new Dimension(200, 200));
		this.pack();
		// this.setSize(300, 300);
		this.setLocationRelativeTo(null); // Center window
		this.setVisible(false);
	}


	private void cancel() {
		list_selections = null;
		closeOperation();
	}


	/**
	 * This will set the result, hide the window, then dispose of itself.
	 * If the window was closed, as opponed to hitting "OK", then null
	 * will be returned.
	 */
	private void closeOperation() {
		setVisible(false);
		dispose();
	}


	/**
	 * Shows the window and returns the {@link ListSelection}s. Because
	 * this dialog is
	 * modal, result will only be returned when the user has closed the
	 * window.
	 * 
	 * @return {@link ListSelection} array of the user's selections.
	 */
	public ListSelection[] showDialog() {
		setVisible(true);
		return list_selections;
	}


	public static class ListSelection {
		private Object message;
		private int selected_value;
		private Object[] selection_values;


		public ListSelection(Object message, Object[] selectionValues, int initialSelectionValue) {
			this.setMessage(message);
			this.setSelectedValue(initialSelectionValue);
			this.setSelectionValues(selectionValues);
		}


		public Object getMessage() {
			return this.message;
		}


		public Object getValue() {
			return getSelectionValues()[getValueIndex()];
		}


		public int getValueIndex() {
			return this.selected_value;
		}


		public Object[] getSelectionValues() {
			return this.selection_values;
		}


		public void setMessage(Object message) {
			this.message = message;
		}


		public void setSelectedValue(int value) {
			this.selected_value = value;
		}


		public void setSelectionValues(Object[] selectionValues) {
			this.selection_values = selectionValues;
		}
	}
}
