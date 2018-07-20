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
  
}
