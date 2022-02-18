package krythos.util.swing;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import krythos.util.swing.dialogs.InputAreaDialog;
import krythos.util.swing.dialogs.InputListDialog;
import krythos.util.swing.dialogs.InputListDialog.ListSelection;

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
	public static String showInputAreaDialog(@SuppressWarnings("exports") Frame parent, String message, String initialValue) {
		return (new InputAreaDialog(parent, message, initialValue)).showDialog();
	}
	
	
	@SuppressWarnings("exports")
	public static InputListDialog.ListSelection[] showInputListDialog(Frame parent, ListSelection[] list_selections){
		return (new InputListDialog(parent,list_selections)).showDialog();
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
	public static File[] fileChooser(boolean multiple_files, @SuppressWarnings("exports") FileFilter filter, File current_dir) {
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
}
