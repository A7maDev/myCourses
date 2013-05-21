package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * User: A7madY911
 */
public class EnrollmentUI extends Activity {

    private static final String TAG = EnrollmentUI.class.getSimpleName();
    private String[] enrollmentDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Enrollment Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        enrollmentDetails = getResources().getStringArray(R.array.Enrollment_Details);

        if (enrollmentDetails != null) {
            TextView uniYearTextView = (TextView) findViewById(R.id.enrollui_uniYearTextView);
            uniYearTextView.setText(enrollmentDetails[0]);
            TextView uniDateTextView = (TextView) findViewById(R.id.enrollui_uniDateTextView);
            uniDateTextView.setText(enrollmentDetails[1]);
            TextView uniRequireTextView = (TextView) findViewById(R.id.enrollui_uniRequireTextView);
            uniRequireTextView.setText(enrollmentDetails[2]);
            TextView courseYearTextView = (TextView) findViewById(R.id.enrollui_courseYearTextView);
            courseYearTextView.setText(enrollmentDetails[3]);
            TextView courseDateTextView = (TextView) findViewById(R.id.enrollui_courseDateTextView);
            courseDateTextView.setText(enrollmentDetails[4]);
        } else {
            Log.d(TAG, "Error populating data from database");
            setContentView(R.layout.empty_ui);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
