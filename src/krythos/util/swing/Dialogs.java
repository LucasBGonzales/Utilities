package krythos.util.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

public class Dialogs {

	/**
	 * Shows an InputAreaDialog box that allows the user to type a
	 * response into a
	 * scroll-able JTextArea.
	 * 
	 * @param parent       The Frame this InputAreaDialog will be tied to.
	 * @param messsage     The message to display with this Dialog.
	 * @param initialValue The value to place into the JTextArea when the
	 *                     dialog
	 *                     opens.
	 * @param modal        If true, the window will be modal and thus
	 *                     prevent
	 *                     interaction with the parent frame. Else, it
	 *                     will be
	 *                     modeless and allow interaction with the parent
	 *                     frame.
	 * @return {@code String} of the user's response.
	 */
	public static String showInputAreaDialog(Frame parent, String message, String initialValue) {
		return (new InputAreaDialog(parent, message, initialValue)).showDialog();
	}


	/**
	 * This function constructs and displays a JFileChooser dialog to the
	 * user based on the given parameters. This function only allows the
	 * choice of files.
	 * 
	 * @param multiple_files Whether to retrieve one file or multiple
	 *                       files from the user.
	 * @param filter         FileFilter for filtering the set of files
	 *                       shown to the user.
	 * @param current_dir    First directory the FileChooser shows to the
	 *                       user.
	 * @return {@code File[]} of user's choices, or {@code null} if
	 *         chooser was canceled.
	 */
	public static File[] fileChooser(boolean multiple_files, FileFilter filter, File current_dir) {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileDialog.setFileFilter(filter);
		fileDialog.setMultiSelectionEnabled(multiple_files);
		fileDialog.setCurrentDirectory(current_dir);
		int returnVal = fileDialog.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (multiple_files)
				return fileDialog.getSelectedFiles();
			else
				return new File[] { fileDialog.getSelectedFile() };
		}
		return null;
	}


	/**
	 * This function constructs and displays a JFileChooser dialog to the
	 * user based on the given parameters. This function only allows the
	 * choice of folders.
	 * 
	 * @param multiple_folders Whether to retrieve one file or multiple
	 *                         files from the user.
	 * @param current_dir      First directory the FileChooser shows to
	 *                         theuser.
	 * @return {@code File[]} of user's choices, or {@code null} if
	 *         chooser was canceled.
	 */
	public static File[] folderChooser(boolean multiple_folders, File current_dir) {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileDialog.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}


			@Override
			public String getDescription() {
				return "Folders";
			}
		});
		fileDialog.setMultiSelectionEnabled(multiple_folders);
		fileDialog.setCurrentDirectory(current_dir);
		int returnVal = fileDialog.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (multiple_folders)
				return fileDialog.getSelectedFiles();
			else
				return new File[] { fileDialog.getSelectedFile() };
		}
		return null;
	}


	@SuppressWarnings("serial")
	private static class InputAreaDialog extends JDialog {
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
}
