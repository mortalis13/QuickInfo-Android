package org.mortalis.quickinfo.adapters;

import java.util.List;

import org.mortalis.quickinfo.R;
import org.mortalis.quickinfo.R.id;
import org.mortalis.quickinfo.R.layout;
import org.mortalis.quickinfo.model.NoteListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotesListAdapter extends ArrayAdapter<NoteListItem> {

  private Context context;
  
  private List<NoteListItem> items;
  
  private static final int itemLayoutId = R.layout.notes_list_item;
  
  
  public NotesListAdapter(Context context, List<NoteListItem> items) {
    super(context, itemLayoutId, items);
    this.items = items;
    this.context = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(itemLayoutId, parent, false);
    }

    TextView noteText = convertView.findViewById(R.id.noteText);
    
    NoteListItem item = getItem(position);
    if (item != null) {
      noteText.setText(item.getInfo());
    }

    return convertView;
  }
  
  public List<NoteListItem> getList() {
    return items;
  }

}
