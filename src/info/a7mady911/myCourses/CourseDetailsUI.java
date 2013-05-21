package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * User: A7madY911
 */
public class CourseDetailsUI extends Activity {

    private static final String TAG = CourseDetailsUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String courseCode = null, staffID = null, courseName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetails_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Course Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            courseCode = extras.getString("Course Code");

            // Create the adapter object
            if (HomeUI.getDataAdapter() != null) {
                dbHelper = HomeUI.getDataAdapter();
            } else {
                dbHelper = new DataAdapter(this);
            }
            if (!dbHelper.isDBHelperOpen()) {
                dbHelper.open();
            }
            cursor = dbHelper.fetchCourseDetailsDataByCode(courseCode);

            //setHasOptionsMenu(true);

            //populate data to view
            TextView courseCodeTextView = (TextView) findViewById(R.id.coursedetailsui_codeTextView);
            TextView courseNameTextView = (TextView) findViewById(R.id.coursedetailsui_nameTextView);
            TextView courseCreditTextView = (TextView) findViewById(R.id.coursedetailsui_creditTextView);
            TextView courseLevelTextView = (TextView) findViewById(R.id.coursedetailsui_levelTextView);
            TextView courseProgTextView = (TextView) findViewById(R.id.coursedetailsui_progTextView);
            TextView classCRNTextView = (TextView) findViewById(R.id.coursedetailsui_crnTextView);
            TextView classTutorTextView = (TextView) findViewById(R.id.coursedetailsui_tutorTextView);
            if (cursor.moveToFirst()) {
                courseCodeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_CODE)));
                courseName = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_NAME));
                courseNameTextView.setText(courseName);
                courseCreditTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_CREDIT)));
                courseLevelTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_Level)));
                courseProgTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_PROG_CODE)));
                classCRNTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_CLASS_CRN)));
                classTutorTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_NAME)));
                staffID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));

                //Tutor text view clickable
                classTutorTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tutorUIIntent = new Intent(CourseDetailsUI.this, StaffUI.class);
                        tutorUIIntent.putExtra("StaffID", staffID);
                        startActivity(tutorUIIntent);
                    }
                });
            } else {
                Log.d(TAG, "Error populating data from database");
                setContentView(R.layout.empty_ui);
            }

            //close cursor
            dbHelper.close();
        } else {
            Log.d(TAG, "Intent extra is empty");
            setContentView(R.layout.empty_ui);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_coursedetailsui, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_assessments:
                Intent courseDetailsUIIntent = new Intent(this, AssessmentsUI.class);
                courseDetailsUIIntent.putExtra("Course Code", courseCode);
                courseDetailsUIIntent.putExtra("Course Name", courseName);
                startActivity(courseDetailsUIIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
