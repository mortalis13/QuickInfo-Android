package org.mortalis.quickinfo.fragments;

import android.support.v4.app.Fragment;

public abstract class PageFragment extends Fragment {
  
  abstract public void loadData();
  
  abstract public void executeMainAction();
  
}
