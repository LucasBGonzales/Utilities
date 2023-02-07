package kytheros.util.abstract_interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

public class FunctionalAction extends AbstractAction {
	private static final long serialVersionUID = -6690207775638659047L;
	ActionListener myaction;


	public FunctionalAction(ActionListener customaction) {
		this.myaction = customaction;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		myaction.actionPerformed(e);
	}


	public ActionListener getMyaction() {
		return myaction;
	}


	public void setMyaction(ActionListener myaction) {
		this.myaction = myaction;
	}
}
