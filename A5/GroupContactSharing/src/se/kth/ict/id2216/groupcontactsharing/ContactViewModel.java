package se.kth.ict.id2216.groupcontactsharing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

public class ContactViewModel  {
	private static String StorageLocation = "Contact_Data.json";
	
	private String name = "";
	private String email = "";
	private String phone = "";

	public ContactViewModel()
	{
	}
	
	void setFields( ContactViewModel source )
	{
		this.setName(source.getName());
		this.setEmail(source.getEmail());

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
		StringBuilder data = new StringBuilder() ;
        try {
            FileInputStream fIn = act.openFileInput ( StorageLocation ) ;
            InputStreamReader isr = new InputStreamReader ( fIn ,"UTF-8") ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
               data.append(readString);
               data.append('\n');
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
            setFields( CreateViewModelFromJson(data.toString()));
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        
        
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
}
