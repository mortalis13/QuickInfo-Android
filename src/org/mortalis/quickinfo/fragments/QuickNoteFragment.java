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
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


public class QuickNoteFragment extends PageFragment {
  
  public static final String FRAGMENT_TAG = "QuickNoteFragment";
  
  private Context context;
  private Activity activity;
  
  private WebView wvContent;
  private TextView noteContent;
  
  private View rootView;
  
  private boolean infoUpdated = false;
  
  
  public static QuickNoteFragment newInstance(Context context) {
    QuickNoteFragment fragment = new QuickNoteFragment();
    Bundle args = new Bundle();
    args.putString(Vars.FRAGMENT_TAG_ARG, FRAGMENT_TAG);
    fragment.setArguments(args);
    return fragment;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.quicknote_view, container, false);
    
    noteContent = (TextView) rootView.findViewById(R.id.noteContent);
    
    // noteContent.setOnClickListener(new OnClickListener() {
    //   public void onClick(View v) {
    //     editAction();
    //   }
    // });
    
    loadData();
    infoUpdated = true;
    
    return rootView;
  }
  
  @Override
  public void onResume() {
    Fun.logd("onResume::infoUpdated: " + infoUpdated);
    
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
    
    String info = DatabaseManager.getQuicknoteInfo();
    if (info != null) {
      loadInfo(info);
    }
  }
  
  @Override
  public void executeMainAction() {
    editAction();
  }
  
  
  public void loadInfo(String info) {
    noteContent.setText(info);
  }
  
  public void editAction() {
    Intent intent = new Intent(context, EditorActivity.class);
    String editorType = "quick_note";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    if (activity != null) activity.overridePendingTransition(0, 0);
  }
  
}
