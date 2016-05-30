package org.mortalis.quickinfo;

import java.io.File;

import org.mortalis.quickinfo.adds.FileChooser;
import org.mortalis.quickinfo.adds.FileChooser.FileSelectedListener;
import org.mortalis.quickinfo.adds.Functions;
import org.mortalis.quickinfo.fragments.NotesFragment;
import org.mortalis.quickinfo.fragments.PersonalInfoFragment;
import org.mortalis.quickinfo.fragments.QuickNoteFragment;
import org.mortalis.quickinfo.fragments.TimetablesFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  static final String SELECTED_DRAWER_ITEM = "drawerItem";
  
  private int mCurrentDrawerItem = -1;
  
  private CharSequence mTitle;
  private NavigationDrawerFragment mNavigationDrawerFragment;
  
  GlobalApp glob;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    Log.d("main", "onCreate()");
    
    glob = (GlobalApp) getApplicationContext();
    glob.init();
    
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    
    mTitle = "Personal Info";
    setTitle(mTitle);

    mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
    mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
  }
  
  @Override
  protected void onPause() {
    Log.d("main", "onPause() :: mCurrentDrawerItem: " + mCurrentDrawerItem);
    glob.mCurrentDrawerItem = mCurrentDrawerItem;
    super.onPause();
  }
  
  @Override
  protected void onResume() {
    Log.d("main", "onResume() :: mCurrentDrawerItem: " + mCurrentDrawerItem);
    Log.d("main", "onResume() :: glob.mCurrentDrawerItem: " + glob.mCurrentDrawerItem);
    
    if(glob.mCurrentDrawerItem != -1){
      mNavigationDrawerFragment.selectItem(glob.mCurrentDrawerItem);
    }
    
    super.onResume();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id){
      case R.id.action_exportdb:
        exportDB();
        return true;
      case R.id.action_importdb:
        importDB();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
  
  
  @Override
  public void onNavigationDrawerItemSelected(int position) {
    Fragment fragment = null;
    
    switch(position){
      case 0:
        fragment = new PersonalInfoFragment();
        mTitle = "Personal Info";
        break;
      case 1:
        fragment = new TimetablesFragment();
        mTitle = "Timetables";
        break;
      case 2:
        fragment = new NotesFragment();
        mTitle = "Notes";
        break;
      case 3:
        fragment = new QuickNoteFragment();
        mTitle = "Quick Note";
        break;
    }
    
    mCurrentDrawerItem = position;
    
    if(fragment == null) return;
    
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    setTitle(mTitle);
  }


// ------------------------------------------------ Actions ------------------------------------------------

  public void exportDB(){
    Functions.exportDB();
    toast("DB exported to: \"/sdcard/QuickInfo\"");
  }
  
  public void importDB(){
    new FileChooser(this).setFileListener(new FileSelectedListener() {
      @Override
      public void fileSelected(File file) {
        Functions.importDB(file);
        
        Log.d("main", "importDB::mCurrentDrawerItem: " + mCurrentDrawerItem);
        if(mCurrentDrawerItem != -1){
          mNavigationDrawerFragment.selectItem(mCurrentDrawerItem);
        }
      }
    }).showDialog();
  }


// ------------------------------------------------ Service ------------------------------------------------

  public void toast(String msg){
    int duration = Toast.LENGTH_LONG;
    Toast toast = Toast.makeText(this, msg, duration);
    toast.show();
  }

}
