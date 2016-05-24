package org.mortalis.quickinfo.fragments;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.EditorActivity;
import org.mortalis.quickinfo.R;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PersonalInfoFragment extends Fragment {
  
  WebView wvContent;
  TextView tvContent;
  ListView lvItems;
  
  View rootView;

  boolean infoUpdated = false;
    

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.personal_info, container, false);
    
    // getActivity().invalidateOptionsMenu();
    
    // Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
    // ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
    
    // myToolbar.setNavigationIcon(R.drawable.ic_drawer);
    // myToolbar.invalidate();
    
    // ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    // actionBar.setDisplayHomeAsUpEnabled(true);
    // actionBar.setHomeButtonEnabled(true);
    
    // actionBar.setIcon(R.drawable.ic_drawer);
    
    lvItems = (ListView) rootView.findViewById(R.id.lvItems);
    
    loadPersonalInfo();
    infoUpdated = true;
    
    lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        String item = (String) parent.getItemAtPosition(position);
//        item = item.trim();
//        toast("Copied: "+item);
        return false;
      }
      
    });
    lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
      
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        item = item.trim();
        copyClipboard(item);
        toast("Copied: "+item);
      }
    });
        
    return rootView;
  }
  
  @Override
  public void onResume() {
    Log.d("main", "onResume");
    Log.d("main", "onResume::infoUpdated: " + infoUpdated);
    
    loadPersonalInfo();
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
    case R.id.action_settings:
      return true;
    case R.id.action_edit:
      editAction();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  public void loadPersonalInfo() {
    if(infoUpdated) return;
    
    DatabaseManager db = new DatabaseManager(getActivity());
    String info = db.getPersonalInfo();
    if(info != null){
      loadInfo(info);
    }
    else{
      Log.d("main", "info-null");
    }
  }
  
  public void loadInfo(String info) {
    String[] listData = info.split("\n");
    int listLayout = R.layout.data_list_item;
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), listLayout, listData);
    lvItems.setAdapter(adapter);
  }
  
  public void editAction() {
    Intent intent = new Intent(getActivity(), EditorActivity.class);
    String editorType = "personal_info";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    getActivity().overridePendingTransition(0, 0);
  }
  
  
// ------------------------------------------------ Service ------------------------------------------------
  
  @SuppressWarnings("deprecation")
  public void copyClipboard(String text){
    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
      android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
      clipboard.setText(text);
    } 
    else {
      android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
      android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
      clipboard.setPrimaryClip(clip);
    }
    
    // toast("\"" + text + "\" copied");
  }
  
  public void toast(String msg){
    int duration = Toast.LENGTH_LONG;
    Toast toast = Toast.makeText(getActivity(), msg, duration);
    toast.show();
  }  
  
}
