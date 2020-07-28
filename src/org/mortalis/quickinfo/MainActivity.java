package org.mortalis.quickinfo;

import java.io.File;
import java.util.List;

import org.home.file_chooser_lib.FilePickerDialog;

import org.mortalis.quickinfo.components.CustomViewPager;
import org.mortalis.quickinfo.fragments.InfoFragment;
import org.mortalis.quickinfo.fragments.NotesFragment;
import org.mortalis.quickinfo.fragments.PageFragment;
import org.mortalis.quickinfo.fragments.QuickNoteFragment;
import org.mortalis.quickinfo.utils.Fun;
import org.mortalis.quickinfo.utils.Vars;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.view.View;


public class MainActivity extends AppCompatActivity {
  
  private FilePickerDialog filePickerDialog;
  private SectionsPagerAdapter sectionsPagerAdapter;
  
  private CustomViewPager viewPager;
  private TabLayout tabLayout;
  private ImageButton bMenu;
  
  private Context context;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Fun.logd("MainActivity.onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    context = this;
    init();
    Fun.setContext(context);
    DatabaseManager.init(context);
    
    Fun.createFolder(Vars.DEFAULT_APP_DATA_DIR_PATH);
    
    FragmentManager fm = getSupportFragmentManager();
    for (Fragment fragment: fm.getFragments()) {
      fm.beginTransaction().remove(fragment).commitNow();
    }
    
    viewPager = findViewById(R.id.viewPager);
    tabLayout = findViewById(R.id.tabLayout);
    bMenu = findViewById(R.id.bMenu);
    
    sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(sectionsPagerAdapter);
    viewPager.setScrollDuration(0);
    tabLayout.setupWithViewPager(viewPager);
    
    tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
      public void onTabReselected(Tab tab) {
        int position = tab.getPosition();
        
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        
        if (fragment != null && fragment instanceof PageFragment) {
          ((PageFragment) fragment).executeMainAction();
        }
      }

      public void onTabSelected(Tab tab) { }
      public void onTabUnselected(Tab tab) { }
    });
    
    bMenu.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Fun.openMenu(v);
      }
    });
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_exportdb:
        exportDB();
        return true;
      case R.id.action_importdb:
        importDB();
        return true;
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  
  // ---------------------------------------------------------------------
  
  private void init() {
    filePickerDialog = new FilePickerDialog(context);
    filePickerDialog.setExtensionFilter("db");
  }
  
  // ------------------------------ Actions ------------------------------
  
  public void exportDB() {
    Fun.exportDB();
    Fun.toast(context, "DB exported to: \"" + Vars.DEFAULT_APP_DATA_DIR_PATH + "\"");
  }
  
  public void importDB() {
    filePickerDialog.setFileSelectedListener(file -> {
      Fun.importDB(file);
      reloadPages();
    }).showDialog();
  }
  
  private void reloadPages() {
    FragmentManager fm = getSupportFragmentManager();
    List<Fragment> fragments = fm.getFragments();

    if (fragments == null) return;
    for (Fragment fragment: fragments) {
      if (fragment != null && fragment instanceof PageFragment) {
        ((PageFragment) fragment).loadData();
      }
    }
  }
  
  
  // ------------------------------ Classes ------------------------------
  
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
      Fun.logd("SectionsPagerAdapter.getItem(): " + position);
      
      Fragment fragment = null;
      
      switch (position) {
      case 0:
        fragment = InfoFragment.newInstance(context);
        break;
      case 1:
        fragment = NotesFragment.newInstance(context);
        break;
      case 2:
        fragment = QuickNoteFragment.newInstance(context);
        break;
      }
      
      return fragment;
    }
    
    @Override
    public int getCount() {
      return 3;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
      case 0:
        return Vars.TAB_TITLE_INFO;
      case 1:
        return Vars.TAB_TITLE_NOTES;
      case 2:
        return Vars.TAB_TITLE_QUICK_NOTE;
      }
      
      return null;
    }
  }
  
}
