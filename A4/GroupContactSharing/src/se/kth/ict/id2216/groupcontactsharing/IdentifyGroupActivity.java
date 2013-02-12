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

 CheckBox chkBox1;
 CheckBox chkBox2;
 CheckBox chkBox3;
 CheckBox chkBox4;
 CheckBox chkBox5;
 
 private boolean[] checkBoxesChecked = new boolean[5];
 
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_identify_group);
  
  chkBox1 = (CheckBox) findViewById(R.id.checkBox1);
  chkBox2 = (CheckBox) findViewById(R.id.checkBox2);
  chkBox3 = (CheckBox) findViewById(R.id.checkBox3);
  chkBox4 = (CheckBox) findViewById(R.id.checkBox4);
  chkBox5 = (CheckBox) findViewById(R.id.checkBox5);
  
  chkBox1.setOnClickListener(myCheckBoxHandler);
  chkBox2.setOnClickListener(myCheckBoxHandler);
  chkBox3.setOnClickListener(myCheckBoxHandler);
  chkBox4.setOnClickListener(myCheckBoxHandler);
  chkBox5.setOnClickListener(myCheckBoxHandler);
 }
 
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.activity_identify_group, menu);
  return true;
 }

 public void onJoinGroupButton_Click(View v) {

  android.util.Log.i("onJoinGroupButton_Click", "ok");
  
  int count = 0;
  for (boolean b : checkBoxesChecked) {
   if (b)
    count++;
  }
  
  if (count >= 1) {
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
 
 View.OnClickListener myCheckBoxHandler = new View.OnClickListener() {
    public void onClick(View v) {
     int id = -1;
        if( chkBox1.getId() == ((CheckBox)v).getId() ){
            id = 1;
        }
        else if( chkBox2.getId() == ((CheckBox)v).getId() ){
            id = 2;
        }
        else if( chkBox3.getId() == ((CheckBox)v).getId() ){
            id = 3;
        }
        else if( chkBox4.getId() == ((CheckBox)v).getId() ){
            id = 4;
        }
        else if( chkBox5.getId() == ((CheckBox)v).getId() ){
            id = 5;
        }
        else {
         id = -1;
        }
        
        if (id != -1) {
         if (((CheckBox) v).isChecked()) {
          checkBoxesChecked[id-1] = true;
         }
         else {
          checkBoxesChecked[id-1] = false;
         }
        }
    }
  };

}