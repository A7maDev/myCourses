package info.a7madev.myCourses;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.acra.ACRA;


/**
 * User: A7maDev
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
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Intent userInterfaceIntent;
    private ActionBar actionBar;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_ui);

        //Create adapter object
        dbHelper = new DataAdapter(this);

        //Start the loading Async Task
        new loadingAsyncTask(this).execute();

        //Actionbar
        actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_launcher);

        this.invalidateOptionsMenu();

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

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
                actionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
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
            sendACRAReport(e);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        try {
            mDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                if (fragmentManager != null && fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Toast.makeText(this, "Stack cleared!", Toast.LENGTH_SHORT).show();
                }
                fragment = new HomeFragment();
                changeFragment(fragment, position);
                ACRA.getErrorReporter().putCustomData(TAG, "Home Fragment Selected");
                break;
            case 1:
                fragment = new StudentUI();
                changeFragment(fragment, position);
                break;
            case 2:
                mDrawerLayout.closeDrawer(mDrawerList);
                userInterfaceIntent = new Intent(this, TimetableUI2.class);
                startActivity(userInterfaceIntent);
                break;
            case 3:
                mDrawerLayout.closeDrawer(mDrawerList);
                fragment = new CoursesUI();
                changeFragment(fragment, position);
                break;
            case 4:
                mDrawerLayout.closeDrawer(mDrawerList);
                fragment = new AcadCalendarUI();
                changeFragment(fragment, position);
                break;
            case 5:
                mDrawerLayout.closeDrawer(mDrawerList);
                fragment = new UsefulLinksUI();
                changeFragment(fragment, position);
                break;
            case 6:
                mDrawerLayout.closeDrawer(mDrawerList);
                fragment = new EnrollmentUI();
                changeFragment(fragment, position);
                break;
        }
    }

    public void changeFragment(Fragment fragment, int position) {
        // update the main content by replacing fragments
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();

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
        }
        return super.onOptionsItemSelected(item);
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
            progressDialog.setCancelable(false);
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
        if (fragmentManager.getBackStackEntryCount() <= 1) {
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
        } else {
            super.onBackPressed();
        }
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
            sendACRAReport(e);
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}