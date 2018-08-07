package org.mortalis.quickinfo.utils;

import android.os.Environment;

public class Vars {
  
  public static final String PACKAGE_NAME = "org.mortalis.quickinfo";
  public static final String PROJECT_EXTERNAL_FOLDER = "QuickInfo";
  public static final String DEFAULT_APP_DATA_DIR_PATH = Environment.getExternalStorageDirectory() + "/" + PROJECT_EXTERNAL_FOLDER;
  
  public enum LogLevel {VERBOSE, DEBUG, INFO, WARN, ERROR};
  public static final LogLevel APP_LOG_LEVEL = LogLevel.DEBUG;
  
  public static final String APP_LOG_TAG = "quick-info";
  
  // ---
  
  public static final String FRAGMENT_TAG_ARG = "tag";
  public static final String DATABASE_NAME = "info.db";
  
  public static final String EXTRA_EDITOR_TYPE = "editor_type";
  public static final String EXTRA_CONTENT = "content";
  public static final String EXTRA_ID = "id";
  
  public static final String PROP_EDITOR_TYPE_INFO = "personal_info";
  public static final String PROP_EDITOR_TYPE_QUICK_NOTE = "quick_note";
  public static final String PROP_EDITOR_TYPE_NOTE_EDIT = "note_edit";
  public static final String PROP_EDITOR_TYPE_NOTE_ADD = "note_add";
  
  public static final String TAB_TITLE_INFO = "Info";
  public static final String TAB_TITLE_NOTES = "Notes";
  public static final String TAB_TITLE_QUICK_NOTE = "Quick Note";
  
}
