package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectReceiversActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_receivers);
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

}
