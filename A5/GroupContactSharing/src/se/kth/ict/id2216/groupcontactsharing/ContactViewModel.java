package se.kth.ict.id2216.groupcontactsharing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;

public class ContactViewModel  {

	private List<NewContactAddedListener> _listeners = new ArrayList<NewContactAddedListener>();
	public synchronized void addEventListener(NewContactAddedListener listener)	{
		if(! _listeners.contains(listener))
			_listeners.add(listener);
	}
	public synchronized void removeEventListener(NewContactAddedListener listener)	{
		_listeners.remove(listener);
	}

	private List<ContactUpdatedListener> _ulisteners = new ArrayList<ContactUpdatedListener>();
	public synchronized void addUEventListener(ContactUpdatedListener ulistener)	{
		if(! _ulisteners.contains(ulistener))
			_ulisteners.add(ulistener);
	}
	public synchronized void removeUEventListener(ContactUpdatedListener listener)	{
		_ulisteners.remove(listener);
	}
	
	private static String StorageLocation = "Contact_Data.json";

	private String displayname = "";
	private String fullname = "";
	private String id = null;
	private String email = "";
	private String phone = "";
	private String label = "";
	private boolean fullNameSelected = true;
	private boolean emailSelected = true;
	private boolean phoneSelected = true;
	private List<ContactDetails> _remoteData;
	
	private boolean active = false;

	public ContactViewModel()
	{
		_remoteData = new LinkedList<ContactDetails>();
	}

	void setFields( ContactDetails source )
	{
		this.setFullName(source.fullname);
		this.setDisplayName(source.displayname);
		this.setEmail(source.email);
		this.setPhone(source.phone);
		this.getId();
		fullNameSelected=true;
		emailSelected=true;
		phoneSelected=true;
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
			setFields(jr.get( ContactDetails.class ));
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
		ContactDetails d =new ContactDetails();
		d.email=this.email;
		d.fullname=this.fullname;
		d.displayname=this.displayname;
		d.phone=this.phone;
		return _gson.toJson( d );
	}

	public void ForeachContact( ContactAction a )
	{
		synchronized(_remoteData)
		{
			for(ContactDetails cd : _remoteData )
			{
				a.run( cd );
			}
		}
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

	public String getFullName() {
		return fullname;
	}

	public void setFullName(String name) {
		this.fullname = name;
	}
	
	public String getDisplayName() {
		return displayname;
	}

	public void setDisplayName(String name) {
		this.displayname = name;
	}

	public boolean isFullNameSelected() {
		return fullNameSelected;
	}

	public void setFullNameSelected(boolean nameSelected) {
		this.fullNameSelected = nameSelected;
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
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getId() {
		if( id == null )
			id = java.util.UUID.randomUUID().toString();
		return id;
	}
	
	public void mergeContacts(List<ContactDetails> list) {
		if( list == null )
			return;
		for(ContactDetails cd : list)
		{
			if( cd.id.equals( this.id ))
				continue;
			boolean isnew = true;

			synchronized(_remoteData)
			{
				for( ContactDetails existing: _remoteData)
				{
					if( existing.id.equals( cd.id ) )
					{
						isnew=false;
						UpdateIfDifferent( existing, cd );
						break;
					}
				}
				if( isnew )
				{
					_remoteData.add(cd);
				}
			}
			if( isnew )
				fireEventNewContactAdded(cd.id);
		}
	}
	
	private boolean fuckingJavaStringEquals( String a, String b )
	{
		if( a == null && b == null )
			return true;
		if( a == null || b == null )
			return false;
		return a.equals( b );
	}
	
	private void UpdateIfDifferent(ContactDetails existing, ContactDetails incoming) {
				
		if(	
				fuckingJavaStringEquals( existing.displayname, incoming.displayname )  &&
				fuckingJavaStringEquals( existing.fullname, incoming.fullname )  &&
				fuckingJavaStringEquals( existing.phone, incoming.phone )  &&
				fuckingJavaStringEquals( existing.email, incoming.email )  )
			return;
		
		existing.displayname = incoming.displayname;
		existing.email = incoming.email;
		existing.phone = incoming.phone;
		existing.fullname = incoming.fullname;
		
		fireContactUpdated(incoming.id);
	}
	
	public void ClearReceivedContacts()
	{
		synchronized(_remoteData)
		{
			_remoteData.clear();
		}
	}

	public ContactDetails getContactById( String uuid )
	{
		synchronized(_remoteData)
		{
			for( ContactDetails cd: _remoteData)
			{
				if( uuid.equals(cd.id) )
				{
					return cd;
				}
			}
		}
		return null;		
	}
	private synchronized void fireEventNewContactAdded(String uuid)	{
		NewContactEvent event = new NewContactEvent(this, uuid);
		Iterator<NewContactAddedListener> i = _listeners.iterator();
		while(i.hasNext())	{
			((NewContactAddedListener) i.next()).newContactAdded(event);
		}
	}
	private synchronized void fireContactUpdated(String uuid)	{
		ContactUpdatedEvent event = new ContactUpdatedEvent(this, uuid);
		Iterator<ContactUpdatedListener> i = _ulisteners.iterator();
		while(i.hasNext())	{
			((ContactUpdatedListener) i.next()).contactUpdated(event);
		}
	}
}