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
public class AssessmentsUI extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = AssessmentsUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private SimpleCursorAdapter mAdapter;
    private String courseCode = null, courseName = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessments_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            courseCode = extras.getString("Course Code");
            courseName = extras.getString("Course Name");

            //Actionbar title
            actionBar.setTitle(courseName + " Assessments");

            ListView listView = (ListView) findViewById(R.id.assessmentsui_listView);

            // Create the adapter object
            if (HomeUI.getDataAdapter() != null) {
                dbHelper = HomeUI.getDataAdapter();
            } else {
                dbHelper = new DataAdapter(this);
            }
            if (!dbHelper.isDBHelperOpen()) {
                dbHelper.open();
            }
            Cursor cursor = dbHelper.fetchAssessmentsDataByCourseCode(courseCode);

            if (cursor.getCount() != 0) {

                String[] listOfAssessments = new String[]{DataAdapter.KEY_ASSESSMENT_NAME, DataAdapter.KEY_ASSESSMENT_DUE};
                int[] to = new int[]{android.R.id.text1, android.R.id.text2};

                mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, listOfAssessments, to, 0);
                listView.setOnItemClickListener(this);
                listView.setAdapter(mAdapter);

            } else {
                Log.d(TAG, "Error populating data from database");
                setContentView(R.layout.empty_ui);
                TextView noDataTextView = (TextView) findViewById(R.id.emptyui_noData_textView);
                noDataTextView.setText("There are no assessments list for this course");
            }

        } else {
            Log.d(TAG, "Intent extra is empty");
            setContentView(R.layout.empty_ui);
            TextView noDataTextView = (TextView) findViewById(R.id.emptyui_noData_textView);
            noDataTextView.setText("There are no assessments list for this course");
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
            selectedString = selectedItemCursor.getString(selectedItemCursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_ID));
            Intent assessmentDetailsUIIntent = new Intent(this, AssessmentDetailsUI.class);
            assessmentDetailsUIIntent.putExtra("Assessment ID", selectedString);
            assessmentDetailsUIIntent.putExtra("Course Code", courseCode);
            startActivity(assessmentDetailsUIIntent);
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