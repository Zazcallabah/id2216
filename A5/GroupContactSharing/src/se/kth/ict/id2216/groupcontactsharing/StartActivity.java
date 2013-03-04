package se.kth.ict.id2216.groupcontactsharing;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class StartActivity extends Activity {

	public static final String ServerUriString = "http://130.229.162.179:8888/api/";
	//public static final String ServerUriString = "http://tmp.prefect.se:8888/api/";

	private ContactViewModel _model;
	private CheckBox email;
	private CheckBox fullname;
	private CheckBox phone;
	private TextView displayname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();

		setContentView(R.layout.activity_start);
		Button b = (Button)findViewById(R.id.shareButton);
		b.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		email =(CheckBox)findViewById(R.id.emailCheckBox); 
		displayname =(TextView)findViewById(R.id.displayNameTextView); 
		phone =(CheckBox)findViewById(R.id.phoneCheckBox); 
		fullname =(CheckBox)findViewById(R.id.nameCheckBox); 

	}
	
	void refresh ()
	{
		fullname.setChecked(_model.isFullNameSelected());
		phone.setChecked(_model.isPhoneSelected());
		email.setChecked(_model.isEmailSelected());
		
		fullname.setText( parseText(_model.getFullName() ));
		phone.setText(parseText( _model.getPhone()) );
		email.setText( parseText(_model.getEmail() ));
		displayname.setText(parseText( _model.getDisplayName() ));
	}
	
	String parseText( String text )
	{
		if( text == null || text.length()==0)
			return "[Empty]";
		else return text;
	}

	@Override
	protected void onStart(){
		super.onStart();
		_model.restore(this);
		refresh();
	}

	@Override
	protected void onPause(){
		super.onPause();
		_model.save( this );		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}

	public void onEditButton_Click(View v) {

		android.util.Log.i("onEditButton_Click", "ok");

		Intent editDataIntent = new Intent(this, EditDataActivity.class);
		startActivity(editDataIntent);
	}

	public void onShareButton_Click(View v) {

		_model.setFullNameSelected(fullname.isChecked());
		_model.setEmailSelected(email.isChecked());
		_model.setPhoneSelected(phone.isChecked());
		_model.ClearReceivedContacts();
		Intent identifyGroup = new Intent(this, IdentifyGroupActivity.class);

		startActivity(identifyGroup);
	}

}
