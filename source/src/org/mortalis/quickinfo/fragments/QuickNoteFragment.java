package org.mortalis.quickinfo.fragments;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.EditorActivity;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.R.id;
import org.mortalis.quickinfo.R.layout;
import org.mortalis.quickinfo.R.menu;

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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class QuickNoteFragment extends Fragment {
  
  WebView wvContent;
  TextView tvContent;
  
  View rootView;

  boolean infoUpdated = false;
    

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.quicknote_view, container, false);
    
    tvContent = (TextView) rootView.findViewById(R.id.tvContent);
    
    loadQuickNoteInfo();
    infoUpdated = true;
    
    return rootView;
  }
  
  @Override
  public void onResume() {
    Log.d("main", "onResume");
    Log.d("main", "onResume::infoUpdated: " + infoUpdated);
    
    loadQuickNoteInfo();
    infoUpdated = false;
    
    super.onResume();
  }
  
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.plain_info, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    
    int id = item.getItemId();
    switch(id){
    case R.id.action_edit:
      editAction();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadQuickNoteInfo() {
    if(infoUpdated) return;
    
    DatabaseManager db = new DatabaseManager(getActivity());
    String info = db.getQuicknoteInfo();
    if(info != null){
      loadInfo(info);
    }
  }
  
  public void loadInfo(String info) {
    tvContent.setText(info);
  }
  
  public void editAction() {
    Intent intent = new Intent(getActivity(), EditorActivity.class);
    String editorType = "quick_note";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    getActivity().overridePendingTransition(0, 0);
  }
  
}
