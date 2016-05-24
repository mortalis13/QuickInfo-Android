package org.mortalis.quickinfo;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManager extends SQLiteOpenHelper {
  
    public static final String DB_NAME = "info";
    public static final String TABLE_NAME = "info";
    public static final String ID_FIELD = "_id";
    
    public static final String CUSTOM_ID_FIELD = "custom_id";
    public static final String VALUE_FIELD = "value";
    public static final String INFO_TYPE_FIELD = "info_type";
    
    
    public DatabaseManager(Context context) {
      super(context, DB_NAME, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("db", "onCreate");
        String sql = "CREATE TABLE " + TABLE_NAME
                + " (" + ID_FIELD + " INTEGER, "
                + CUSTOM_ID_FIELD + " INTEGER, "
                + INFO_TYPE_FIELD + " TEXT,"
                + VALUE_FIELD + " TEXT,"
                + " PRIMARY KEY (" + ID_FIELD + "));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        Log.d("db", "onUpdate");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


// ------------------------------------------------ Personal Info ------------------------------------------------

    public void updatePersonalInfo(String info) {
        Log.d("db", "updatePersonalInfo"); 
        
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_FIELD, 0);
        values.put(CUSTOM_ID_FIELD, 0);
        values.put(INFO_TYPE_FIELD, "personal_info");
        values.put(VALUE_FIELD, info);
        
        // long id = db.insert(TABLE_NAME, null, values);
        // long id = db.update(TABLE_NAME, values, CUSTOM_ID_FIELD + "=0", null);
        // long id = db.update(TABLE_NAME, values, null, null);
        
        int id = (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("db", "insertWithOnConflict-res: " + id);
        
        // long res = db.replace(TABLE_NAME, null, values);
        // Log.d("db", "replace-res: " + res);
        
        // int id = (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        // if (id == -1) {
        //   db.update(TABLE_NAME, values, null, null);  // number 1 is the _id here, update to variable for your code
        // }
        
        db.close();
    }
    
    public String getPersonalInfo() {
        Log.d("db", "getPersonalInfo");
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='personal_info'", null, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
          Log.d("db", "cursor-ok");
          String info = cursor.getString(0);
          db.close();
          return info;
        }
        
        Log.d("db", "cursor-null");
        db.close();
        return null;
    }
    
    
// ------------------------------------------------ Quick Note ------------------------------------------------
    
    public void updateQuicknoteInfo(String info) {
        Log.d("db", "updateQuicknoteInfo"); 
        SQLiteDatabase db = null;
        
        db = this.getReadableDatabase();
        String[] cols = {ID_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='quick_note'", null, null, null, null, null);
        
        int id = -1;
        if (cursor != null && cursor.moveToFirst()) {
          id = cursor.getInt(0);
          db.close();
        }
        
        
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(id != -1)
          values.put(ID_FIELD, id);
        values.put(INFO_TYPE_FIELD, "quick_note");
        values.put(VALUE_FIELD, info);
        
        int res = (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("db", "insertWithOnConflict-res: " + res);
        
        db.close();
    }
    
    public String getQuicknoteInfo() {
        Log.d("db", "getQuicknoteInfo");
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='quick_note'", null, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
          Log.d("db", "cursor-ok");
          String info = cursor.getString(0);
          db.close();
          return info;
        }
        
        Log.d("db", "cursor-null");
        db.close();
        return null;
    }
    
    
// ------------------------------------------------ Timetables ------------------------------------------------
    
    public void addTimetableItem(String info) {
        Log.d("db", "addTimetableItem"); 
        
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INFO_TYPE_FIELD, "timetable");
        values.put(VALUE_FIELD, info);
        
        long id = db.insert(TABLE_NAME, null, values);

        db.close();
    }
    
    public List<String> getTimetables() {
        Log.d("db", "getTimetables");
        List<String> res = null;
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='timetable'", null, null, null, null, null);
        
        if (cursor != null){
          res = new ArrayList<String>();
          
          while(cursor.moveToNext()) {
            String info = cursor.getString(0);
            info = info.split("\n")[0];
            res.add(info);
          }
        }
        
        db.close();
        return res;
    }
    
    public String getTimetable(int position) {
        Log.d("db", "getTimetable");
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='timetable'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            String info = cursor.getString(0);
            db.close();
            return info;
          }
        }
        
        db.close();
        return null;
    }
    
    public void updateTimetableItem(String info, int position) {
        Log.d("db", "updateTimetableItem");
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] cols = {ID_FIELD, VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='timetable'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            int id = cursor.getInt(0);
            
            ContentValues values = new ContentValues();
            values.put(VALUE_FIELD, info);
            int res = db.update(TABLE_NAME, values, ID_FIELD + "=" + id, null);
            Log.d("db", "update-res: " + res);
          }
        }
        
        db.close();
    }
    
    public void deleteTimetableItem(int position) {
        Log.d("db", "deleteTimetableItem");
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] cols = {ID_FIELD, VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='timetable'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            int id = cursor.getInt(0);
            
            int res = db.delete(TABLE_NAME, ID_FIELD + "=" + id, null);
            Log.d("db", "delete-res: " + res);
          }
        }
        
        db.close();
    }
    
    
// ------------------------------------------------ Notes ------------------------------------------------
    
    public void addNoteItem(String info) {
        Log.d("db", "addNoteItem"); 
        
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INFO_TYPE_FIELD, "note");
        values.put(VALUE_FIELD, info);
        
        long id = db.insert(TABLE_NAME, null, values);

        db.close();
    }
    
    public List<String> getNotes() {
        Log.d("db", "getNotes");
        List<String> res = null;
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='note'", null, null, null, null, null);
        
        if (cursor != null){
          res = new ArrayList<String>();
          
          while(cursor.moveToNext()) {
            String info = cursor.getString(0);
            info = info.split("\n")[0];
            res.add(info);
          }
        }
        
        db.close();
        return res;
    }
    
    public String getNote(int position) {
        Log.d("db", "getNote");
        
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = {VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='note'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            String info = cursor.getString(0);
            db.close();
            return info;
          }
        }
        
        db.close();
        return null;
    }
    
    public void updateNoteItem(String info, int position) {
        Log.d("db", "updateNoteItem");
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] cols = {ID_FIELD, VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='note'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            int id = cursor.getInt(0);
            
            ContentValues values = new ContentValues();
            values.put(VALUE_FIELD, info);
            int res = db.update(TABLE_NAME, values, ID_FIELD + "=" + id, null);
            Log.d("db", "update-res: " + res);
          }
        }
        
        db.close();
    }
    
    public void deleteNoteItem(int position) {
        Log.d("db", "deleteNoteItem");
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] cols = {ID_FIELD, VALUE_FIELD};
        Cursor cursor = db.query("info", cols, "info_type='note'", null, null, null, null, null);
        
        if (cursor != null){
          if(cursor.moveToPosition(position)){
            int id = cursor.getInt(0);
            
            int res = db.delete(TABLE_NAME, ID_FIELD + "=" + id, null);
            Log.d("db", "delete-res: " + res);
          }
        }
        
        db.close();
    }
    
}
