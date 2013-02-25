package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class ImportDataActivity extends Activity {

	List<Other> otherList = new ArrayList<Other>();

	RelativeLayout rel;
	CheckBox everythingCheckBox;

	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);

		Intent myIntent = getIntent();
		ArrayList<String> uuidList = myIntent.getStringArrayListExtra("importdata"); 

		rel = (RelativeLayout)findViewById(R.id.importDataLayout);
		everythingCheckBox = (CheckBox) findViewById(R.id.everythingCheckBox);
		everythingCheckBox.setOnClickListener(everythingHandler);

		createCheckBoxes(uuidList);
		createImportButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_import_data, menu);
		return true;
	}

	private void createCheckBoxes(ArrayList<String> uuidList) {
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		ContactViewModel _model = myApp.getModel();
		for(String uuid : uuidList) {
			ContactDetails details = _model.getContactById(uuid);
			if (details != null) {
				Other newOther;
				if (otherList.isEmpty())
					newOther = new Other(details.name, rel, everythingCheckBox.getId(), everythingCheckBox.getId());
				else
					newOther = new Other(details.name, rel, otherList.get(otherList.size()-1).getId(), everythingCheckBox.getId());
				if (details.phone != null && !details.phone.equals(""))
					newOther.addCheckBox("Phone: " + details.phone);
				if (details.email != null && !details.email.equals(""))
					newOther.addCheckBox("E-Mail: " + details.email);
				otherList.add(newOther);
			}
		}
	}

	private void createImportButton() {
		Button importButton = new Button(this);
		importButton.setText(R.string.import_);
		importButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View btn) {
				showProgressBar();
			}
		});

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (otherList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, otherList.get(otherList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		params.setMargins(30, 0, 30, 0);

		importButton.setLayoutParams(params);
		rel.addView(importButton);
	}

	private void showProgressBar() {
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Importing contact details ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		progressBarStatus = 0;

		new Thread(new Runnable() {
			public void run() {
				long startTime =  System.currentTimeMillis();
				while (progressBarStatus < 100) {
					progressBarStatus = Math.round((System.currentTimeMillis() - startTime)/10);

					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
						}
					});
				}

				if (progressBarStatus >= 100) {

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					progressBar.dismiss();
					goBackToStart();

				}
			}
		}).start();
	}

	public void goBackToStart(){
		Intent startIntent = new Intent(this, StartActivity.class);
		startActivity(startIntent);
	}

	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			for (Other other : otherList)
				other.setChecked(b);

		}
	};

}

class Other implements View.OnClickListener{

	private static int id = 1;

	private RelativeLayout rel;
	private CheckBox cbName;

	private List<CheckBox> cbList = new ArrayList<CheckBox>();

	public Other(String name, RelativeLayout rel, int relativeToBelow, int relativeToLeft) {
		this.rel = rel;
		cbName = new CheckBox(rel.getContext());
		cbName.setText(name);
		cbName.setChecked(true);
		cbName.setId(id++);
		cbName.setOnClickListener(this);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, relativeToBelow);
		params.addRule(RelativeLayout.ALIGN_LEFT, relativeToLeft);
		cbName.setLayoutParams(params);

		rel.addView(cbName);
	}

	@Override
	public void onClick(View v) {
		boolean b = ((CheckBox) v).isChecked();
		setChecked(b);
	}

	public void addCheckBox(String text) {
		CheckBox newCheckBox = new CheckBox(rel.getContext());
		newCheckBox.setText(text);
		newCheckBox.setId(id++);
		newCheckBox.setChecked(true);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (cbList.isEmpty()) {
			params.addRule(RelativeLayout.BELOW, cbName.getId());
			params.addRule(RelativeLayout.ALIGN_LEFT, cbName.getId());
			params.setMargins(30, 0, 0, 0);
		}
		else {
			CheckBox relTo = cbList.get(cbList.size()-1);
			params.addRule(RelativeLayout.BELOW, relTo.getId());
			params.addRule(RelativeLayout.ALIGN_LEFT, relTo.getId());
		}
		newCheckBox.setLayoutParams(params);

		cbList.add(newCheckBox);
		rel.addView(newCheckBox);
	}

	public void setChecked(boolean b) {
		cbName.setChecked(b);
		for (CheckBox cb : cbList)
			cb.setChecked(b);
	}

	public int getId() {
		return id-1;
	}

}
