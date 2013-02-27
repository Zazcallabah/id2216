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

	public Contact(ContactDetails details, RelativeLayout rel, int relativeToBelow, int relativeToLeft) {
		this.details = details;
		this.rel = rel;
		cbName = new CheckBox(rel.getContext());
		cbName.setText(details.displayname);
		cbName.setChecked(true);
		cbName.setId(id++);
		cbName.setOnClickListener(this);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, relativeToBelow);
		params.addRule(RelativeLayout.ALIGN_LEFT, relativeToLeft);
		cbName.setLayoutParams(params);

		rel.addView(cbName);
		addDetails();
	}

	@Override
	public void onClick(View v) {
		boolean b = ((CheckBox) v).isChecked();
		setChecked(b);
	}

	private void addDetails() {
		if (details.fullname != null && !details.fullname.equals(""))
			addCheckBox("Full name: " + details.fullname, Type.NAME);
		if (details.phone != null && !details.phone.equals(""))
			addCheckBox("Phone: " + details.phone, Type.PHONE);
		if (details.email != null && !details.email.equals(""))
			addCheckBox("E-Mail: " + details.email, Type.MAIL);
	}

	public void addCheckBox(String text, Type type) {
		CheckBox newCheckBox = new CheckBox(rel.getContext());
		newCheckBox.setText(text);
		newCheckBox.setId(id++);
		newCheckBox.setChecked(true);

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

	public ContactDetails getDetails() {
		ContactDetails checkedDetails = new ContactDetails();

		checkedDetails.displayname = details.displayname;

		for (DetailCheckBox cb : cbList) {
			if (cb.isChecked()) {
				switch (cb.getType()) {
				case NAME : 
					checkedDetails.fullname = details.fullname;
					break;
				case PHONE :
					checkedDetails.phone = details.phone;
					break;
				case MAIL :
					checkedDetails.email = details.email;
					break;
				}
			}
		}

		return checkedDetails;
	}

}