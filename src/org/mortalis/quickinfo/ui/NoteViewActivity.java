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
import android.widget.Button;
import android.view.LayoutInflater;


public class NoteViewActivity extends AppCompatActivity {
  
  private ImageButton bBack;
  private ImageButton bDelete;
  
  private TextView noteContent;
  
  private int noteId = -1;
  private boolean infoUpdated;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_view);
    
    Fun.logd("onCreate");
    
    noteContent = findViewById(R.id.noteContent);
    
    bBack = findViewById(R.id.bBack);
    bDelete = findViewById(R.id.bDelete);
    
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
    
    noteContent.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        editAction();
      }
    });
    
    TooltipCompat.setTooltipText(bDelete, getString(R.string.button_tooltip_delete_note));
    
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
    noteContent.setText(info);
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
    int layoutId = R.layout.dialog_confirm_delete;
    
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(layoutId, null);
    builder.setView(view);
    
    Button bOk = view.findViewById(R.id.bOk);
    bOk.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
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
