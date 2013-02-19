package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditDataActivity extends Activity {

	

	private ContactViewModel _model;
private EditText email;
private EditText phone;
private EditText name;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();

		setContentView(R.layout.activity_edit_data);
		 email =(EditText)findViewById(R.id.emailEditText); 

		 phone =(EditText)findViewById(R.id.phoneEditText); 
		 name =(EditText)findViewById(R.id.nameEditText); 
	    email.setText(_model.getEmail());
	    name.setText(_model.getName());
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
		
		Editable n = name.getText();
		String tn = n.toString();
		_model.setName(tn);
		Editable e = email.getText();
		String te = e.toString();
		_model.setEmail(te);
		Editable p = phone.getText();
		String tp = p.toString();
		_model.setPhone(tp);
		_model.save(this);
		this.finish();
	}
	public void onCancelButton_Click(View v) {

		android.util.Log.i("onCancelButton_Click", "ok");
		
		this.finish();
	}

}
