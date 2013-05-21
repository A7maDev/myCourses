package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User: A7madY911
 */
public class AssessmentDetailsUI extends Activity {

    private static final String TAG = AssessmentDetailsUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String courseCode = null, assessmentID = null;
    private String assessmentGrade = null, assessmentRowID = null;
    private TextView assessmentGradeTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessmentdetails_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Assessment Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            assessmentID = extras.getString("Assessment ID");
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
            cursor = dbHelper.fetchAssessmentDetailsDataByID(assessmentID, courseCode);

            //populate data to view
            TextView assessmentNameTextView = (TextView) findViewById(R.id.assessmentsui_nameTextView);
            TextView assessmentWeightTextView = (TextView) findViewById(R.id.assessmentsui_weightTextView);
            TextView assessmentDueTextView = (TextView) findViewById(R.id.assessmentsui_dueTextView);
            assessmentGradeTextView = (TextView) findViewById(R.id.assessmentsui_gradeTextView);
            if (cursor.moveToFirst()) {
                assessmentNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_NAME)));
                assessmentWeightTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_WEIGHT)));
                assessmentDueTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_DUE)));
                assessmentGradeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_GRADE)));
                assessmentRowID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ROW_ID));

                //grade text view clickable
                assessmentGradeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addGrade();
                    }
                });

            } else {
                Log.d(TAG, "Error populating data from database");
                setContentView(R.layout.empty_ui);
            }

        } else {
            Log.d(TAG, "Intent extra is empty");
            setContentView(R.layout.empty_ui);
            TextView noDataTextView = (TextView) findViewById(R.id.emptyui_noData_textView);
            noDataTextView.setText("There are no assessment details information");

        }
    }

    private void addGrade() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = inflater.inflate(
                R.layout.add_grade_dialog, null);
        final EditText assessmentGradeEditText = (EditText) dialogView.findViewById(R.id.assessmentui_gradeEditText);
        builder.setTitle(R.string.assessmentui_title_gradedialog).setView(dialogView)
                .setPositiveButton(R.string.assessmentsui_button_save,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                assessmentGrade = "%" + assessmentGradeEditText.getText().toString();
                                if (assessmentGrade != null) {
                                    updateGradeOnSave();
                                }
                            }
                        })
                .setNegativeButton(R.string.assessmentsui_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void updateGradeOnSave() {
        if (!dbHelper.isDBHelperOpen()) {
            dbHelper.open();
        }

        try {
            assessmentGradeTextView.setText(assessmentGrade);
            int rowsChanged = dbHelper.updateAssessmentGrade(assessmentGrade, assessmentID, assessmentRowID);
            if (rowsChanged > 0) {
                Toast.makeText(this, "Assessment grade updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Assessment grade update failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "updateGradeOnSave " + e.getMessage());
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

    @Override
    public void onStop() {
        super.onStop();

        //close database helper if its open
        try {
            if (dbHelper.isDBHelperOpen()) {
                dbHelper.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "onStop - Close DBHelper: " + e.getMessage());
        }
    }
}
