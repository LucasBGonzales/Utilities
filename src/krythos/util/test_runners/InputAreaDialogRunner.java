package krythos.util.test_runners;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import krythos.util.swing.Dialogs;

public class InputAreaDialogRunner {

	public static void main(String[] args) {
		JFrame frame = new JFrame("HI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lbl = new JLabel("Default");
		lbl.setBackground(Color.LIGHT_GRAY);
		lbl.setBounds(0, 0, 500, 500);
		frame.add(lbl);

		frame.setBounds(0, 0, 500, 500);
		frame.setVisible(true);
		lbl.setText(Dialogs.showInputAreaDialog(null, "Enter in your test value below, in that big 'ol text box.", "This is where you enter your values."));
		lbl.setText(Dialogs.showInputAreaDialog(null, "", "Empty Message"));
	}

}
