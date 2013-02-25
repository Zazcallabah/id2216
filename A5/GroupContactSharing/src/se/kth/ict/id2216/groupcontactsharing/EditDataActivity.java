package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditDataActivity extends Activity {

	private ContactViewModel _model;
	private EditText email;
	private EditText phone;
	private EditText name;
	private EditText fullname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();

		setContentView(R.layout.activity_edit_data);
		email =(EditText)findViewById(R.id.emailEditText); 
		Button b = (Button)findViewById(R.id.saveButton);
		b.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
	
		phone =(EditText)findViewById(R.id.phoneEditText); 
		name =(EditText)findViewById(R.id.nameEditText); 
		fullname =(EditText)findViewById(R.id.fullnameEditText); 
		email.setText(_model.getEmail());
		name.setText(_model.getDisplayName());
		fullname.setText(_model.getFullName());
		phone.setText(_model.getPhone());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_edit_data, menu);
		return true;
	}

	public void onStoreButton_Click(View v) {

		android.util.Log.i("onStoreButton_Click", "ok");

		_model.setDisplayName(name.getText().toString());
		_model.setFullName(fullname.getText().toString());
		_model.setEmail( email.getText().toString() );
		_model.setPhone(phone.getText().toString());
		_model.save(this);
		this.finish();
	}
	
	public void onCancelButton_Click(View v) {
		android.util.Log.i("onCancelButton_Click", "ok");
		this.finish();
	}
}
