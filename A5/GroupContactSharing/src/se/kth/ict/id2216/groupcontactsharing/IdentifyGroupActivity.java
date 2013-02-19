package se.kth.ict.id2216.groupcontactsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class IdentifyGroupActivity extends Activity {

	private CheckBox[] checkBoxes;
	private ContactViewModel _model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_group);

		checkBoxes = new CheckBox[] { (CheckBox) findViewById(R.id.checkBox1),
				(CheckBox) findViewById(R.id.checkBox2),
				(CheckBox) findViewById(R.id.checkBox3),
				(CheckBox) findViewById(R.id.checkBox4),
				(CheckBox) findViewById(R.id.checkBox5)};

		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_identify_group, menu);
		return true;
	}

	public void onJoinGroupButton_Click(View v) {

		android.util.Log.i("onJoinGroupButton_Click", "ok");

		String label = "";
		int count = 0;
		for (CheckBox b : checkBoxes) {
			if (b.isChecked())
			{
				count++;
				label+=b.getId();
			}
		}
		if (count >= 1) {
			_model.setLabel(label);
			new PostDataTask().execute(_model);
			Intent excludeReceivers = new Intent(this, SelectReceiversActivity.class);
			startActivity(excludeReceivers);
		}
		else {
			new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage("Select at least one picture!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
	}

	public void onQuestionButton_Click(View v) {
		new AlertDialog.Builder(this)
		.setTitle("Description")
		.setMessage("Choose a combination of the shown pictures as your group's identification and then select the according pictures before clicking on the 'Join Group' Button.")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
			}
		})
		.show();
	}


}