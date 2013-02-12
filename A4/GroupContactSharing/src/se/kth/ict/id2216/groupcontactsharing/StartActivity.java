package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;

public class StartActivity extends Activity /* implements OnClickListener */{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// Button editButton = (Button) this.findViewById(R.id.editButton);
		// editButton.setOnClickListener(this);
		//
		// Button shareButton = (Button) this.findViewById(R.id.shareButton);
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

		android.util.Log.i("onShareButton_Click", "ok");

		Intent identifyGroup = new Intent(this, IdentifyGroupActivity.class);
		startActivity(identifyGroup);
	}

}
