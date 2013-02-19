package se.kth.ict.id2216.groupcontactsharing;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class ImportDataActivity extends Activity {

	List<Other> otherList = new ArrayList<Other>();
	
	CheckBox everythingCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);

		RelativeLayout rel = (RelativeLayout)findViewById(R.id.importDataLayout);
		everythingCheckBox = (CheckBox) findViewById(R.id.everythingCheckBox);
		everythingCheckBox.setOnClickListener(everythingHandler);
		
		Other o = new Other("name", rel, everythingCheckBox.getId(), everythingCheckBox.getId());
		o.addCheckBox("fb: fbName");
		o.addCheckBox("skype: skypeName");
		o.addCheckBox("whattsapp: wa");
		otherList.add(o);
		
		Other o2 = new Other("name", rel, o.getId(), everythingCheckBox.getId());
		o2.addCheckBox("fb: fbName2");
		o2.addCheckBox("skype: skypeName2");
		o2.addCheckBox("whattsapp: wa2");
		otherList.add(o2);
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
