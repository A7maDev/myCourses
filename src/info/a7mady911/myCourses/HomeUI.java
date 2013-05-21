package info.a7mady911.myCourses;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * User: A7madY911
 */
public class HomeUI extends Activity {

    private static final String TAG = HomeUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerMenuItems;

    private Bundle homeUISavedInstanceState;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Intent userInterfaceIntent;


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_ui);

        //Create adapter object
        dbHelper = new DataAdapter(this);

        //Start the loading Async Task
        new loadingAsyncTask(this).execute();

        mTitle = mDrawerTitle = getTitle();
        mDrawerMenuItems = getResources().getStringArray(R.array.NavDrawer_List);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeUISavedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        try {
            mDrawerToggle.syncState();
        } catch (Exception e) {
            Log.d(TAG, "onPostCreate: " + e.getMessage());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        try {
            mDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            Log.d(TAG, "onConfigurationChanged: " + e.getMessage());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (position == 0) {
            fragment = new EmptyFragment();
            changeFragment(fragment, position);
        } else if (position == 1) {
            fragment = new StudentUI();
            changeFragment(fragment, position);
        } else if (position == 2) {
            mDrawerLayout.closeDrawer(mDrawerList);
            userInterfaceIntent = new Intent(this, TimetableUI2.class);
            startActivity(userInterfaceIntent);
        } else if (position == 3) {
            fragment = new CoursesUI();
            changeFragment(fragment, position);
        } else if (position == 4) {
            mDrawerLayout.closeDrawer(mDrawerList);
            userInterfaceIntent = new Intent(this, UsefulLinksUI.class);
            startActivity(userInterfaceIntent);
        } else if (position == 5) {
            mDrawerLayout.closeDrawer(mDrawerList);
            userInterfaceIntent = new Intent(this, EnrollmentUI.class);
            startActivity(userInterfaceIntent);
        }
    }

    private void changeFragment(Fragment fragment, int position) {
        // update the main content by replacing fragments
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerMenuItems[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_homeui, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsUI.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_exit:
                try {
                    finish();
                } catch (Exception e) {
                    Intent exitIntent = new Intent(Intent.ACTION_MAIN);
                    exitIntent.addCategory(Intent.CATEGORY_HOME);
                    exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exitIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static DataAdapter getDataAdapter() {
        return dbHelper;
    }

    private class loadingAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private ProgressDialog progressDialog;

        public loadingAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //open database, insert data and close
            dbHelper.open();
            dbHelper.insertAllData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton(R.string.text_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                finish();
                            }
                        })
                .setNegativeButton(R.string.text_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.dismiss();
                            }
                        });
        builder.create().show();
    }

    @Override
    public void onStop() {
        super.onStop();

        //close database helper if its open
        try {
            if (dbHelper.isDBHelperOpen()) {
                dbHelper.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "onStop, Database Helper: " + e.getMessage());
        }
    }
}