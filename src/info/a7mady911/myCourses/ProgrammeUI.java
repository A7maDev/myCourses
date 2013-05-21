package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * User: A7madY911
 */
public class ProgrammeUI extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = ProgrammeUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private SimpleCursorAdapter mAdapter;
    private String progCode = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programme_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            progCode = extras.getString("Programme Code");

            //Actionbar title
            actionBar.setTitle(progCode + " Tutors List");

            ListView listView = (ListView) findViewById(R.id.programmeui_listView);

            // Create the adapter object
            if (HomeUI.getDataAdapter() != null) {
                dbHelper = HomeUI.getDataAdapter();
            } else {
                dbHelper = new DataAdapter(this);
            }
            if (!dbHelper.isDBHelperOpen()) {
                dbHelper.open();
            }
            Cursor cursor = dbHelper.fetchStaffListDataByProgCode(progCode);

            if (cursor.getCount() != 0) {

                String[] listOfAssessments = new String[]{DataAdapter.KEY_STAFF_NAME};
                int[] to = new int[]{android.R.id.text1};

                mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, listOfAssessments, to, 0);
                listView.setOnItemClickListener(this);
                listView.setAdapter(mAdapter);

            } else {
                Log.d(TAG, "Error populating data from database");
                setContentView(R.layout.empty_ui);
                TextView noDataTextView = (TextView) findViewById(R.id.emptyui_noData_textView);
                noDataTextView.setText("There are no tutors list for this programme");
            }

        } else {
            Log.d(TAG, "Intent extra is empty");
            setContentView(R.layout.empty_ui);
            TextView noDataTextView = (TextView) findViewById(R.id.emptyui_noData_textView);
            noDataTextView.setText("There are no tutors list for this programme");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Close cursor
        try {
            dbHelper.close();
        } catch (Exception e) {
            Log.e(TAG, "onStop - Close cursor: " + e.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor selectedItemCursor = (Cursor) mAdapter.getItem(position);
        String selectedString = null;
        if (selectedItemCursor != null) {
            selectedString = selectedItemCursor.getString(selectedItemCursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));
            Intent staffUIIntent = new Intent(this, StaffUI.class);
            staffUIIntent.putExtra("Staff ID", selectedString);
            startActivity(staffUIIntent);
        } else {
            Toast.makeText(view.getContext(), "Not able to get the selected item", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}