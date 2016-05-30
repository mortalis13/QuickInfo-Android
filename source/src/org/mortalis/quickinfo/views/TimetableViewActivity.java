package org.mortalis.quickinfo.views;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.EditorActivity;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.R.id;
import org.mortalis.quickinfo.R.layout;
import org.mortalis.quickinfo.R.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TimetableViewActivity extends AppCompatActivity {
  
  TextView tvContent;
  int position = -1;
  boolean infoUpdated = false;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timetable_view);
    
    Log.d("timetable_view", "onCreate");
    
    Toolbar myChildToolbar = (Toolbar) findViewById(R.id.my_child_toolbar);
    setSupportActionBar(myChildToolbar);
    
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setHomeButtonEnabled(true);
    
    tvContent = (TextView) findViewById(R.id.tvContent);
    
    String content = "";
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      content = extras.getString("content");
      position = extras.getInt("position");
    }
    
    // tvContent.setText(content);
    loadInfo(content);
    infoUpdated = true;
  }
  
  @Override
  public void onResume() {
    // Log.d("timetable_view", "onResume");
    // Log.d("timetable_view", "Timetable::onResume::position: " + position);
    
    if(position != -1 && !infoUpdated){
      DatabaseManager db = new DatabaseManager(this);
      String info = db.getTimetable(position);

      loadInfo(info);
    }
    
    infoUpdated = false;
    super.onResume();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.timetable_view, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id){
    case R.id.action_edit:
      editAction();
      return true;
    case R.id.action_delete:
      deleteAction();
      return true;
    case android.R.id.home:
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(0, 0);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadInfo(String info) {
    if(infoUpdated) return;
    Log.d("timetable_view", "loadInfo");
    tvContent.setText(info);
  }
  
  public void editAction() {
    Log.d("timetable_view", "edit-position: " + position);
    
    Intent intent = new Intent(this, EditorActivity.class);
    String editorType = "timetable_edit";
    intent.putExtra("editor_type", editorType);
    intent.putExtra("position", position);
    
    startActivity(intent);
    overridePendingTransition(0, 0);
    
    infoUpdated = false;
  }
  
  public void deleteAction() {
    DatabaseManager db = new DatabaseManager(this);
    db.deleteTimetableItem(position);
    onBackPressed();
    // finish();
  }
  
}
