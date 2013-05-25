package info.a7madev.myCourses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class AssessmentDetailsUI extends Fragment {

    private static final String TAG = AssessmentDetailsUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String courseCode = null, assessmentID = null;
    private String assessmentGrade = null, assessmentRowID = null;
    private TextView assessmentGradeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.assessmentdetails_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("Assessment Details");

        //Get data from Bundle
        Bundle extras = this.getArguments();
        if (extras != null) {
            assessmentID = extras.getString("Assessment ID");
            courseCode = extras.getString("Course Code");

            try {
                // Create the database adapter object
                if (HomeUI.dbHelper != null) {
                    dbHelper = HomeUI.dbHelper;
                } else {
                    dbHelper = new DataAdapter(getActivity());
                }
                if (!dbHelper.isDBHelperOpen()) {
                    dbHelper.open();
                }
                cursor = dbHelper.fetchAssessmentDetailsDataByID(assessmentID, courseCode);

                //populate data to view
                EditText assessmentNameTextView = (EditText) getActivity().findViewById(R.id.assessmentsui_nameEditText);
                EditText assessmentWeightTextView = (EditText) getActivity().findViewById(R.id.assessmentsui_weightEditText);
                EditText assessmentDueTextView = (EditText) getActivity().findViewById(R.id.assessmentsui_dueEditText);
                assessmentGradeTextView = (EditText) getActivity().findViewById(R.id.assessmentsui_gradeEditText);
                ImageView assessmentGradeAction = (ImageView) getActivity().findViewById(R.id.assessmentsui_gradeAction);
                if (cursor.moveToFirst()) {
                    assessmentNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_NAME)));
                    assessmentWeightTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_WEIGHT)));
                    assessmentDueTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_DUE)));
                    assessmentGradeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ASSESSMENT_GRADE)));
                    assessmentRowID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_ROW_ID));

                    //grade action clickable
                    assessmentGradeAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addGrade();
                        }
                    });

                } else {
                    sendACRAReport(null);
                    showEmptyUI("There are no assessment details information");
                }
            } catch (SQLException e) {
                sendACRAReport(e);
                showEmptyUI("There are no assessment details information");
            } catch (IllegalArgumentException e) {
                sendACRAReport(e);
                showEmptyUI("There are no assessment details information");
            }
        } else {
            sendACRAReport(null);
            showEmptyUI("There are no assessment details information");
        }

    }

    private void showEmptyUI(String msg) {
        try {
            Fragment fragment = new EmptyFragment();
            Bundle extras = new Bundle();
            extras.putString("NoData Message", msg);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void addGrade() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                Toast.makeText(getActivity(), "Assessment grade updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Assessment grade update failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            sendACRAReport(e);
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
