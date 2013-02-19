package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class SelectReceiversActivity extends Activity implements OnClickListener {

	int id = 1;

	List<CheckBox> cbList = new ArrayList<CheckBox>();

	CheckBox everythingCheckBox;
	Button sendButton;
	RelativeLayout rel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_receivers);

		rel = (RelativeLayout)findViewById(R.id.selectReceiversLayout);
		everythingCheckBox = (CheckBox) findViewById(R.id.nameCheckBox);
		everythingCheckBox.setOnClickListener(everythingHandler);

		addCheckBoxes();
		createSendButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_receivers, menu);
		return true;
	}

	public void onClick(View btn) {
		if (btn.equals(sendButton)) {
			Intent importDataIntent = new Intent(this, ImportDataActivity.class);
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
		addCheckBox("name1");
		addCheckBox("name2");
	}

	private void addCheckBox(String text) {
		CheckBox newCheckBox = new CheckBox(this);
		newCheckBox.setText(text);
		newCheckBox.setChecked(true);
		newCheckBox.setId(id++);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (cbList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, cbList.get(cbList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_LEFT, everythingCheckBox.getId());
		newCheckBox.setLayoutParams(params);

		cbList.add(newCheckBox);
		rel.addView(newCheckBox);
	}

	private void createSendButton() {
		sendButton = new Button(this);
		sendButton.setText(R.string.import_);
		sendButton.setOnClickListener(this);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (cbList.isEmpty())
			params.addRule(RelativeLayout.BELOW, everythingCheckBox.getId());
		else
			params.addRule(RelativeLayout.BELOW, cbList.get(cbList.size()-1).getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		params.setMargins(30, 0, 30, 0);

		sendButton.setLayoutParams(params);
		rel.addView(sendButton);
	}

}
