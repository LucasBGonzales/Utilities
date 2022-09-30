package krythos.util.swing.kcontextmenu;

public class ContextEvent {
	private int m_index;
	private Object m_source;


	public ContextEvent(Object source, int index) {
		m_source = source;
		m_index = index;
	}


	public int getIndex() {
		return m_index;
	}


	public Object getSource() {
		return m_source;
	}
}
