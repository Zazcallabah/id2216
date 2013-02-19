package se.kth.ict.id2216.groupcontactsharing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.CDATASection;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

public class ContactViewModel  {
	private static String StorageLocation = "Contact_Data.json";
	
	private String name = "";
	private String id = null;
	private String email = "";
	private String phone = "";
	private String label = "";
	private boolean nameSelected = true;
	private boolean emailSelected = true;
	private boolean phoneSelected = true;
	public List<ContactDetails> _remoteData = new LinkedList<ContactDetails>();

	public ContactViewModel()
	{
	}
	
	void setFields( ContactViewModel source )
	{
		this.setName(source.getName());
		this.setEmail(source.getEmail());
		this.setPhone(source.getPhone());
		this.getId();
		nameSelected=source.nameSelected;
		emailSelected=source.emailSelected;
		phoneSelected=source.phoneSelected;
	}
	
	public void save( Activity act )
	{

		try {
			FileOutputStream fos = act.openFileOutput(StorageLocation, Context.MODE_PRIVATE);
			fos.write(Serialize().getBytes("UTF-8"));
		fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void restore( Activity act )
	{
			try {
	            FileInputStream fIn;
				fIn = act.openFileInput ( StorageLocation );
	            JsonReader jr = new JsonReader(fIn);
	            setFields(jr.get( ContactViewModel.class ));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
	}
	
	private static Gson _gson = new Gson();
	public static ContactViewModel CreateViewModelFromJson( String json )
	{
		try{
	 return	_gson.fromJson(json, ContactViewModel.class);
		}catch(Exception e){
			return new ContactViewModel();
		}
	}
	
	public String Serialize()
	{
		return _gson.toJson( this );
	}



	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNameSelected() {
		return nameSelected;
	}

	public void setNameSelected(boolean nameSelected) {
		this.nameSelected = nameSelected;
	}
	public boolean isEmailSelected() {
		return emailSelected;
	}

	public void setEmailSelected(boolean emailSelected) {
		this.emailSelected = emailSelected;
	}
	public boolean isPhoneSelected() {
		return phoneSelected;
	}

	public void setPhoneSelected(boolean phoneSelected) {
		this.phoneSelected = phoneSelected;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		if( id == null )
			id = java.util.UUID.randomUUID().toString();
		return id;
	}

	public void mergeContacts(List<ContactDetails> list) {
		for(ContactDetails cd : list)
		{
			boolean isnew = true;
			for( ContactDetails existing: _remoteData)
			{
				if( existing.id == cd.id )
				{
					isnew=false;
					break;
				}
			}
			if( isnew )
			{
				_remoteData.add(cd);
				// fire some kind of property changed event
			}
		}
		
	}

}
