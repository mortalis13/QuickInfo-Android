package org.mortalis.quickinfo.ui;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.utils.Fun;
import org.mortalis.quickinfo.utils.Vars;

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
  
  private TextView tvContent;
  
  private int noteId = -1;
  private boolean infoUpdated;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_view);
    
    Fun.logd("onCreate");
    
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
    if (noteId != -1 && !infoUpdated) {
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
      content = extras.getString(Vars.EXTRA_CONTENT);
      noteId = extras.getInt(Vars.EXTRA_ID);
    }
    
    loadInfo(content);
    infoUpdated = true;
  }
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadInfo(String info) {
    Fun.logd("loadInfo");
    tvContent.setText(info);
  }
  
  public void editAction() {
    Fun.logd("edit-position: " + noteId);
    
    Intent intent = new Intent(this, EditorActivity.class);
    String editorType = Vars.PROP_EDITOR_TYPE_NOTE_EDIT;
    intent.putExtra(Vars.EXTRA_EDITOR_TYPE, editorType);
    intent.putExtra(Vars.EXTRA_ID, noteId);
    
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
