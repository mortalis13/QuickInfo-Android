package org.mortalis.quickinfo.model;

public class NoteListItem {
  
  private int id;
  private String info;
  
  public NoteListItem(int id, String info) {
    this.id = id;
    this.info = info;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
  
  public String getInfo() {
    return info;
  }
  
}