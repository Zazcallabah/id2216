package se.kth.ict.id2216.groupcontactsharing;

import com.google.gson.Gson;

public class ContactViewModel  {

	public ContactViewModel()
	{
	}
	
	private static Gson _gson = new Gson();
	public static ContactViewModel CreateViewModelFromJson( String json )
	{
	 return	_gson.fromJson(json, ContactViewModel.class);
	}
	
	public String Serialize()
	{
		return _gson.toJson( this );
	}
}
