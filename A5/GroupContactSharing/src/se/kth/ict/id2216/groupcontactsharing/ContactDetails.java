package se.kth.ict.id2216.groupcontactsharing;

import com.google.gson.Gson;

public class ContactDetails {
	public String id = null;
	public String displayname = null;
	public String fullname = null;
	public String email = null;
	public String phone = null;

	public static ContactDetails Create( ContactViewModel model )
	{
		ContactDetails d = new ContactDetails();
		d.id = model.getId();
		d.displayname = model.getDisplayName();
		if( model.isFullNameSelected() )
			d.fullname = model.getFullName();
		if( model.isEmailSelected() )
			d.email = model.getEmail();
		if( model.isPhoneSelected())
			d.phone = model.getPhone();
		return d;
	}

	private static Gson _gson = new Gson();

	public String Serialize()
	{
		return _gson.toJson( this );
	}
}
