package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class StartActivity extends Activity /* implements OnClickListener */{

	private ContactViewModel _model;
private CheckBox email;
private CheckBox name;
private CheckBox phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();
 
		setContentView(R.layout.activity_start);
		
		email =(CheckBox)findViewById(R.id.emailCheckBox); 
		 phone =(CheckBox)findViewById(R.id.phoneCheckBox); 
		 name =(CheckBox)findViewById(R.id.nameCheckBox); 

	}
	void refresh ()
	{
		name.setChecked(_model.isNameSelected());
		phone.setChecked(_model.isPhoneSelected());
		email.setChecked(_model.isEmailSelected());
		name.setText( _model.getName() );
		phone.setText( _model.getPhone() );
		email.setText( _model.getEmail() );
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

		_model.setNameSelected(name.isChecked());
		_model.setEmailSelected(email.isChecked());
		_model.setPhoneSelected(phone.isChecked());
		Intent identifyGroup = new Intent(this, IdentifyGroupActivity.class);
	
		startActivity(identifyGroup);
	}

}
