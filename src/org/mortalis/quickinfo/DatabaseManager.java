package org.mortalis.quickinfo;

import java.util.ArrayList;
import java.util.List;

import org.mortalis.quickinfo.model.NoteModel;
import org.mortalis.quickinfo.utils.Fun;
import org.mortalis.quickinfo.utils.Vars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {
  
  private static SQLiteDatabase db;
  private static DatabaseHelper dbHelper;
  
  
  public static void init(Context context) {
    if (db == null) {
      dbHelper = new DatabaseHelper(context);
      db = dbHelper.getWritableDatabase();
    }
  }
  
  
// ------------------------------------------------ Personal Info ------------------------------------------------
  
  public static void updatePersonalInfo(String info) {
    Fun.logd("updatePersonalInfo");
    
    try {
      ContentValues values = new ContentValues();
      values.put(DatabaseHelper.ID_FIELD, 0);
      values.put(DatabaseHelper.INFO_TYPE_FIELD, DatabaseHelper.INFO_TYPE_INFO);
      values.put(DatabaseHelper.VALUE_FIELD, info);
      
      int id = (int) db.insertWithOnConflict(DatabaseHelper.INFO_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
      Fun.logd("insertWithOnConflict-res: " + id);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String getPersonalInfo() {
    Fun.logd("getPersonalInfo");
    
    try {
      String[] cols = { DatabaseHelper.VALUE_FIELD };
      String where = DatabaseHelper.INFO_TYPE_FIELD + "='" + DatabaseHelper.INFO_TYPE_INFO + "'";
      Cursor cursor = db.query(DatabaseHelper.INFO_TABLE_NAME, cols, where, null, null, null, null, null);
      
      if (cursor != null && cursor.moveToFirst()) {
        Fun.logd("cursor-ok");
        String info = cursor.getString(0);
        cursor.close();
        return info;
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    Fun.logd("cursor-null");
    return null;
  }
  
  
// ------------------------------------------------ Quick Note ------------------------------------------------
  
  public static void updateQuicknoteInfo(String info) {
    Fun.logd("updateQuicknoteInfo");
    
    try {
      ContentValues values = new ContentValues();
      // values.put(ID_FIELD, 1);
      values.put(DatabaseHelper.INFO_TYPE_FIELD, DatabaseHelper.INFO_TYPE_QUICK_NOTE);
      values.put(DatabaseHelper.VALUE_FIELD, info);
      
      int id = (int) db.insertWithOnConflict(DatabaseHelper.INFO_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
      Fun.logd("insertWithOnConflict-res: " + id);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String getQuicknoteInfo() {
    Fun.logd("getQuicknoteInfo");
    
    try {
      String[] cols = { DatabaseHelper.VALUE_FIELD };
      String order = DatabaseHelper.ID_FIELD + " DESC";
      String where = DatabaseHelper.INFO_TYPE_FIELD + "='" + DatabaseHelper.INFO_TYPE_QUICK_NOTE + "'";
      Cursor cursor = db.query(DatabaseHelper.INFO_TABLE_NAME, cols, where, null, null, null, order);
      
      if (cursor.getCount() > 0) {
        Fun.logd("cursor-ok");
        cursor.moveToFirst();
        String info = cursor.getString(0);
        cursor.close();
        return info;
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    Fun.logd("cursor-null");
    return null;
  }
  
  
// ------------------------------------------------ Notes ------------------------------------------------
  
  public static void addNoteItem(String info) {
    Fun.logd("addNoteItem");
    
    try {
      ContentValues values = new ContentValues();
      values.put(DatabaseHelper.INFO_TYPE_FIELD, DatabaseHelper.INFO_TYPE_NOTE);
      values.put(DatabaseHelper.VALUE_FIELD, info);
      
      long id = db.insert(DatabaseHelper.INFO_TABLE_NAME, null, values);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void updateNoteItem(String info, int id) {
    Fun.logd("updateNoteItem");
    
    try {
      ContentValues values = new ContentValues();
      values.put(DatabaseHelper.VALUE_FIELD, info);
      String where = DatabaseHelper.ID_FIELD + "=?";
      String[] args = new String[] {String.valueOf(id)};
      int res = db.update(DatabaseHelper.INFO_TABLE_NAME, values, where, args);
      Fun.logd("update-res: " + res);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void deleteNoteItem(int id) {
    Fun.logd("deleteNoteItem");
    
    try {
      String where = DatabaseHelper.ID_FIELD + "=?";
      String[] args = new String[] {String.valueOf(id)};
      int res = db.delete(DatabaseHelper.INFO_TABLE_NAME, where, args);
      Fun.logd("delete-res: " + res);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static List<NoteModel> getNotes() {
    Fun.logd("getNotes");
    
    List<NoteModel> res = new ArrayList<NoteModel>();
    
    try {
      String[] cols = { DatabaseHelper.ID_FIELD, DatabaseHelper.VALUE_FIELD };
      String order = DatabaseHelper.VALUE_FIELD + " COLLATE NOCASE ASC";
      String where = DatabaseHelper.INFO_TYPE_FIELD + "='" + DatabaseHelper.INFO_TYPE_NOTE + "'";
      Cursor cursor = db.query(DatabaseHelper.INFO_TABLE_NAME, cols, where, null, null, null, order);
      
      if (cursor.getCount() > 0) {
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          int id = cursor.getInt(0);
          String info = cursor.getString(1);
          info = info.split("\n")[0];
          
          NoteModel item = new NoteModel(id, info);
          res.add(item);
        }
        
        cursor.close();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return res;
  }
  
  public static String getNote(int id) {
    Fun.logd("getNote");
    
    try {
      String[] cols = { DatabaseHelper.VALUE_FIELD };
      String where = String.format("%s=? AND %s=?", DatabaseHelper.INFO_TYPE_FIELD, DatabaseHelper.ID_FIELD);
      String[] args = new String[] {DatabaseHelper.INFO_TYPE_NOTE, String.valueOf(id)};
      Cursor cursor = db.query(DatabaseHelper.INFO_TABLE_NAME, cols, where, args, null, null, null, null);
      
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        String info = cursor.getString(0);
        cursor.close();
        return info;
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  private static class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = Vars.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    
    public static final String INFO_TABLE_NAME = "info";
    public static final String ID_FIELD = "_id";
    
    public static final String VALUE_FIELD = "value";
    public static final String INFO_TYPE_FIELD = "info_type";
    public static final String IS_ACTIVE_FIELD = "is_active";
    
    public static final String INFO_TYPE_INFO = "personal_info";
    public static final String INFO_TYPE_QUICK_NOTE = "quick_note";
    public static final String INFO_TYPE_NOTE = "note";
    
    
    private static final String INFO_TABLE_CREATE = 
        "CREATE TABLE " + INFO_TABLE_NAME + " (" + 
          ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
          INFO_TYPE_FIELD + " TEXT," + 
          VALUE_FIELD + " TEXT," + 
          IS_ACTIVE_FIELD + " INTEGER" + 
        ");";
    
    
    public DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(INFO_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }
  }
  
}
