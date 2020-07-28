package org.mortalis.quickinfo.fragments;

import java.util.ArrayList;
import java.util.List;

import org.mortalis.quickinfo.DatabaseManager;
import org.mortalis.quickinfo.R;
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
import android.widget.TextView;
import android.view.View.OnClickListener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.LinearLayoutManager;


public class NotesFragment extends PageFragment {
  
  public static final String FRAGMENT_TAG = "NotesFragment";
  
  private Context context;
  private Activity activity;
  
  private RecyclerView notesListView;
  private RecyclerView.Adapter notesAdapter;
  private RecyclerView.LayoutManager layoutManager;
  
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
    notesListView = rootView.findViewById(R.id.notesList);
    
    layoutManager = new LinearLayoutManager(context);
    notesListView.setLayoutManager(layoutManager);
    
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
    
    List<NoteModel> noteModelList = DatabaseManager.getNotes();
    List<NoteListItem> notesList = new ArrayList<>();
    for (NoteModel model: noteModelList) {
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
    notesAdapter = new NotesAdapter(notesList);
    notesListView.setAdapter(notesAdapter);
  }
  
  public void addAction() {
    Intent intent = new Intent(context, EditorActivity.class);
    String editorType = "note_add";
    intent.putExtra("editor_type", editorType);
    startActivity(intent);
    if (activity != null) activity.overridePendingTransition(0, 0);
  }
  
  
  // -------------------
  
  public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private List<NoteListItem> items;

    public class MyViewHolder extends RecyclerView.ViewHolder {
      public TextView textView;
      public MyViewHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.noteText);
      }
    }

    public NotesAdapter(List<NoteListItem> items) {
      this.items = items;
    }

    public NotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
      view.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          int itemPosition = notesListView.getChildLayoutPosition(v);
          NoteListItem item = items.get(itemPosition);
          int noteId = item.getId();
          String info = DatabaseManager.getNote(noteId);
          showNoteInfo(info, noteId);
        }
      });
      
      MyViewHolder holder = new MyViewHolder(view);
      return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
      holder.textView.setText(items.get(position).getInfo());
    }

    public int getItemCount() {
      return items.size();
    }
  }
  
}
