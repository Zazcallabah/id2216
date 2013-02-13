package se.kth.ict.id2216.groupcontactsharing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ImportDataActivity extends Activity {

	CheckBox everythingCheckBox;
	CheckBox other1CheckBox;
	CheckBox other2CheckBox;
	CheckBox other1nameCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);

		everythingCheckBox = (CheckBox) findViewById(R.id.everythingCheckBox);
		other1CheckBox = (CheckBox) findViewById(R.id.other1CheckBox);
		other2CheckBox = (CheckBox) findViewById(R.id.phoneCheckBox);
		other1nameCheckBox = (CheckBox) findViewById(R.id.other1nameCheckBox);

		everythingCheckBox.setOnClickListener(everythingHandler);
		other1CheckBox.setOnClickListener(other1Handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_import_data, menu);
		return true;
	}

	public void onImportButton_Click(View v) {

		// add new checkboxes
		RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.importDataLayout);
		Button bt = new Button(this);
		bt.setText("A Button");
		bt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		linearLayout.addView(bt);
	}
	
	View.OnClickListener everythingHandler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			other1CheckBox.setChecked(b);
			other1nameCheckBox.setChecked(b);
			other2CheckBox.setChecked(b);
		}
	};

	View.OnClickListener other1Handler = new View.OnClickListener() {
		public void onClick(View v) {
			boolean b = ((CheckBox) v).isChecked();
			other1nameCheckBox.setChecked(b);
		}
	};

}
