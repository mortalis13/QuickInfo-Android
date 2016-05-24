package org.mortalis.quickinfo;

import java.io.File;

import org.mortalis.quickinfo.adds.FileChooser;
import org.mortalis.quickinfo.adds.Functions;
import org.mortalis.quickinfo.adds.FileChooser.FileSelectedListener;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

//public class EditorActivity extends Activity {
public class EditorActivity extends AppCompatActivity {
  
  EditText etEditorText;
  String editor_type = null;
  int position = -1;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);
    
    Log.d("editor", "onCreate");
    
    Toolbar myChildToolbar = (Toolbar) findViewById(R.id.my_child_toolbar);
    setSupportActionBar(myChildToolbar);
    
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setHomeButtonEnabled(true);
    
    etEditorText = (EditText) findViewById(R.id.editor_text);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      editor_type = extras.getString("editor_type");
    }
    
    Log.d("editor", "editor_type: " + editor_type);
    
    if(editor_type != null){
      if(editor_type.equals("personal_info")){
        loadPersonalInfo();
      }
      else if(editor_type.equals("quick_note")){
        loadQuicknoteInfo();
      }
      else if(editor_type.equals("timetable_edit")){
        if (extras != null) {
          position = extras.getInt("position");
          // Log.d("editor", "position: " + position);
          loadTimetableInfo(position);
        }
      }
      else if(editor_type.equals("note_edit")){
        if (extras != null) {
          position = extras.getInt("position");
          loadNoteInfo(position);
        }
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.editor, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    
    switch(id){
    case R.id.action_save:
      saveAction();
      return true;
    case R.id.action_import_text:
      importFileAction();
      return true;
    case android.R.id.home:
      onBackPressed();
//      finish();
//      overridePendingTransition(0, 0);
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

  public void loadPersonalInfo() {
    DatabaseManager db = new DatabaseManager(this);
    String info = db.getPersonalInfo();
    if(info != null){
      etEditorText.setText(info);
    }
    else{
      Log.d("editor", "info-null");
    }
  }

  public void loadQuicknoteInfo() {
    DatabaseManager db = new DatabaseManager(this);
    String info = db.getQuicknoteInfo();
    if(info != null){
      etEditorText.setText(info);
    }
  }

  public void loadTimetableInfo(int position) {
    DatabaseManager db = new DatabaseManager(this);
    String info = db.getTimetable(position);
    if(info != null){
      etEditorText.setText(info);
    }
  }

  public void loadNoteInfo(int position) {
    DatabaseManager db = new DatabaseManager(this);
    String info = db.getNote(position);
    if(info != null){
      etEditorText.setText(info);
    }
  }
    
  public void saveAction() {
    if(editor_type != null){
      String info = etEditorText.getText().toString();
      DatabaseManager db = new DatabaseManager(this);
      
      if(editor_type.equals("personal_info")){
        db.updatePersonalInfo(info);
      }
      else if(editor_type.equals("quick_note")){
        db.updateQuicknoteInfo(info);
      }
      else if(editor_type.equals("timetable_add")){
        db.addTimetableItem(info);
      }
      else if(editor_type.equals("timetable_edit")){
        db.updateTimetableItem(info, position);
      }
      else if(editor_type.equals("note_add")){
        db.addNoteItem(info);
      }
      else if(editor_type.equals("note_edit")){
        db.updateNoteItem(info, position);
      }
      
      // finish();
      onBackPressed();
    }
  }
    
  public void importFileAction() {
    new FileChooser(this).setFileListener( new FileSelectedListener() {
      @Override
      public void fileSelected(File file) {
        String text = Functions.readFile(file);
        if(text != null)
          etEditorText.setText(text);
        
        Log.d("editor", "finishReadFile");
      }
    }).showDialog();
  }
  
}
