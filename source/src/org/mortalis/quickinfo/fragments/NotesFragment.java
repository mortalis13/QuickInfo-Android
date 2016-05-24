package org.mortalis.quickinfo.fragments;

import java.util.List;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.EditorActivity;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.R.id;
import org.mortalis.quickinfo.R.layout;
import org.mortalis.quickinfo.R.menu;
import org.mortalis.quickinfo.views.NoteViewActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NotesFragment extends Fragment {
  
  ListView lvItems;
  boolean infoUpdated = false;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.notes_view, container, false);
    getActivity().invalidateOptionsMenu();
    
    lvItems = (ListView) rootView.findViewById(R.id.lvItems);
    
    lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DatabaseManager db = new DatabaseManager(getActivity());
        String info = db.getNote(position);
        showNoteInfo(info, position);
      }
    });
    
    loadNotes();
    infoUpdated = true;
    
    return rootView;
  }
  
  @Override
  public void onResume() {
    Log.d("main", "onResume");
    Log.d("main", "onResume::infoUpdated: " + infoUpdated);
    
    loadNotes();
    infoUpdated = false;
    
    super.onResume();
  }
  
  
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.notes, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id){
    case R.id.action_settings:
      return true;
    case R.id.action_add:
      addAction();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadNotes() {
    if(infoUpdated) return;
    
    DatabaseManager db = new DatabaseManager(getActivity());
    List<String> info = db.getNotes();
    if(info != null){
      loadInfo(info);
    }
  }
  
  public void showNoteInfo(String info, int position) {
    Intent intent = new Intent(getActivity(), NoteViewActivity.class);
    intent.putExtra("content", info);
    intent.putExtra("position", position);
    startActivity(intent);
    getActivity().overridePendingTransition(0, 0);
  }
  
  public void loadInfo(List<String> info) {
    Context context = getActivity();
    int listLayout = R.layout.notes_list_item;
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, listLayout, info);
    
    // ArrayAdapter<String> adapter = new ArrayAdapter<String>(
    //   context, android.R.layout.simple_list_item_activated_1, android.R.id.text1, info
    // );
    
    lvItems.setAdapter(adapter);
  }
  
  public void addAction() {
    Intent intent = new Intent(getActivity(), EditorActivity.class);
    String editorType = "note_add";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    getActivity().overridePendingTransition(0, 0);
  }

}
