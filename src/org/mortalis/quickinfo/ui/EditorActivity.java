package org.mortalis.quickinfo.ui;

import java.io.File;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.utils.FileChooser;
import org.mortalis.quickinfo.utils.FileChooser.FileSelectedListener;
import org.mortalis.quickinfo.utils.Fun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditorActivity extends AppCompatActivity {
  
  private ImageButton bBack;
  private ImageButton bSave;
  private ImageButton bImportText;
  
  EditText etEditorText;
  String editor_type = null;
  int noteId = -1;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);
    
    Log.d("editor", "onCreate");
    
    etEditorText = findViewById(R.id.editor_text);
    
    bBack = findViewById(R.id.bBack);
    bSave = findViewById(R.id.bSave);
    bImportText = findViewById(R.id.bImportText);
    
    
    bBack.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        onBackPressed();
      }
    });
    
    bSave.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        saveAction();
      }
    });
    
    bImportText.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        importFileAction();
      }
    });
    
    TooltipCompat.setTooltipText(bSave, getString(R.string.button_tooltip_save_note));
    TooltipCompat.setTooltipText(bImportText, getString(R.string.button_tooltip_import_text));
    
    loadContent();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // getMenuInflater().inflate(R.menu.editor, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id){
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(0, 0);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  private void loadContent() {
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
      else if(editor_type.equals("note_edit")){
        if (extras != null) {
          noteId = extras.getInt("id");
          loadNoteInfo(noteId);
        }
      }
    }
  }
  
  public void loadPersonalInfo() {
    String info = DatabaseManager.getPersonalInfo();
    if(info != null){
      etEditorText.setText(info);
    }
    else{
      Log.d("editor", "info-null");
    }
  }

  public void loadQuicknoteInfo() {
    String info = DatabaseManager.getQuicknoteInfo();
    if(info != null){
      etEditorText.setText(info);
    }
  }

  public void loadNoteInfo(int id) {
    String info = DatabaseManager.getNote(id);
    if(info != null){
      etEditorText.setText(info);
    }
  }
    
  public void saveAction() {
    if(editor_type != null){
      String info = etEditorText.getText().toString();
      
      if(editor_type.equals("personal_info")){
        DatabaseManager.updatePersonalInfo(info);
      }
      else if(editor_type.equals("quick_note")){
        DatabaseManager.updateQuicknoteInfo(info);
      }
      else if(editor_type.equals("note_add")){
        DatabaseManager.addNoteItem(info);
      }
      else if(editor_type.equals("note_edit")){
        DatabaseManager.updateNoteItem(info, noteId);
      }
      
      // finish();
      onBackPressed();
    }
  }
    
  public void importFileAction() {
    new FileChooser(this).setFileListener(new FileSelectedListener() {
      @Override
      public void fileSelected(File file) {
        String text = Fun.readFile(file);
        if(text != null)
          etEditorText.setText(text);
        
        Log.d("editor", "finishReadFile");
      }
    }).showDialog();
  }
  
}
