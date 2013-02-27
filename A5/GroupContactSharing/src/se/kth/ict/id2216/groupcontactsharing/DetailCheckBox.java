package se.kth.ict.id2216.groupcontactsharing;

import se.kth.ict.id2216.groupcontactsharing.Contact.Type;
import android.widget.CheckBox;

public class DetailCheckBox {

	private Type type;
	private String text;
	private CheckBox cb;

	public DetailCheckBox(Type type, String text, CheckBox cb) {
		this.type = type;
		this.text = text;
		this.cb = cb;
	}

	public Type getType() {
		return this.type;
	}

	public String getText() {
		return this.text;
	}

	public boolean isChecked() {
		return cb.isChecked();
	}

	public void setChecked(boolean b) {
		this.cb.setChecked(b);
	}

	public int getId() {
		return cb.getId();
	}
	
}
