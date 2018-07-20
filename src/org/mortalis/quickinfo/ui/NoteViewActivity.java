package org.mortalis.quickinfo.ui;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.utils.Fun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class NoteViewActivity extends AppCompatActivity {
  
  private ImageButton bBack;
  private ImageButton bDelete;
  private ImageButton bEdit;
  
  TextView tvContent;
  int noteId = -1;
  boolean infoUpdated = false;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_view);
    
    Log.d("note_view", "onCreate");
    
    tvContent = findViewById(R.id.tvContent);
    
    bBack = findViewById(R.id.bBack);
    bDelete = findViewById(R.id.bDelete);
    bEdit = findViewById(R.id.bEdit);
    
    bBack.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        onBackPressed();
      }
    });
    
    bDelete.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        deleteAction();
      }
    });
    
    bEdit.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        editAction();
      }
    });
    
    TooltipCompat.setTooltipText(bDelete, getString(R.string.button_tooltip_delete_note));
    TooltipCompat.setTooltipText(bEdit, getString(R.string.button_tooltip_edit_note));
    
    loadContent();
  }
  
  @Override
  public void onResume() {
    if(noteId != -1 && !infoUpdated){
      String info = DatabaseManager.getNote(noteId);
      loadInfo(info);
    }
    
    infoUpdated = false;
    super.onResume();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // getMenuInflater().inflate(R.menu.note_view, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(0, 0);
  }
  
  
  private void loadContent() {
    String content = "";
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      content = extras.getString("content");
      noteId = extras.getInt("id");
    }
    
    loadInfo(content);
    infoUpdated = true;
  }
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadInfo(String info) {
    Log.d("note_view", "loadInfo");
    tvContent.setText(info);
  }
  
  public void editAction() {
    Log.d("note_view", "edit-position: " + noteId);
    
    Intent intent = new Intent(this, EditorActivity.class);
    String editorType = "note_edit";
    intent.putExtra("editor_type", editorType);
    intent.putExtra("id", noteId);
    
    startActivity(intent);
    overridePendingTransition(0, 0);
  }
  
  public void deleteAction() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    
    builder.setTitle(R.string.delete_note_confirm);
    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        Fun.logd("Deleting note");
        DatabaseManager.deleteNoteItem(noteId);
        onBackPressed();
      }
    });
    
    AlertDialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(true);
    dialog.show();
  }
  
}
