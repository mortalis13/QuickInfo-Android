package org.mortalis.quickinfo;

import android.app.Application;


public class GlobalApp extends Application{

  // public ScanDict scanDict;
  // public ArticleParser aparser;
  
  private boolean appStarted = false;
  // public boolean enableSwap = true;
  
  public int mCurrentDrawerItem = -1;
  
  
  public void init(){
    if(appStarted) return;
    appStarted = true;
  }
  
}
