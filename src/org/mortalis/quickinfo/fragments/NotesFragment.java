package org.mortalis.quickinfo.fragments;

import java.util.ArrayList;
import java.util.List;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.adapters.NotesListAdapter;
import org.mortalis.quickinfo.model.NoteListItem;
import org.mortalis.quickinfo.model.NoteModel;
import org.mortalis.quickinfo.ui.EditorActivity;
import org.mortalis.quickinfo.ui.NoteViewActivity;
import org.mortalis.quickinfo.utils.Vars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class NotesFragment extends PageFragment {
  
  public static final String FRAGMENT_TAG = "NotesFragment";
  
  private Context context;
  private Activity activity;
  
  private ListView lvItems;
  
  private boolean infoUpdated = false;
  
  
  public NotesFragment() {
  }
  
  public NotesFragment(Context context) {
    this.context = context;
  }
  
  public static NotesFragment newInstance(Context context) {
    NotesFragment fragment = new NotesFragment(context);
    Bundle args = new Bundle();
    args.putString(Vars.FRAGMENT_TAG_ARG, FRAGMENT_TAG);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.notes_view, container, false);
    lvItems = rootView.findViewById(R.id.lvItems);
    
    lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteListItem item = (NoteListItem) parent.getItemAtPosition(position);
        int noteId = item.getId();
        String info = DatabaseManager.getNote(noteId);
        showNoteInfo(info, noteId);
      }
    });
    
    loadData();
    infoUpdated = true;
    
    return rootView;
  }
  
  @Override
  public void onResume() {
    loadData();
    infoUpdated = false;
    
    super.onResume();
  }
  
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity == null) return;
    this.context = activity;
    this.activity = activity;
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context == null) return;
    this.context = context;
  }
  
  
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // inflater.inflate(R.menu.notes, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
    case R.id.action_add:
      addAction();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  @Override
  public void loadData() {
    if (infoUpdated) return;
    
    List<NoteModel> noteModeList = DatabaseManager.getNotes();
    List<NoteListItem> notesList = new ArrayList<>();
    for (NoteModel model: noteModeList) {
      notesList.add(new NoteListItem(model.getId(), model.getInfo()));
    }
    
    loadInfo(notesList);
  }
  
  @Override
  public void executeMainAction() {
    addAction();
  }
  
  
  public void showNoteInfo(String info, int id) {
    Intent intent = new Intent(context, NoteViewActivity.class);
    intent.putExtra("content", info);
    intent.putExtra("id", id);
    startActivity(intent);
    if (activity != null) activity.overridePendingTransition(0, 0);
  }
  
  public void loadInfo(List<NoteListItem> notesList) {
    int listLayout = R.layout.notes_list_item;
    ListAdapter adapter = new NotesListAdapter(context, notesList);
    lvItems.setAdapter(adapter);
  }
  
  public void addAction() {
    Intent intent = new Intent(context, EditorActivity.class);
    String editorType = "note_add";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    if (activity != null) activity.overridePendingTransition(0, 0);
  }
  
}
