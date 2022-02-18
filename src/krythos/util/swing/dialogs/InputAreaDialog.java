package krythos.util.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class InputAreaDialog extends JDialog {
	private static final long serialVersionUID = 5609713195136302612L;
	private String result;
	private JTextArea txt;


	/**
	 * Creates an InputAreaDialog with multiple-lined JTextArea to
	 * retrieve user input as a {@code String}.
	 * 
	 * @param parent       The Frame this InputAreaDialog will be tied to.
	 * @param messsage     The message to display with this Dialog.
	 * @param initialValue The value to place into the JTextArea when the
	 *                     dialog opens.
	 * @param modality     specifies whether dialog blocks input to
	 *                     other windows when shown. null value and
	 *                     unsupported modality types are equivalent to
	 *                     MODELESS
	 */
	public InputAreaDialog(Frame parent, String message, String initialValue) {
		super(parent, "Input Dialog", true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);

		// Close Window returns an empty value.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				txt.setText("");
				closeOperation();
			}
		});

		// -- Create GUI -- //

		// BorderLayout as top-level organization. Keeps everything filling
		// the screen.
		getContentPane().setLayout(new BorderLayout());

		// Another BorderLayout so that I can add an empty border around
		// everything. Gives some padding.
		JPanel borderPane = new JPanel();
		borderPane.setLayout(new BorderLayout());
		borderPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		getContentPane().add(borderPane, BorderLayout.CENTER);

		// Spring Layout for TextArea and Button.
		JPanel springPane = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		springPane.setLayout(springLayout);
		borderPane.add(springPane, BorderLayout.CENTER);

		// Message JTextArea. Only if there is a message, else don't bother.
		// Keeps there from being an empty space.
		if (!message.equals("")) {
			JTextArea lbl = new JTextArea(message);
			lbl.setEditable(false);
			lbl.setLineWrap(true);
			lbl.setWrapStyleWord(true);
			borderPane.add(lbl, BorderLayout.NORTH);
		}

		// Entry JTextArea nested in a JScrollPane
		txt = new JTextArea(initialValue);
		JScrollPane scrollPane = new JScrollPane(txt);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		springPane.add(scrollPane);

		JButton btn = new JButton("OK");
		btn.addActionListener(e -> {
			closeOperation();
		});
		springPane.add(btn);


		// -- SpringLayout Constraints --

		// TextArea
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, springPane);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, springPane);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, springPane);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -5, SpringLayout.NORTH, btn);

		// Button
		springLayout.putConstraint(SpringLayout.NORTH, btn, -40, SpringLayout.SOUTH, springPane);
		springLayout.putConstraint(SpringLayout.WEST, btn, 0, SpringLayout.WEST, springPane);
		springLayout.putConstraint(SpringLayout.EAST, btn, 0, SpringLayout.EAST, springPane);
		springLayout.putConstraint(SpringLayout.SOUTH, btn, 0, SpringLayout.SOUTH, springPane);


		this.setMinimumSize(new Dimension(200, 200));
		this.setSize(300, 300);
		this.setLocationRelativeTo(null); // Center window
		this.setVisible(false);
	}


	/**
	 * This will set the result, hide the window, then dispose of itself.
	 */
	private void closeOperation() {
		result = txt.getText();
		setVisible(false);
		dispose();
	}


	/**
	 * Shows the window and returns the result. Because this dialog is
	 * modal, result will only be returned when the user has closed the
	 * window.
	 * 
	 * @return {@code String} of the user's input. If the user closed the
	 *         window (as opposed to selecting the "OK" button) then the
	 *         result will be an empty {@code String ""}.
	 */
	public String showDialog() {
		setVisible(true);
		return result;
	}
}
