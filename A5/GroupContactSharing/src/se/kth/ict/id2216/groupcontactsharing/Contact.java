package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class Contact implements View.OnClickListener{

	private static int id = 1;
	private ContactDetails details;

	private RelativeLayout rel;
	private CheckBox cbName;

	private List<DetailCheckBox> cbList = new ArrayList<DetailCheckBox>();
	
	// TODO: may have to be changed in future versions!
	public static enum Type {
		NAME, PHONE, MAIL
	}

	public Contact(ContactDetails details, Contact oldContact, RelativeLayout rel, int relativeToBelow, int relativeToLeft) {
		this.details = details;
		this.rel = rel;
		cbName = new CheckBox(rel.getContext());
		cbName.setText(details.displayname);
		cbName.setChecked(oldContact != null ? oldContact.cbName.isChecked() : true);
		cbName.setId(id++);
		cbName.setOnClickListener(this);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, relativeToBelow);
		params.addRule(RelativeLayout.ALIGN_LEFT, relativeToLeft);
		cbName.setLayoutParams(params);

		rel.addView(cbName);
		addDetails(oldContact);
	}

	@Override
	public void onClick(View v) {
		boolean b = ((CheckBox) v).isChecked();
		setChecked(b);
	}
	
	private void addDetails(Contact oldContact) {
		if (details.fullname != null && !details.fullname.equals("")) {
			boolean b = oldContact != null ? oldContact.getStatusOf(Type.NAME) : true;
			addCheckBox("Full name: " + details.fullname, Type.NAME, b);
		}
		if (details.phone != null && !details.phone.equals("")) {
			boolean b = oldContact != null ? oldContact.getStatusOf(Type.PHONE) : true;
			addCheckBox("Phone: " + details.phone, Type.PHONE, b);
		}
		if (details.email != null && !details.email.equals("")) {
			boolean b = oldContact != null ? oldContact.getStatusOf(Type.MAIL): true;
			addCheckBox("E-Mail: " + details.email, Type.MAIL, b);
		}
	}

	private boolean getStatusOf(Type type) {
		boolean b = true;
		for (DetailCheckBox detailCb : cbList) {
			if (detailCb.getType() == type)
				b = detailCb.isChecked();
		}
		return b;
	}

	public void addCheckBox(String text, Type type, boolean b) {
		CheckBox newCheckBox = new CheckBox(rel.getContext());
		newCheckBox.setText(text);
		newCheckBox.setId(id++);
		newCheckBox.setChecked(b);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		if (cbList.isEmpty()) {
			params.addRule(RelativeLayout.BELOW, cbName.getId());
			params.addRule(RelativeLayout.ALIGN_LEFT, cbName.getId());
			params.setMargins(30, 0, 0, 0);
		}
		else {
			int relToId = cbList.get(cbList.size()-1).getId();
			params.addRule(RelativeLayout.BELOW, relToId);
			params.addRule(RelativeLayout.ALIGN_LEFT, relToId);
		}
		newCheckBox.setLayoutParams(params);

		DetailCheckBox detailCB = new DetailCheckBox(type, text, newCheckBox);
		cbList.add(detailCB);
		rel.addView(newCheckBox);
	}

	public void setChecked(boolean b) {
		cbName.setChecked(b);
		for (DetailCheckBox cb : cbList)
			cb.setChecked(b);
	}

	public int getId() {
		return id-1;
	}
	
	public String getUuid() {
		return details.id;
	}

	public ContactDetails getDetails() {
		ContactDetails checkedDetails = new ContactDetails();

		int count = 0;
		for (DetailCheckBox cb : cbList) {
			if (cb.isChecked()) {
				switch (cb.getType()) {
				case NAME : 
					checkedDetails.fullname = details.fullname;
					count++;
					break;
				case PHONE :
					checkedDetails.phone = details.phone;
					count++;
					break;
				case MAIL :
					checkedDetails.email = details.email;
					count++;
					break;
				}
			}
		}
		if (count > 0) {
			checkedDetails.displayname = details.displayname;
			return checkedDetails;
		}
		else 
			return null;
	}

}