package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class IdentifyGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_identify_group, menu);
		return true;
	}

	public void onJoinGroupButton_Click(View v) {

		android.util.Log.i("onJoinGroupButton_Click", "ok");
		
		Intent excludeReceivers = new Intent(this, SelectReceiversActivity.class);
		startActivity(excludeReceivers);
	}

}