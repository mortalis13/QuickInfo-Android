package org.mortalis.quickinfo.adds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.Environment;


public class Functions {
  
  static final String nl = "\n";
  static final String packageName = "org.mortalis.quickinfo";
  static final String dbName = "info";
  static final String projectExternalFolder = "QuickInfo";
  
  
  public static String readFile1(String file) {
    String doc = "", line = null;
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(file));               // read by lines
      while ((line = br.readLine()) != null) {
        doc += line+nl;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return doc;
  }
  
  public static String readFile(File file) {
    try {
      FileInputStream f = new FileInputStream(file.getPath());

      int fileSize = f.available();
      byte[] buf = new byte[fileSize];
      f.read(buf);
      
      f.close();

      return new String(buf);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  public static void importDB(File dbFile) {
    try {
        File sd = Environment.getExternalStorageDirectory();
        File data  = Environment.getDataDirectory();
        
        File dir = new File(sd + "/" + projectExternalFolder);
        if(!dir.exists())
          dir.mkdir();
        
        if (sd.canWrite()) {
            String currentDBPath= "//data//" + packageName + "//databases//" + dbName;
            // String currentDBPath= "/data/data/" + packageName + "//databases//" + dbName;
//             String backupDBPath  = "/" + projectExternalFolder + "/" + dbName;
//            String backupDBPath  = dbPath;
            
            File backupDB = new File(data, currentDBPath);
            // File currentDB = new File(sd, backupDBPath);
            File currentDB = dbFile;

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            
            src.close();
            dst.close();
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void exportDB() {
    try {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        
        File dir = new File(sd + "/" + projectExternalFolder);
        if(!dir.exists())
          dir.mkdir();

        if (sd.canWrite()) {
            String currentDBPath= "//data//" + packageName + "//databases//" + dbName;
            // String currentDBPath= "/data/data/" + packageName + "//databases//" + dbName;
            String backupDBPath  = "/" + projectExternalFolder + "/" + dbName;
            
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            
            src.close();
            dst.close();
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
      
}
