package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class ImportDataActivity extends Activity {

	ProgressDialog progressBar;

	List<Contact> contactList = new ArrayList<Contact>();

	RelativeLayout rel;
	CheckBox everythingCheckBox;

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

	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			for (Contact contact : contactList)
				contact.setChecked(b);

		}
	};

	private void createCheckBoxes(ArrayList<String> uuidList) {
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		ContactViewModel _model = myApp.getModel();

		for(String uuid : uuidList) {
			ContactDetails details = _model.getContactById(uuid);

			if (details != null) {
				Contact newContact;

				if (contactList.isEmpty())
					newContact = new Contact(details, rel, everythingCheckBox.getId(), everythingCheckBox.getId());
				else
					newContact = new Contact(details, rel, contactList.get(contactList.size()-1).getId(), everythingCheckBox.getId());

				contactList.add(newContact);
			}
		}
	}

	private void createImportButton() {
		Button importButton = new Button(this);
		importButton.setText(R.string.import_);
		importButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btn) {
				List<ContactDetails> details = new ArrayList<ContactDetails>();
				for (Contact o : contactList) {
					ContactDetails checkedDetails = o.getDetails();
					details.add(checkedDetails);
				}
				importData(details);
			}
		});

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (contactList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, contactList.get(contactList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		params.setMargins(30, 0, 30, 0);

		importButton.setLayoutParams(params);
		importButton.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		rel.addView(importButton);
	}

	private void importData(List<ContactDetails> details) {
		ProgressDialog dialog = createProgressDialog(details.size());
		ImportThread importThread = new ImportThread(dialog);

		// TODO look into this warning
		importThread.execute(details);
	}

	protected ProgressDialog createProgressDialog(Integer max) {
		progressBar = new ProgressDialog(this);
		progressBar.setMessage("Importing contact details ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(max);
		progressBar.setCancelable(true);

		return progressBar;
	}

}

class ImportThread extends AsyncTask<List<ContactDetails>, Integer, Boolean>{

	private ProgressDialog dialog;

	public ImportThread(ProgressDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.show();
	}


	@Override
	protected Boolean doInBackground(List<ContactDetails>... params) {
		List<ContactDetails> details = params[0];

		ContactImporter importer = new ContactImporter(dialog.getContext());

		for (int i = 0; i < details.size(); i++) {
			importer.Read(details.get(i));

			publishProgress(i+1);
		}

		return true;
	}

	protected void onProgressUpdate(Integer... progress) {
		dialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(Boolean b) {
		if (b) {}
		// Success
		else {}
		// Failed
		dialog.dismiss();

		Intent startIntent = new Intent(dialog.getContext(), StartActivity.class);
		dialog.getContext().startActivity(startIntent);
	}

}