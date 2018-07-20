package org.mortalis.quickinfo;

import java.util.ArrayList;
import java.util.List;

import org.mortalis.quickinfo.model.NoteModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManager {
  
  public static final String DATABASE_NAME = "info.db";
  public static final int DATABASE_VERSION = 1;
  
  public static final String INFO_TABLE_NAME = "info";
  public static final String ID_FIELD = "_id";
  
  public static final String VALUE_FIELD = "value";
  public static final String INFO_TYPE_FIELD = "info_type";
  public static final String IS_ACTIVE_FIELD = "is_active";
  
  
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
    Log.d("db", "updatePersonalInfo");
    
    ContentValues values = new ContentValues();
    values.put(ID_FIELD, 0);
    values.put(INFO_TYPE_FIELD, "personal_info");
    values.put(VALUE_FIELD, info);
    
    int id = (int) db.insertWithOnConflict(INFO_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    Log.d("db", "insertWithOnConflict-res: " + id);
  }
  
  public static String getPersonalInfo() {
    Log.d("db", "getPersonalInfo");
    
    String[] cols = { VALUE_FIELD };
    Cursor cursor = db.query(INFO_TABLE_NAME, cols, "info_type='personal_info'", null, null, null, null, null);
    
    if (cursor != null && cursor.moveToFirst()) {
      Log.d("db", "cursor-ok");
      String info = cursor.getString(0);
      cursor.close();
      return info;
    }
    
    Log.d("db", "cursor-null");
    return null;
  }
  
  
// ------------------------------------------------ Quick Note ------------------------------------------------
  
  public static void updateQuicknoteInfo(String info) {
    Log.d("db", "updateQuicknoteInfo");
    
    ContentValues values = new ContentValues();
    // values.put(ID_FIELD, 1);
    values.put(INFO_TYPE_FIELD, "quick_note");
    values.put(VALUE_FIELD, info);
    
    int id = (int) db.insertWithOnConflict(INFO_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    Log.d("db", "insertWithOnConflict-res: " + id);
  }
  
  public static String getQuicknoteInfo() {
    Log.d("db", "getQuicknoteInfo");
    
    String[] cols = { VALUE_FIELD };
    String order = ID_FIELD + " DESC";
    Cursor cursor = db.query(INFO_TABLE_NAME, cols, "info_type='quick_note'", null, null, null, order);
    
    if (cursor.getCount() > 0) {
      Log.d("db", "cursor-ok");
      cursor.moveToFirst();
      String info = cursor.getString(0);
      cursor.close();
      return info;
    }
    
    Log.d("db", "cursor-null");
    return null;
  }
  
  
// ------------------------------------------------ Notes ------------------------------------------------
  
  public static void addNoteItem(String info) {
    Log.d("db", "addNoteItem");
    
    ContentValues values = new ContentValues();
    values.put(INFO_TYPE_FIELD, "note");
    values.put(VALUE_FIELD, info);
    
    long id = db.insert(INFO_TABLE_NAME, null, values);
  }
  
  public static void updateNoteItem(String info, int id) {
    Log.d("db", "updateNoteItem");
    
    ContentValues values = new ContentValues();
    values.put(VALUE_FIELD, info);
    int res = db.update(INFO_TABLE_NAME, values, ID_FIELD + "=" + id, null);
    Log.d("db", "update-res: " + res);
  }
  
  public static void deleteNoteItem(int id) {
    Log.d("db", "deleteNoteItem");
    
    int res = db.delete(INFO_TABLE_NAME, ID_FIELD + "=" + id, null);
    Log.d("db", "delete-res: " + res);
  }
  
  public static List<NoteModel> getNotes() {
    Log.d("db", "getNotes");
    List<NoteModel> res = new ArrayList<NoteModel>();
    
    String[] cols = { ID_FIELD, VALUE_FIELD };
    String order = VALUE_FIELD + " COLLATE NOCASE ASC";
    Cursor cursor = db.query(INFO_TABLE_NAME, cols, "info_type='note'", null, null, null, order);
    
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
    
    return res;
  }
  
  public static String getNote(int id) {
    Log.d("db", "getNote");
    
    String[] cols = { VALUE_FIELD };
    Cursor cursor = db.query(INFO_TABLE_NAME, cols, "info_type='note' AND " + ID_FIELD + "=" + id, null, null, null, null, null);
    
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      String info = cursor.getString(0);
      cursor.close();
      return info;
    }
    
    return null;
  }
  
  
  private static class DatabaseHelper extends SQLiteOpenHelper {
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
      Log.d("db", "onCreate");
      db.execSQL(INFO_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
      Log.d("db", "onUpdate");
    }
  }

}
