package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImportDataActivity extends Activity implements ContactUpdatedListener {

	ProgressDialog progressBar;
	ContactViewModel _model;

	ArrayList<String> selectedUuids;
	
	List<Contact> contactList = new ArrayList<Contact>();
	
	RelativeLayout rel;
	CheckBox everythingCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);

		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();
		_model.addUEventListener(this);
		_model.setActive(true);
		if( GetDataTask.TaskCount <= 0 )
			new GetDataTask(30).execute(_model);

		Intent myIntent = getIntent();
		selectedUuids = myIntent.getStringArrayListExtra("importdata"); 

		rel = (RelativeLayout)findViewById(R.id.relativeLayoutImport);
		everythingCheckBox = (CheckBox) findViewById(R.id.everythingCheckBox);
		everythingCheckBox.setOnClickListener(everythingHandler);
		
		createCheckBoxes(selectedUuids);
		createImportButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_import_data, menu);
		return true;
	}

	public void addInitialViews(TextView textView) {
		rel.addView(textView);
		rel.addView(everythingCheckBox);
	}

	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			for (Contact contact : contactList)
				contact.setChecked(b);
		}
	};

	private void createCheckBoxes(ArrayList<String> newUuidList) {
		Map<String, Contact> oldState = null;
		ArrayList<String> uuidList = new ArrayList<String>();
		if (!contactList.isEmpty()) {
			oldState = getOldState();
			uuidList = new ArrayList<String>(oldState.keySet());
			for (String str : newUuidList)  {
				if (selectedUuids.contains(str) && !oldState.containsKey(str))
					uuidList.add(str);
			}
		}
		else
			uuidList.addAll(newUuidList);
		

		TextView textView = (TextView)findViewById(R.id.importTextView);
		rel.removeAllViewsInLayout();
		contactList.clear();
		addInitialViews(textView);
		
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		ContactViewModel _model = myApp.getModel();

		for(String uuid : uuidList) {
			ContactDetails details = _model.getContactById(uuid);

			if (details != null) {
				Contact newContact;
				Contact oldContact = null;
				if (oldState != null)
					oldContact = oldState.get(uuid);

				if (contactList.isEmpty())
					newContact = new Contact(details, oldContact, rel, everythingCheckBox.getId(), everythingCheckBox.getId());
				else
					newContact = new Contact(details, oldContact, rel, contactList.get(contactList.size()-1).getId(), everythingCheckBox.getId());

				contactList.add(newContact);
			}
		}
		createImportButton();
	}

	private Map<String, Contact> getOldState() {
		Map<String, Contact> contactMap = new HashMap<String, Contact>();
		for (Contact c : contactList) {
			contactMap.put(c.getUuid(), c);
		}
		return contactMap;
	}

	private void createImportButton() {
		Button importButton = new Button(this);
		importButton.setText(R.string.import_);
		importButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btn) {
				List<ContactDetails> details = new ArrayList<ContactDetails>();
				for (Contact c : contactList) {
					ContactDetails checkedDetails = c.getDetails();
					if (checkedDetails != null)
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
		importThread.execute(details.toArray(new ContactDetails[0]));
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

	@Override
	public void contactUpdated(ContactUpdatedEvent e) {
		ContactDetails cd = _model.getContactById(e.getUuid());
		if( cd == null )
			return;
		final String cdid = cd.id;
		Runnable r = new Runnable() {

			@Override
			public void run() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(cdid);
				createCheckBoxes(list);

			}
		};

		ImportDataActivity.this.runOnUiThread(r);
	}

}

class ImportThread extends AsyncTask<ContactDetails, Integer, Boolean>{

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
	protected Boolean doInBackground(ContactDetails... details) {

		ContactImporter importer = new ContactImporter(dialog.getContext());

		for (int i = 0; i < details.length; i++) {
			importer.Read(details[i]);

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