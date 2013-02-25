package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class SelectReceiversActivity extends Activity implements NewContactAddedListener {

	int id = 1;

	List<CheckBox> cbList = new ArrayList<CheckBox>();
	SparseArray<String> checkboxid2uuid = new SparseArray<String>();
	CheckBox everythingCheckBox;
	Button refreshButton;
	RelativeLayout rel;

	private ContactViewModel _model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_receivers);
		GroupContactSharingApplication myApp = (GroupContactSharingApplication) getApplication();
		_model = myApp.getModel();
		_model.addEventListener(this);
		new GetDataTask(30).execute(_model);
		Button b = (Button)findViewById(R.id.sendButton);
		b.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
		refreshButton = (Button)findViewById(R.id.refreshButton);

		rel = (RelativeLayout)findViewById(R.id.selectReceiversLayout);
		everythingCheckBox = (CheckBox) findViewById(R.id.nameCheckBox);
		everythingCheckBox.setOnClickListener(everythingHandler);

		addCheckBoxes();
		repositionButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_receivers, menu);
		return true;
	}

	public void onSendButton_Click(View v) {

		ArrayList<String> uuids = new ArrayList<String>();
		for(CheckBox cb : cbList )
		{
			if(cb.isChecked())
			{
				int id = cb.getId();
				String uuid = checkboxid2uuid.get(id);
				uuids.add(uuid);
			}
		}

		if(uuids.size() >0)
		{
			Intent importDataIntent = new Intent(this, ImportDataActivity.class);
			importDataIntent.putStringArrayListExtra("importdata", uuids);
			startActivity(importDataIntent);
		}
	}

	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			for (CheckBox cb : cbList)
				cb.setChecked(b);
		}
	};

	private void addCheckBoxes() {
		//this will most probably be an empty list, but do it anyway
		_model.ForeachContact( new ContactAction() {			
			@Override
			public void run(ContactDetails cd) {
				addCheckBox(cd.displayname,cd.id);

			}
		});
	}

	private void addCheckBox(String text, String uuid) {
		CheckBox newCheckBox = new CheckBox(this);
		newCheckBox.setText(text);
		newCheckBox.setChecked(true);
		newCheckBox.setId(id);

		checkboxid2uuid.put(id, uuid);
		id+=1;

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (cbList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, cbList.get(cbList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_LEFT, everythingCheckBox.getId());
		newCheckBox.setLayoutParams(params);

		cbList.add(newCheckBox);
		rel.addView(newCheckBox);
		repositionButtons();
	}

	private void repositionButtons(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (cbList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, cbList.get(cbList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		params.setMargins(30, 0, 30, 0);

		refreshButton.setLayoutParams(params);
	}

	public void onRefreshButton_Click(View v) {
		new GetDataTask(1).execute(_model);
	}


	@Override
	public void newContactAdded(NewContactEvent e) {
		ContactDetails cd = _model.getContactById(e.getUuid());
		if( cd == null )
			return;
		final String cdname =cd.displayname;
		final String cdid = cd.id;
		Runnable r = new Runnable() {

			@Override
			public void run() {
				addCheckBox(cdname,cdid);

			}
		};

		SelectReceiversActivity.this.runOnUiThread(r);
	}

}
