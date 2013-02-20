package se.kth.ict.id2216.groupcontactsharing;

public class NewContactEvent  extends java.util.EventObject {
	private static final long serialVersionUID = -4975144166877815230L;
	private String _uuid;

	//here's the constructor
	public NewContactEvent(Object source, String uuid) {
		super(source);
		_uuid=uuid;
	}

	public String getUuid() {
		return _uuid;
	}
}
