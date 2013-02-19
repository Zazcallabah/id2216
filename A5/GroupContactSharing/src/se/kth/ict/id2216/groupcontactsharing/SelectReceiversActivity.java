package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class SelectReceiversActivity extends Activity {

	CheckBox everythingCheckBox;
	CheckBox other1CheckBox;
	CheckBox other2CheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_receivers);
		
		everythingCheckBox = (CheckBox) findViewById(R.id.nameCheckBox);
		other1CheckBox = (CheckBox) findViewById(R.id.emailCheckBox);
		other2CheckBox = (CheckBox) findViewById(R.id.phoneCheckBox);

		everythingCheckBox.setOnClickListener(everythingHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_receivers, menu);
		return true;
	}

	public void onSendButton_Click(View v) {

		Intent importDataIntent = new Intent(this, ImportDataActivity.class);
		startActivity(importDataIntent);
	}

	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			other1CheckBox.setChecked(b);
			other2CheckBox.setChecked(b);
		}
	};

}
