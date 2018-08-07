package org.mortalis.quickinfo.fragments;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.ui.EditorActivity;
import org.mortalis.quickinfo.utils.Fun;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class InfoFragment extends PageFragment {
  
  public static final String FRAGMENT_TAG = "InfoFragment";
  
  private Context context;
  private Activity activity;
  
  private TextView tvContent;
  private ListView lvItems;
  
  private View rootView;
  
  private boolean infoUpdated = false;
  
  
  public static InfoFragment newInstance(Context context) {
    InfoFragment fragment = new InfoFragment();
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
    rootView = inflater.inflate(R.layout.personal_info, container, false);
    
    lvItems = (ListView) rootView.findViewById(R.id.lvItems);
    
    loadData();
    infoUpdated = true;
    
    lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        item = item.trim();
        copyClipboard(item);
        Fun.toast(context, "Copied: " + item);
      }
    });
    
    return rootView;
  }
  
  @Override
  public void onResume() {
    Fun.logd("InfoFragment.onResume()");
    
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
    // inflater.inflate(R.menu.plain_info, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
    case R.id.action_edit:
      editAction();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  
// ------------------------------------------------ Actions ------------------------------------------------
  
  @Override
  public void loadData() {
    if (infoUpdated) return;
    
    String info = DatabaseManager.getPersonalInfo();
    
    if (info != null) {
      loadInfo(info);
    }
    else {
      Fun.loge("info null");
    }
  }
  
  @Override
  public void executeMainAction() {
    editAction();
  }
  
  public void loadInfo(String info) {
    String[] listData = info.split("\n");
    int listLayout = R.layout.data_list_item;
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, listLayout, listData);
    lvItems.setAdapter(adapter);
  }
  
  public void editAction() {
    Intent intent = new Intent(context, EditorActivity.class);
    String editorType = "personal_info";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    if (activity != null) activity.overridePendingTransition(0, 0);
  }
  
  
// ------------------------------------------------ Service ------------------------------------------------
  
  @SuppressWarnings("deprecation")
  public void copyClipboard(String text) {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
      android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      clipboard.setText(text);
    }
    else {
      android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
      clipboard.setPrimaryClip(clip);
    }
  }
  
}
