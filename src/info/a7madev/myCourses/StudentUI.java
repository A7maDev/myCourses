package info.a7madev.myCourses;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class StudentUI extends Fragment implements View.OnClickListener {

    private static final String TAG = StudentUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String staffID = null, progCode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

            //fetch data
            try {
                cursor = dbHelper.fetchStudentData();
            } catch (Exception e) {
                sendACRAReport(e);
            }

            //set Actionbar title
            getActivity().getActionBar().setTitle("Student");

            //populate data to view
            EditText studentIDTextView = (EditText) getActivity().findViewById(R.id.studentui_studentIDEditText);
            EditText studentNameTextView = (EditText) getActivity().findViewById(R.id.studentui_studentNameEditText);
            EditText studentEmailTextView = (EditText) getActivity().findViewById(R.id.studentui_studentEmailEditText);
            EditText studentMentorTextView = (EditText) getActivity().findViewById(R.id.studentui_studentMentorEditText);
            EditText studentProgrammeTextView = (EditText) getActivity().findViewById(R.id.studentui_studentProgrammeEditText);
            ImageView mentorAction = (ImageView) getActivity().findViewById(R.id.studentui_mentorAction);
            ImageView progAction = (ImageView) getActivity().findViewById(R.id.studentui_progAction);

            if (cursor.moveToFirst()) {
                studentIDTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_ID)));
                studentNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_NAME)));
                studentEmailTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_EMAIL)));
                studentMentorTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_NAME)));
                progCode = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_PROG_CODE));
                studentProgrammeTextView.setText(progCode);
                staffID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));

                //set all onClick listeners
                mentorAction.setOnClickListener(this);
                progAction.setOnClickListener(this);

            } else {
                sendACRAReport(null);
                showEmptyUI("No relevant student details found!");
            }

            //close cursor
            dbHelper.close();

        } catch (SQLException e) {
            sendACRAReport(e);
            showEmptyUI("No relevant student details found!");
        } catch (IllegalArgumentException e) {
            sendACRAReport(e);
            showEmptyUI("No relevant student details found!");
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.studentui_mentorAction:
                showMentor();
                break;
            case R.id.studentui_progAction:
                showProgramme();
                break;
        }
    }

    private void showProgramme() {
        try {
            Fragment fragment = new ProgrammeStaffUI();
            Bundle extras = new Bundle();
            extras.putString("Programme Code", progCode);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void showMentor() {
        try {
            Fragment fragment = new StaffUI();
            Bundle extras = new Bundle();
            extras.putString("Staff ID", staffID);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}
